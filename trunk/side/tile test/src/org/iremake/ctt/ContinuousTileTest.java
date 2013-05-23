/*
 * Copyright (C) 2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.iremake.ctt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.HyperlinkLabel;
import org.tools.ui.utils.GraphicsUtils;
import org.tools.ui.utils.LookAndFeel;
import org.tools.utils.Pair;

/**
 *
 */
public class ContinuousTileTest {

    private final static int NUMBER_TILES = 6; // NxN field
    private final static int PATTERN_SIZE = 20;
    private final static int TILE_SIZE = 80;
    private boolean[][] pattern = new boolean[NUMBER_TILES][NUMBER_TILES];
    private Random random = new Random();
    private JFrame frame;
    private JPanel patternPanel;
    private JTextField baseTerrain;
    private JTextField innerTile;
    private JTextField outerTile;
    private JPanel viewPanel;
    private Image baseGraphics;
    private Image innerGraphics;
    private Image outerGraphics;
    private JLabel basePreview;
    private JLabel innerPreview;
    private JLabel outerPreview;
    private ActionListener baseAction;
    private ActionListener innerAction;
    private ActionListener outerAction;
    private boolean oldMode;
    private static JFileChooser fileChooser;

    {
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".png") || f.getName().endsWith(".bmp");
            }

            @Override
            public String getDescription() {
                return "Image files (*.png, *.bmp)";
            }
        });
    }

    /**
     *
     */
    public ContinuousTileTest() {
        frame = new JFrame("Imperialism Continuous Tiles Tester");
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // frame.getContentPane().setBackground(new Color(230,220,210));

        HyperlinkLabel title = new HyperlinkLabel("More information");
        title.setToolTipText("Click to open the project website.");
        title.setLink("http://remake.twelvepm.de/forum/");

        frame.setLayout(new MigLayout("wrap 2, fill", "", "[][][grow]"));
        frame.add(title, "span 2, alignx center");
        frame.add(makePatternPanel(), "aligny top, grow");
        frame.add(makeTileSelectionPanel());
        frame.add(makeViewPanel(), "span 2, grow");

        frame.pack();
        frame.setVisible(true);

        // default is random pattern
        randomPattern();

        /*
         baseTerrain.setText("C:\\Users\\Jan\\Dropbox\\remake\\graphics\\Veneteaou\\continuous tiles\\plains.base.bmp");
         baseAction.actionPerformed(null);
         innerTile.setText("C:\\Users\\Jan\\Dropbox\\remake\\graphics\\Veneteaou\\continuous tiles\\forest.inner.png");
         innerAction.actionPerformed(null);
         outerTile.setText("C:\\Users\\Jan\\Dropbox\\remake\\graphics\\Veneteaou\\continuous tiles\\forest.outer.png");
         outerAction.actionPerformed(null);
         */
    }

    /**
     *
     */
    private void randomPattern() {
        for (int i = 0; i < NUMBER_TILES; i++) {
            for (int j = 0; j < NUMBER_TILES; j++) {
                pattern[i][j] = random.nextBoolean();
            }
        }
        patternPanel.repaint();
        viewPanel.repaint();
    }

    /**
     *
     * @return
     */
    private JComponent makePatternPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("Select Pattern"));

        patternPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;

                for (int i = 0; i < NUMBER_TILES; i++) {
                    for (int j = 0; j < NUMBER_TILES; j++) {
                        if (pattern[i][j] == true) {
                            g2d.setColor(Color.black);
                        } else {
                            g2d.setColor(Color.white);
                        }
                        g2d.fillRect(i * PATTERN_SIZE + (j % 2 == 1 ? PATTERN_SIZE / 2 : 0), j * PATTERN_SIZE, PATTERN_SIZE, PATTERN_SIZE);
                        // g2d.fillRect(i * PATTERN_SIZE + (j % 2 == 1 ? PATTERN_SIZE / 2 : 0) + 1, j * PATTERN_SIZE + 1, PATTERN_SIZE - 1, PATTERN_SIZE - 1);
                        // g2d.fillRect(i * PATTERN_SIZE, j * PATTERN_SIZE + 1, PATTERN_SIZE - 1, PATTERN_SIZE - 1);
                    }
                }
            }
        };
        patternPanel.setToolTipText("Click for setting/clearing the terrain.");
        Dimension size = new Dimension(NUMBER_TILES * PATTERN_SIZE + PATTERN_SIZE / 2 + 1, NUMBER_TILES * PATTERN_SIZE + 1);
        patternPanel.setPreferredSize(size);
        patternPanel.setBackground(Color.lightGray);
        patternPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int j = (e.getY() - 1) / PATTERN_SIZE;
                int i = (e.getX() - (j % 2 == 1 ? PATTERN_SIZE / 2 : 0) - 1) / PATTERN_SIZE;
                if (pattern[i][j] == true) {
                    pattern[i][j] = false;
                } else {
                    pattern[i][j] = true;
                }
                patternPanel.repaint();
                viewPanel.repaint();
            }
        });


        JButton clearPatternButton = new JButton("Clear");
        clearPatternButton.setToolTipText("Clears the map.");
        clearPatternButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < NUMBER_TILES; i++) {
                    for (int j = 0; j < NUMBER_TILES; j++) {
                        pattern[i][j] = false;
                    }
                }
                patternPanel.repaint();
                viewPanel.repaint();
            }
        });

        JButton randomPatternButton = new JButton("Random");
        randomPatternButton.setToolTipText("Random pattern on the map (50% occupation)");
        randomPatternButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomPattern();
            }
        });

        panel.setLayout(new MigLayout("fill"));
        panel.add(clearPatternButton);
        panel.add(randomPatternButton, "wrap");
        panel.add(patternPanel, "span 2, aligny center, w pref!, h pref!");

        return panel;
    }

    /**
     *
     * @param parent
     * @param component
     */
    private static void selectFile(Component parent, JTextComponent component) {
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            component.setText(f.getPath());
        }
    }

    /**
     *
     * @param parent
     * @param tile
     * @param icon
     * @param component
     */
    private static Image importGraphics(Component parent, JLabel icon, JTextComponent component) {
        Image tile = null;
        try {
            tile = importGraphics(component.getText());
            icon.setIcon(new ImageIcon(tile));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Cannot load graphics file.", "Error", JOptionPane.ERROR_MESSAGE);
            icon.setIcon(null);
        }
        return tile;
    }

    /**
     *
     * @return
     */
    private JComponent makeTileSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("Tile Selection"));

        baseTerrain = new JTextField();
        baseTerrain.setEditable(false);
        JButton selectBaseTerrain = new JButton("Select");
        selectBaseTerrain.setToolTipText("Select image file for base terrain.");
        baseAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContinuousTileTest.selectFile(frame, baseTerrain);
                baseGraphics = ContinuousTileTest.importGraphics(frame, basePreview, baseTerrain);
                viewPanel.repaint();
            }
        };
        selectBaseTerrain.addActionListener(baseAction);
        // basePreview = new JLabel("base", JLabel.CENTER);
        basePreview = new JLabel();
        basePreview.setToolTipText("Base terrain tile.");
        basePreview.setBorder(BorderFactory.createLineBorder(Color.black));

        innerTile = new JTextField();
        innerTile.setEditable(false);
        JButton selectInnerTile = new JButton("Select");
        selectInnerTile.setToolTipText("Select inner (continuous) image file for terrain");
        innerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContinuousTileTest.selectFile(frame, innerTile);
                innerGraphics = ContinuousTileTest.importGraphics(frame, innerPreview, innerTile);
                viewPanel.repaint();
            }
        };
        selectInnerTile.addActionListener(innerAction);

        // innerPreview = new JLabel("inner", JLabel.CENTER);
        innerPreview = new JLabel();
        innerPreview.setToolTipText("Inner (continuous) version of terrain tile.");
        innerPreview.setBorder(BorderFactory.createLineBorder(Color.black));

        outerTile = new JTextField();
        outerTile.setEditable(false);
        JButton selectOuterTile = new JButton("Select");
        selectOuterTile.setToolTipText("Select outer (edge) image file for terrain.");
        outerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContinuousTileTest.selectFile(frame, outerTile);
                outerGraphics = ContinuousTileTest.importGraphics(frame, outerPreview, outerTile);
                viewPanel.repaint();
            }
        };
        selectOuterTile.addActionListener(outerAction);

        // outerPreview = new JLabel("outer", JLabel.CENTER);
        outerPreview = new JLabel();
        outerPreview.setToolTipText("Outer (edge) version of terrain tile.");
        outerPreview.setBorder(BorderFactory.createLineBorder(Color.black));

        // reload button
        JButton update = new JButton("Reload images");
        update.setToolTipText("Reaload all image files again and update view.");
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baseGraphics = ContinuousTileTest.importGraphics(frame, basePreview, baseTerrain);
                innerGraphics = ContinuousTileTest.importGraphics(frame, innerPreview, innerTile);
                outerGraphics = ContinuousTileTest.importGraphics(frame, outerPreview, outerTile);
                viewPanel.repaint();
            }
        });
        final JToggleButton oldview = new JToggleButton("Old view");
        oldview.setToolTipText("Toggle old/new continuous mode.");
        oldMode = false;
        oldview.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                int state = itemEvent.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    oldview.setText("Continuous view");
                    oldMode = true;
                    viewPanel.repaint();
                } else {
                    oldview.setText("Old view");
                    oldMode = false;
                    viewPanel.repaint();
                }
            }
        });

        panel.setLayout(new MigLayout("wrap 3, fill", "[right]"));

        panel.add(new JLabel("Select base terrain"));
        panel.add(baseTerrain, "wmin 300");
        panel.add(selectBaseTerrain);

        panel.add(new JLabel("Select inner tile"));
        panel.add(innerTile, "wmin 300");
        panel.add(selectInnerTile);

        panel.add(new JLabel("Select outer tile"));
        panel.add(outerTile, "wmin 300");
        panel.add(selectOuterTile);

        panel.add(basePreview, String.format("spanx 3, split 5, alignx leading, w %d!, h %d!", TILE_SIZE, TILE_SIZE));
        panel.add(innerPreview, String.format("w %d!, h %d!", TILE_SIZE, TILE_SIZE));
        panel.add(outerPreview, String.format("w %d!, h %d!", TILE_SIZE, TILE_SIZE));
        panel.add(update, "aligny top");
        panel.add(oldview, "aligny top");

        return panel;
    }

    /**
     *
     * @return
     */
    private JComponent makeViewPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("View Tiles"));

        viewPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                // only if everything is successfull
                if (baseGraphics == null || innerGraphics == null || outerGraphics == null) {
                    return;
                }

                Graphics2D g2d = (Graphics2D) g;

                EnhancedTile tile = new EnhancedTile(innerGraphics, outerGraphics);

                for (int i = 0; i < NUMBER_TILES; i++) {
                    for (int j = 0; j < NUMBER_TILES; j++) {
                        int x = i * TILE_SIZE + (j % 2 == 1 ? TILE_SIZE / 2 : 0);
                        int y = j * TILE_SIZE;
                        if (pattern[i][j] == false) {
                            g2d.drawImage(baseGraphics, x, y, null);
                        } else {
                            List<Pair<TilesTransition, Boolean>> list = new ArrayList<>(6);
                            for (TilesTransition transition : TilesTransition.values()) {
                                list.add(new Pair<TilesTransition, Boolean>(transition, !isTerrain(i, j, transition) || oldMode));
                            }
                            tile.paintMany(g2d, x, y, list);
                        }
                    }
                }

            }
        };
        Dimension size = new Dimension(NUMBER_TILES * TILE_SIZE + TILE_SIZE / 2 + 1, NUMBER_TILES * TILE_SIZE + 1);
        viewPanel.setPreferredSize(size);
        viewPanel.setBackground(Color.gray);

        panel.setLayout(new MigLayout("fill"));
        panel.add(viewPanel, "w pref!, h pref!");

        return panel;
    }

    /**
     *
     * @param i column
     * @param j row
     * @param transition
     * @return
     */
    private boolean isTerrain(int i, int j, TilesTransition transition) {
        switch (transition) {
            case East:
                if (i == NUMBER_TILES - 1) {
                    return false;
                }
                i++;
                break;

            case West:
                if (i == 0) {
                    return false;
                }
                i--;
                break;

            case NorthWest:
                if (j == 0 || (i == 0 && j % 2 == 0)) {
                    return false;
                }
                if (j % 2 == 0) {
                    i--;
                }
                j--;
                break;

            case NorthEast:
                if (j == 0 || (i == NUMBER_TILES - 1 && j % 2 == 1)) {
                    return false;
                }
                if (j % 2 == 1) {
                    i++;
                }
                j--;
                break;

            case SouthWest:
                if (j == NUMBER_TILES - 1 || (i == 0 && j % 2 == 0)) {
                    return false;
                }
                if (j % 2 == 0) {
                    i--;
                }
                j++;
                break;

            case SouthEast:
                if (j == NUMBER_TILES - 1 || (i == NUMBER_TILES - 1 && j % 2 == 1)) {
                    return false;
                }
                if (j % 2 == 1) {
                    i++;
                }
                j++;
                break;
        }
        return pattern[i][j];
    }

    /**
     * Load and resize.
     */
    public static Image importGraphics(String text) throws IOException {
        BufferedImage original = ImageIO.read(new File(text));
        return GraphicsUtils.scaleBufferedImage(original, new Dimension(TILE_SIZE, TILE_SIZE));
    }

    /**
     * Just start.
     *
     * @param args
     */
    public static void main(String[] args) {
        LookAndFeel.setSystemLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContinuousTileTest();
            }
        });
    }
}
