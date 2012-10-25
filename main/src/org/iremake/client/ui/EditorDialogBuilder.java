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
package org.iremake.client.ui;

import java.awt.Container;
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
import org.iremake.client.resources.Places;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.map.ScenarioModel;
import org.iremake.common.GeographicalMap;

/**
 *
 */
public class EditorDialogBuilder {

    private static EditorManager manager;

    private EditorDialogBuilder() {
    }

    public static void makeDialog(JFrame owner, String title, Rectangle bounds) {
        manager = new EditorManager();

        JDialog dialog = CommonElementsFactory.makeDialog(owner, title, false, bounds);

        manager.setFrame(dialog);
        // create menu bar and add to frame
        JToolBar menuBar = EditorDialogBuilder.menuBar();
        dialog.add(menuBar);

        // tabbed pane
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // placeholder for the real things
        JPanel mapPanel = EditorDialogBuilder.createMapPanel();
        JPanel nationPanel = new JPanel();

        // add panels to tabbed pane
        tabPane.addTab("Map", mapPanel);
        tabPane.addTab("Nation", nationPanel);

        // add to frame
        dialog.add(tabPane);

        // layout (vertically first menubar, then tabbed pane)
        Container c = dialog.getContentPane();
        GroupLayout layout = new GroupLayout(c);
        c.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(menuBar).addComponent(tabPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(menuBar).addComponent(tabPane));

        dialog.setVisible(true);

        manager.init();
    }

    private static JToolBar menuBar() {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // exit button
        JButton loadButton = CommonElementsFactory.makeButton(Places.UI, "scenario.button.load.png");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.load();
            }
        });

        // exit button
        JButton saveButton = CommonElementsFactory.makeButton(Places.UI, "scenario.button.save.png");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.save();
            }
        });

        // add buttons to toolbar
        bar.add(loadButton);
        bar.add(saveButton);

        return bar;
    }

    private static JPanel createMapPanel() {
        JPanel panel = new JPanel();

        ScenarioModel model = new ScenarioModel();


        GeographicalMap map = new GeographicalMap();

        manager.setScenarioContent(map, model);

        MiniMapPanel miniMapPanel = new MiniMapPanel(model);

        JToolBar menuBar = EditorDialogBuilder.makeMapMenuBar();

        EditorMapInfoPanel infoPanel = new EditorMapInfoPanel();

        MainMapPanel mainMapPanel = new MainMapPanel(model);

        // manager (wires everything
        manager.setMapTab(mainMapPanel, miniMapPanel, infoPanel);

        // add all
        panel.add(miniMapPanel);
        panel.add(menuBar);
        panel.add(infoPanel);
        panel.add(mainMapPanel);

        // layout
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

    private static JToolBar makeMapMenuBar() {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // terrain button
        JButton terrainButton = CommonElementsFactory.makeButton(Places.UI, "editor.button.terrain.png");
        terrainButton.setToolTipText("Modify terrain of current tile.");
        terrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rectangle bounds = new Rectangle(100, 100, 200, 200);
                JDialog terrainDialog = CommonElementsFactory.makeDialog(null, "Terrain Selector", true, bounds);
                terrainDialog.setVisible(true);
            }
        });

        // nation button
        JButton nationButton = CommonElementsFactory.makeButton(Places.UI, "editor.button.nation.png");

        // province button
        JButton provinceButton = CommonElementsFactory.makeButton(Places.UI, "editor.button.province.png");

        // add buttons to toolbar
        bar.add(terrainButton);
        bar.add(nationButton);
        bar.add(provinceButton);

        return bar;
    }
}