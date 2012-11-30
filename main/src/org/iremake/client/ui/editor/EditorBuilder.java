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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
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
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.Button;
import org.iremake.client.ui.FrameManager;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.model.ScenarioUIModel;
import org.iremake.common.model.Nation;
import org.iremake.common.model.Province;
import org.iremake.common.model.Scenario;
import org.tools.ui.utils.GraphicsUtils;
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
     */
    public static JComponent build() {

        JPanel panel = new JPanel();

        // create menu bar and add to frame
        JToolBar menuBar = GraphicsUtils.makeToolBar(false, false);

        // new scenario button
        JButton newButton = Button.ScenarioNew.create();
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO create dialog, transfer the manager and start it or simply call (empty map or so)
            }
        });

        // load scenario button
        JButton loadButton = Button.ScenarioLoad.create();
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorManager.getInstance().loadScenarioDialog();
            }
        });

        // save scenario button
        JButton saveButton = Button.ScenarioSave.create();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorManager.getInstance().saveScenarioDialog();
            }
        });

        // TODO hardcode these buttons somewhere?
        JButton exitButton = Button.NormalExit.create();
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameManager.getInstance().switchToStartScreen();
            }
        });

        menuBar.add(newButton);
        menuBar.add(loadButton);
        menuBar.add(saveButton);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(exitButton);

        // create tabbed pane and add to dialog
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // create and add map panel
        JPanel mapPanel = EditorBuilder.buildMapPanel();

        // create and add nations panel
        JPanel nationPanel = EditorBuilder.buildNationPanel();

        tabPane.addTab("General", nationPanel);
        tabPane.addTab("Map", mapPanel);
        tabPane.setSelectedComponent(mapPanel);


        // set layout (vertically first menubar, then tabbed pane)
        panel.setLayout(new MigLayout("wrap 1, fill", "[fill, grow]", "[][fill,grow]"));
        panel.add(menuBar);
        panel.add(tabPane);

        return panel;
    }

    /**
     *
     * @param parent
     * @param manager
     * @return
     */
    private static JPanel buildNationPanel() {
        JPanel panel = new JPanel();

        JPanel nations = new JPanel();
        nations.setBorder(new LineBorder(Color.black, 1));
        nations.setPreferredSize(new Dimension(200, 400));

        // InfoLabel
        JLabel nationsInfoLabel = new JLabel("X Nations - Y Tiles without Nation");

        // create menu bar and add to panel
        JToolBar nationsBar = GraphicsUtils.makeToolBar(false, false);
        // TODO load images
        JButton addnationButton = Button.SmallAdd.create();
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
                String name = JOptionPane.showInputDialog(FrameManager.getInstance().getFrame(), "Enter new Nation's name:");
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


        // layout of nations panel
        nations.setLayout(new MigLayout("wrap 1, fill", "", "[][][grow]"));
        nations.add(nationsInfoLabel);
        nations.add(nationsBar);
        nations.add(nationScrollPane, "grow");


        // provinces panel

        JPanel provinces = new JPanel();
        provinces.setBorder(new LineBorder(Color.black, 1));
        provinces.setPreferredSize(new Dimension(200, 400));

        // InfoLabel
        JLabel provinceInfoLabel = new JLabel("X Provinces");

        // create menu bar and add to panel
        JToolBar provinceBar = GraphicsUtils.makeToolBar(false, false);
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
                String name = JOptionPane.showInputDialog(FrameManager.getInstance().getFrame(), "Enter new Province's name:");
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

        // layout of province panel
        provinces.setLayout(new MigLayout("wrap 1, fill", "", "[][][grow]"));
        provinces.add(provinceInfoLabel);
        provinces.add(provinceBar);
        provinces.add(provinceScrollPane, "grow");

        // set layout
        panel.setLayout(new MigLayout());
        panel.add(nations);
        panel.add(provinces);

        return panel;
    }

    /**
     *
     * @param manager
     * @return
     */
    private static JPanel buildMapPanel() {
        JPanel panel = new JPanel();

        ScenarioUIModel model = new ScenarioUIModel();
        Scenario map = new Scenario();
        EditorManager.getInstance().setScenarioContent(map, model);

        // create mini map and add to panel
        MiniMapPanel miniMapPanel = new MiniMapPanel(model);

        // create menu bar and add to panel
        JToolBar menuBar = GraphicsUtils.makeToolBar(false, false);

        // terrain button
        final JButton terrainButton = Button.EditorTerrain.create();
        terrainButton.setToolTipText("Modify terrain of current tile.");
        terrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel content = new EditorSelectionTerrainPanel();

                Point p = terrainButton.getLocationOnScreen();
                p.x += 50;

                FrameManager.getInstance().startDialog(content, "Terrain Selection", new Dimension(0, 0), p);
                // TODO automatically put next to button the location
            }
        });
        menuBar.add(terrainButton);
        // nation button
        JButton nationButton = Button.EditorNation.create();
        menuBar.add(nationButton);
        // province button
        JButton provinceButton = Button.EditorProvince.create();
        menuBar.add(provinceButton);

        // info map panel and add
        EditorMapInfoPanel infoPanel = new EditorMapInfoPanel(map);

        // create main map panel and add
        MainMapPanel mainMapPanel = new MainMapPanel(model);

        // add them to manager
        EditorManager.getInstance().setMapTab(mainMapPanel, miniMapPanel, infoPanel);

        // set layout
        panel.setLayout(new MigLayout("wrap 2", "[][grow]", "[][][grow]"));

        panel.add(miniMapPanel, "width 200:300:, hmin 100");
        panel.add(mainMapPanel, "span 1 3, grow");
        panel.add(menuBar);
        panel.add(infoPanel, "grow");

        return panel;
    }
}
