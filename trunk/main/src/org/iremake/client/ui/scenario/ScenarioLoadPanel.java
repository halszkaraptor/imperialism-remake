/*
 * Copyright (C) 2012 Trilarion
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
package org.iremake.client.ui.scenario;

import java.awt.Color;
import java.awt.Container;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.iremake.client.ui.Factory;
import org.iremake.client.utils.Resources;

/**
 *
 * @author Trilarion
 */
public class ScenarioLoadPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public ScenarioLoadPanel() {
        initComponents();
    }

    private void initComponents() {
        JLabel selectPanel = new JLabel("Select");
        JLabel mapPanel = new JLabel("Map");
        JLabel infoPanel = new JLabel("Info");
        
        // toolbar
        JToolBar menuBar = new JToolBar();
        menuBar.setFloatable(false);  // non floatable
        menuBar.setOpaque(false);     // transparent

        // load button
        JButton loadButton = Factory.makeButton("");
        
        // start button
        JButton startButton = Factory.makeButton("");
        
        // add buttons to tool bar
        menuBar.add(loadButton);
        menuBar.add(startButton);
        
        // set background
        setBackground(Color.WHITE);    // white color        
        
        // layout
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);        
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(menuBar)
                .addGroup(layout.createSequentialGroup().addComponent(selectPanel)
                .addComponent(mapPanel)).addComponent(infoPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(menuBar)
                .addGroup(layout.createParallelGroup().addComponent(selectPanel).addComponent(mapPanel))
                .addComponent(infoPanel));
    }
}
