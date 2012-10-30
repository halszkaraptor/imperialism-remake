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
package org.iremake.client.ui.editor;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import org.iremake.client.ui.CommonElementsFactory;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.map.ScenarioModel;
import org.iremake.common.GeographicalMap;
import org.iremake.common.resources.Places;

/**
 *
 */
public class EditorBuilder {

    private EditorBuilder() {
    }

    /**
     * 
     * @param owner
     * @param title
     * @param bounds 
     */
    public static void build(JFrame owner, String title, Rectangle bounds) {
        final EditorManager manager = new EditorManager();

        JDialog dialog = CommonElementsFactory.makeDialog(owner, title, false, bounds);

        manager.setFrame(dialog);

        // create menu bar and add to frame
        JToolBar menuBar = CommonElementsFactory.makeToolBar();
        dialog.add(menuBar);

        // load scenario button
        JButton loadButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "scenario.button.load.png");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.loadScenarioDialog();
            }
        });
        menuBar.add(loadButton);        
        // save scenario button
        JButton saveButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "scenario.button.save.png");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.saveScenarioDialog();
            }
        });
        menuBar.add(saveButton);
        
        // create tabbed pane and add to dialog
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        dialog.add(tabPane);
        
        // create and add map panel
        JPanel mapPanel = EditorBuilder.buildMapPanel(dialog, manager);
        tabPane.addTab("Map", mapPanel);
        
        // create and add nations panel
        JPanel nationPanel = new JPanel();
        tabPane.addTab("Nation", nationPanel);


        // set layout (vertically first menubar, then tabbed pane)
        Container c = dialog.getContentPane();
        GroupLayout layout = new GroupLayout(c);
        c.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(menuBar).addComponent(tabPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(menuBar).addComponent(tabPane));

        // we make it visible immediately
        dialog.setVisible(true);

        // and load a basic scenario
        manager.loadInitialScenario();
    }

    /**
     * 
     * @param manager
     * @return 
     */
    private static JPanel buildMapPanel(final Dialog parent, final EditorManager manager) {
        JPanel panel = new JPanel();

        ScenarioModel model = new ScenarioModel();
        GeographicalMap map = new GeographicalMap();
        manager.setScenarioContent(map, model);

        // create mini map and add to panel
        MiniMapPanel miniMapPanel = new MiniMapPanel(model);
        panel.add(miniMapPanel);
        
        // create menu bar and add to panel
        JToolBar menuBar = CommonElementsFactory.makeToolBar();
        panel.add(menuBar);
        
        // terrain button
        JButton terrainButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        terrainButton.setToolTipText("Modify terrain of current tile.");
        terrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new EditorSelectionTerrainDialog(parent, manager);
                dialog.setVisible(true);
            }
        });
        menuBar.add(terrainButton);
        // nation button
        JButton nationButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.nation.png");
        menuBar.add(nationButton);
        // province button
        JButton provinceButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.province.png");
        menuBar.add(provinceButton);

        // info map panel and add
        EditorMapInfoPanel infoPanel = new EditorMapInfoPanel(map);
        panel.add(infoPanel);
        
        // create main map panel and add
        MainMapPanel mainMapPanel = new MainMapPanel(model);
        panel.add(mainMapPanel);
        
        // add them to manager
        manager.setMapTab(mainMapPanel, miniMapPanel, infoPanel);

        // set layout
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup()
                .addComponent(miniMapPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(menuBar, 200, 200, 200).addComponent(infoPanel, 200, 200, 200))
                .addComponent(mainMapPanel));
        layout.setVerticalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup()
                .addComponent(miniMapPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(menuBar, 100, 100, 100).addComponent(infoPanel))
                .addComponent(mainMapPanel));

        return panel;
    }
}
