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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.utils.GraphicsUtils;
import org.tools.ui.utils.LookAndFeel;

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

    /**
     *
     */
    public ContinuousTileTest() {
        frame = new JFrame("Imperialism Continuous Tiles Test");
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            }
        });

        frame.setLayout(new MigLayout("wrap 2, fill", "[grow][]", "[][grow]"));
        frame.add(makePatternPanel(), "aligny top, w pref!, h pref!");
        frame.add(makeTileSelectionPanel(), "aligny top");
        frame.add(makeViewPanel(), "span 2, hmin 200, grow");

        frame.pack();
        frame.setVisible(true);

        // default is random pattern
        randomPattern();

        baseTerrain.setText("C:\\Users\\Jan\\Dropbox\\remake\\graphics\\Veneteaou\\Plains.bmp");
        baseAction.actionPerformed(null);
        innerTile.setText("C:\\Users\\Jan\\Dropbox\\remake\\graphics\\Veneteaou\\Forest.bmp");
        innerAction.actionPerformed(null);
        outerTile.setText("C:\\Users\\Jan\\Dropbox\\remake\\graphics\\Veneteaou\\Hills.bmp");
        outerAction.actionPerformed(null);
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
        Dimension size = new Dimension(NUMBER_TILES * PATTERN_SIZE + PATTERN_SIZE / 2 + 1, NUMBER_TILES * PATTERN_SIZE + 1);
        patternPanel.setPreferredSize(size);
        patternPanel.setBackground(Color.gray);
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
        });;


        JButton clearPatternButton = new JButton("Clear");
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
        randomPatternButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomPattern();
            }
        });

        panel.setLayout(new MigLayout("fill"));
        panel.add(clearPatternButton);
        panel.add(randomPatternButton, "wrap");
        panel.add(patternPanel, "south, w pref!, h pref!");



        return panel;
    }

    /**
     *
     * @return
     */
    private JComponent makeTileSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Tile Selection"));

        baseTerrain = new JTextField();
        JButton selectBaseTerrain = new JButton("Select");
        selectBaseTerrain.addActionListener(new SelectTileListener(baseTerrain, frame));
        baseAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    baseGraphics = importGraphics(baseTerrain.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Cannot load base terrain graphics file.", "Error", JOptionPane.ERROR_MESSAGE);
                    baseGraphics = null;
                }
                basePreview.setIcon(new ImageIcon(baseGraphics));
                viewPanel.repaint();
            }
        };
        selectBaseTerrain.addActionListener(baseAction);
        // basePreview = new JLabel("base", JLabel.CENTER);
        basePreview = new JLabel();
        basePreview.setBorder(BorderFactory.createLineBorder(Color.black));

        innerTile = new JTextField();
        JButton selectInnerTile = new JButton("Select");
        selectInnerTile.addActionListener(new SelectTileListener(innerTile, frame));
        innerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    innerGraphics = importGraphics(innerTile.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Cannot load inner tile graphics file.", "Error", JOptionPane.ERROR_MESSAGE);
                    innerGraphics = null;
                }
                innerPreview.setIcon(new ImageIcon(innerGraphics));
                viewPanel.repaint();
            }
        };
        selectInnerTile.addActionListener(innerAction);

        // innerPreview = new JLabel("inner", JLabel.CENTER);
        innerPreview = new JLabel();
        innerPreview.setBorder(BorderFactory.createLineBorder(Color.black));

        outerTile = new JTextField();
        JButton selectOuterTile = new JButton("Select");
        selectOuterTile.addActionListener(new SelectTileListener(outerTile, frame));
        outerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    outerGraphics = importGraphics(outerTile.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Cannot load outer tile graphics file.", "Error", JOptionPane.ERROR_MESSAGE);
                    outerGraphics = null;
                }
                outerPreview.setIcon(new ImageIcon(outerGraphics));
                viewPanel.repaint();
            }
        };
        selectOuterTile.addActionListener(outerAction);

        // outerPreview = new JLabel("outer", JLabel.CENTER);
        outerPreview = new JLabel();
        outerPreview.setBorder(BorderFactory.createLineBorder(Color.black));

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

        panel.add(basePreview, String.format("spanx 3, split 3, alignx leading, w %d!, h %d!", TILE_SIZE, TILE_SIZE));
        panel.add(innerPreview, String.format("w %d!, h %d!", TILE_SIZE, TILE_SIZE));
        panel.add(outerPreview, String.format("w %d!, h %d!", TILE_SIZE, TILE_SIZE));

        return panel;
    }

    /**
     *
     * @return
     */
    private JComponent makeViewPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("View Tiles"));

        viewPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                // only if everything is successfull
                if (baseGraphics == null) {
                    return;
                }

                Graphics2D g2d = (Graphics2D) g;

                for (int i = 0; i < NUMBER_TILES; i++) {
                    for (int j = 0; j < NUMBER_TILES; j++) {
                        int x = i * TILE_SIZE + (j % 2 == 1 ? TILE_SIZE / 2 : 0);
                        int y = j * TILE_SIZE;
                        if (pattern[i][j] == false) {
                            g2d.drawImage(baseGraphics, x, y, null);
                        } else {
                        }
                    }
                }

            }
        };
        Dimension size = new Dimension(NUMBER_TILES * TILE_SIZE + TILE_SIZE / 2 + 1, NUMBER_TILES * TILE_SIZE + 1);
        viewPanel.setPreferredSize(size);
        viewPanel.setBackground(Color.gray);

        panel.setLayout(new MigLayout("fill"));
        panel.add(viewPanel, "south, w pref!, h pref!");

        return panel;
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
