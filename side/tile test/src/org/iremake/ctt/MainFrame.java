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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.utils.LookAndFeel;

/**
 *
 */
public class MainFrame {

    public final static int SIZE = 10; // field is 10x10
    private JFrame frame;
    private PatternPanel pattern;
    private JTextField baseTerrain;
    private JTextField innerTile;
    private JTextField outerTile;
    private JTextField tileSize;
    private ViewPanel view;

    /**
     *
     */
    public MainFrame() {
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
        frame.add(makePatternPanel(), "w pref!, h pref!");
        frame.add(makeTileSelectionPanel(), "aligny top");
        frame.add(makeViewPanel(), "span 2, hmin 200, grow");

        frame.pack();
        frame.setVisible(true);

        // either load or set defaults
        initializeByDefaults();

    }

    /**
     *
     */
    private void initializeByDefaults() {
        tileSize.setText("80");
        pattern.newRandomPattern();
    }

    /**
     *
     * @return
     */
    private JComponent makePatternPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Select Pattern"));

        pattern = new PatternPanel();
        
        JButton clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pattern.clearPattern();
            }
        });
        
        JButton random = new JButton("Random");
        random.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pattern.newRandomPattern();
            }
        });

        panel.setLayout(new MigLayout("fill"));
        panel.add(clear);
        panel.add(random, "wrap");
        panel.add(pattern, "south, w pref!, h pref!");
        


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

        innerTile = new JTextField();
        JButton selectInnerTile = new JButton("Select");
        selectInnerTile.addActionListener(new SelectTileListener(innerTile, frame));

        outerTile = new JTextField();
        JButton selectOuterTile = new JButton("Select");
        selectOuterTile.addActionListener(new SelectTileListener(outerTile, frame));

        tileSize = new JTextField();

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

        panel.add(new JLabel("Tile size in view"));
        panel.add(tileSize, "align left, wmin 50");


        return panel;
    }

    /**
     *
     * @return
     */
    private JComponent makeViewPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("View Tiles"));

        view = new ViewPanel();

        JButton button = new JButton("Update");

        panel.setLayout(new MigLayout("fill"));
        panel.add(button);
        panel.add(view, "south, w pref!, h pref!");

        return panel;
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
                new MainFrame();
            }
        });
    }
}
