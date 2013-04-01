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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.utils.LookAndFeel;

/**
 *
 */
public class MainFrame {
    
    private JFrame frame;
    private int rows = 10;
    private int columns = 10;
    
    public MainFrame() {
        frame = new JFrame("Imperialism Continuous Tiles Test");
        frame.setResizable(true);
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);        
        
        frame.setLayout(new MigLayout("wrap 2, fill", "[grow][]", "[][grow]"));
        frame.add(makePatternPanel(), "wmin 200, hmin 200, grow");
        frame.add(makeTileSelectionPanel(), "aligny top");
        frame.add(makeViewPanel(), "span 2, hmin 200, grow");
        
        frame.pack();
        frame.setVisible(true);
    }
    
    private JComponent makePatternPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Select Pattern"));
        
        JTextField rowsTextField = new JTextField();
        rowsTextField.setText(String.valueOf(rows));
        JTextField columnsTextField = new JTextField();
        columnsTextField.setText(String.valueOf(columns));
        JButton random = new JButton("New pattern");
        
        JPanel pattern = new PatternPanel();
        
        panel.setLayout(new MigLayout("fill", "", "[][grow]"));
        panel.add(rowsTextField, "wmin 50");
        panel.add(columnsTextField, "wmin 50");
        panel.add(random, "wrap");
        panel.add(pattern, "grow");
                
        
        return panel;
    }
    
    private JComponent makeTileSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Tile Selection"));        
        
        JTextField baseTerrain = new JTextField();
        JButton selectBaseTerrain = new JButton("Select");
        selectBaseTerrain.addActionListener(new SelectTileListener(baseTerrain, frame));
        
        JTextField innerTile = new JTextField();
        JButton selectInnerTile = new JButton("Select");
        selectInnerTile.addActionListener(new SelectTileListener(innerTile, frame));
        
        JTextField outerTile = new JTextField();
        JButton selectOuterTile = new JButton("Select");        
        selectOuterTile.addActionListener(new SelectTileListener(outerTile, frame));
        
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
        
        
        return panel;
    }
    
    private JComponent makeViewPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("View Tiles"));        
        
        JToggleButton button = new JToggleButton("Start view");
        JButton help = new JButton("Help");
        JPanel view = new ViewPanel();
        
        panel.setLayout(new MigLayout("fill, wrap 2", "", "[][grow]"));
        panel.add(button);
        panel.add(help);
        panel.add(view, "grow");
        
        return panel;
    }    
    
    
    

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
