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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import org.iremake.client.ui.CommonElementsFactory;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.model.ScenarioUIModel;
import org.iremake.common.model.Nation;
import org.iremake.common.model.Province;
import org.iremake.common.model.Scenario;
import org.iremake.common.resources.Places;
import org.tools.xml.common.XList;

/**
 * Builds the editor as we know it for now.
 */
// TODO unmake static
public class EditorBuilder {

    /**
     * No instantiation.
     */
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

        // new scenario button
        JButton newButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "scenario.button.new.png");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO create dialog, transfer the manager and start it or simply call (empty map or so)
            }
        });
        menuBar.add(newButton);

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
        JPanel nationPanel = EditorBuilder.buildNationPanel(dialog, manager);
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
     * @param parent
     * @param manager
     * @return
     */
    private static JPanel buildNationPanel(final Dialog parent, final EditorManager manager) {
        JPanel panel = new JPanel();

        JPanel nations = new JPanel();
        nations.setBorder(new LineBorder(Color.black, 1));
        nations.setPreferredSize(new Dimension(200, 400));

        // InfoLabel
        JLabel nationsInfoLabel = new JLabel("X Nations - Y Tiles without Nation");
        nations.add(nationsInfoLabel);

        // create menu bar and add to panel
        JToolBar nationsBar = CommonElementsFactory.makeToolBar();
        nations.add(nationsBar);
        // TODO load images
        JButton addnationButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "generic.button.add.png");
        nationsBar.add(addnationButton);
        JButton removenationButton = new JButton("Remove");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        nationsBar.add(removenationButton);
        JButton changenationButton = new JButton("Change");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        nationsBar.add(changenationButton);

        // list
        final JList<Nation> nationList = new JList<>();
        nationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nationList.setModel(new XList<>(Nation.class));

        // set button actions
        addnationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(parent, "Enter new Nation's name:");
                if (name != null) {
                    // need new valid id
                    Nation nation = new Nation(0, name, "");
                    // tell the manager
                    // TODO implement
                }
            }
        });
        removenationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = nationList.getSelectedIndex();
                if (row != -1) {
                    // TODO implement
                }
            }
        });
        changenationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = nationList.getSelectedIndex();
                if (row != -1) {
                    // TODO implement
                }
            }
        });


        // ScrollPane
        JScrollPane nationScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nationScrollPane.setViewportView(nationList);
        nations.add(nationScrollPane);

        // layout of nations panel
        GroupLayout layout = new GroupLayout(nations);
        nations.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(nationsInfoLabel).addComponent(nationsBar).addComponent(nationScrollPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(nationsInfoLabel).addComponent(nationsBar).addComponent(nationScrollPane));


        JPanel provinces = new JPanel();
        provinces.setBorder(new LineBorder(Color.black, 1));
        provinces.setPreferredSize(new Dimension(200, 400));

        // InfoLabel
        JLabel provinceInfoLabel = new JLabel("X Provinces");
        provinces.add(provinceInfoLabel);

        // create menu bar and add to panel
        JToolBar provinceBar = CommonElementsFactory.makeToolBar();
        provinces.add(provinceBar);
        // TODO load images
        JButton addprovinceButton = new JButton("Add");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        provinceBar.add(addprovinceButton);
        JButton removeprovinceButton = new JButton("Remove");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        provinceBar.add(removeprovinceButton);
        JButton changeprovinceButton = new JButton("Change");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        provinceBar.add(changeprovinceButton);

        // list
        final JList<Province> provinceList = new JList<>();
        provinceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        provinceList.setModel(new XList<>(Province.class));


        // set button actions
        addprovinceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(parent, "Enter new Province's name:");
                if (name != null) {
                    // need new valid id

                    // tell the manager
                    // TODO implement
                }
            }
        });
        removeprovinceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = provinceList.getSelectedIndex();
                if (row != -1) {
                    // TODO implement
                }
            }
        });
        changeprovinceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = provinceList.getSelectedIndex();
                if (row != -1) {
                    // TODO implement
                }
            }
        });

        // ScrollPane
        JScrollPane provinceScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        provinceScrollPane.setViewportView(provinceList);
        provinces.add(provinceScrollPane);

        // layout of province panel
        layout = new GroupLayout(provinces);
        provinces.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(provinceInfoLabel).addComponent(provinceBar).addComponent(provinceScrollPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(provinceInfoLabel).addComponent(provinceBar).addComponent(provinceScrollPane));

        // set layout
        layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(nations, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(provinces, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup().addComponent(nations, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(provinces, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));

        return panel;
    }

    /**
     *
     * @param manager
     * @return
     */
    private static JPanel buildMapPanel(final Dialog parent, final EditorManager manager) {
        JPanel panel = new JPanel();

        ScenarioUIModel model = new ScenarioUIModel();
        Scenario map = new Scenario();
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
