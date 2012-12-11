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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;
import nu.xom.Element;
import nu.xom.ParsingException;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.iremake.client.resources.TerrainLoader;
import org.iremake.client.ui.Button;
import org.iremake.client.ui.FrameManager;
import org.iremake.client.ui.StartScreen;
import org.iremake.client.ui.UIDialog;
import org.iremake.client.ui.UIFrame;
import org.iremake.client.ui.WindowClosingListener;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MapTileListener;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.model.ScenarioUIView;
import org.iremake.common.model.MapPosition;
import org.iremake.common.model.Nation;
import org.iremake.common.model.Province;
import org.iremake.common.model.Scenario;
import org.iremake.common.model.ScenarioChangedListener;
import org.tools.ui.ButtonBar;
import org.tools.xml.XList;
import org.tools.xml.XMLHelper;

/**
 *
 */
public class EditorScreen extends UIFrame {

    private Scenario scenario = new Scenario();
    private ScenarioUIView uiscenario = new ScenarioUIView(scenario);
    private String selectedTerrain;
    private JList<Nation> nationsList;
    private MainMapPanel mainMapPanel;
    private MiniMapPanel miniMapPanel;
    private EditorMapInfoPanel infoPanel;

    public EditorScreen() {
        JPanel content = new JPanel();

        // create tabbed pane and add to dialog
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPane.addTab("General", createGeneralTab());
        tabPane.addTab("Map", createMapTab());
        tabPane.setSelectedIndex(1); // map selected initially

        // set layout (vertically first menubar, then tabbed pane)
        content.setLayout(new MigLayout("fill", "[]", "[][grow]"));
        content.add(createMenuBar(), "wrap");
        content.add(tabPane, "grow");

        scenario.addMapChangedListener(new ScenarioChangedListener() {
            @Override
            public void tileChanged(MapPosition p, String id) {
                miniMapPanel.tileChanged();
                mainMapPanel.tileChanged(p);
                infoPanel.mainMapTileChanged(p);
            }
            @Override
            public void scenarioChanged(Scenario scenario) {
                // TODO size of map could also have changed!!!
                mainMapPanel.mapChanged();

                Dimension size = mainMapPanel.getSize();
                Dimension tileSize = TerrainLoader.getTileSize();
                // tell the minimap about our size
                float fractionRows = (float) size.height / tileSize.height / scenario.getNumberRows();
                float fractionColumns = (float) size.width / tileSize.width / scenario.getNumberColumns();
                miniMapPanel.mapChanged(fractionRows, fractionColumns);
            }
        });
        setContent(content);
    }

    /**
     *
     * @return
     */
    private JComponent createMenuBar() {
        // new scenario button
        final JButton newButton = Button.ScenarioNew.create();
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point p = newButton.getLocationOnScreen();
                p.x += 50;
                UIDialog dialog = new EditorNewScenarioDialog();
                dialog.setLocation(p);
                dialog.start();
            }
        });

        // load scenario button
        JButton loadButton = Button.ScenarioLoad.create();
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = IOManager.getFileChooser();
                if (FrameManager.getInstance().showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    // read file and parse to xml
                    Element xml;
                    try (InputStream is = new FileInputStream(f)) {
                        xml = XMLHelper.read(is);
                    } catch (ParsingException | IOException ex) {
                        // LOG.log(Level.SEVERE, null, ex);
                        // NotificationFactory.createInfoPane(dialog, "Loading failed.");
                        // TODO also make it working with dialogs
                        return;
                    }
                    scenario.fromXML(xml);
                }
            }
        });

        // save scenario button
        JButton saveButton = Button.ScenarioSave.create();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = IOManager.getFileChooser();
                if (FrameManager.getInstance().showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    String name = f.getAbsolutePath();
                    if (!name.endsWith(".xml")) {
                        f = new File(name + ".xml");
                    }
                    Element xml = scenario.toXML();
                    OutputStream os;
                    try {
                        os = new FileOutputStream(f);
                        XMLHelper.write(os, xml);
                    } catch (IOException ex) {
                        //LOG.log(Level.SEVERE, null, ex);
                        //NotificationFactory.createInfoPane(TableEditorFrame.this, "Saving failed.");
                        return;
                    }
                    //NotificationFactory.createInfoPane(TableEditorFrame.this, "Table saved.");
                }
            }
        });

        // TODO hardcode these buttons somewhere?
        JButton exitButton = Button.NormalExit.create();
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIFrame frame = new StartScreen();
                frame.switchTo();
            }
        });

        ButtonBar bar = new ButtonBar();
        bar.add(newButton, loadButton, saveButton, exitButton);
        return bar.get();
    }

    /**
     *
     * @param parent
     * @param manager
     * @return
     */
    private JPanel createGeneralTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(createNationsPanel(), "sizegroup");
        panel.add(createProvincesPanel(), "sizegroup");

        return panel;
    }

    /**
     *
     * @return
     */
    private JComponent createNationsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Nations"));

        // InfoLabel
        JLabel nationsInfoLabel = new JLabel("X Nations - Y Tiles without Nation");

        // create menu bar and add to panel

        // TODO load images
        JButton addnationButton = Button.SmallAdd.create();

        JButton removenationButton = new JButton("Remove");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        JButton changenationButton = new JButton("Change");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");

        ButtonBar nbar = new ButtonBar();
        nbar.add(addnationButton, removenationButton, changenationButton);

        // list
        nationsList = new JList<>();
        nationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nationsList.setModel(scenario.getNations()); // TODO update model when scenario changes

        // set button actions
        addnationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = FrameManager.getInstance().showInputDialog("Enter new Nation's name:");
                if (name != null) {
                    // TODO test if already existing
                    scenario.getNations().addElement(new Nation(name));
                }
            }
        });
        removenationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = nationsList.getSelectedIndex();
                if (row != -1) {
                    // TODO are you sure?
                    scenario.getNations().removeElementAt(row);
                }
            }
        });
        changenationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = nationsList.getSelectedIndex();
                if (row != -1) {
                    // TODO implement
                }
            }
        });

        // scrollpane
        JScrollPane nationScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nationScrollPane.setViewportView(nationsList);

        // layout of nations panel
        panel.setLayout(new MigLayout("wrap 1, fill", "", "[][][grow]"));
        panel.add(nationsInfoLabel);
        panel.add(nbar.get());
        panel.add(nationScrollPane, "height 400!, width 300!");

        return panel;
    }

    private JComponent createProvincesPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Provinces"));

        // InfoLabel
        JLabel provinceInfoLabel = new JLabel("X Provinces");

        // TODO load images
        JButton addprovinceButton = new JButton("Add");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        JButton removeprovinceButton = new JButton("Remove");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");
        JButton changeprovinceButton = new JButton("Change");// CommonElementsFactory.makeButton(Places.GraphicsIcons, "editor.button.terrain.png");

        ButtonBar pbar = new ButtonBar();
        pbar.add(addprovinceButton, removeprovinceButton, changeprovinceButton);

        // list
        final JList<Province> provinceList = new JList<>();
        provinceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        provinceList.setModel(new XList<>(Province.class));


        // set button actions
        addprovinceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = FrameManager.getInstance().showInputDialog("Enter new Province's name:");
                if (name != null) {
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
        panel.setLayout(new MigLayout("wrap 1, fill", "", "[][][grow]"));
        panel.add(provinceInfoLabel);
        panel.add(pbar.get());
        panel.add(provinceScrollPane, "height 400!, width 300!");

        return panel;
    }

    /**
     *
     * @param manager
     * @return
     */
    private JPanel createMapTab() {
        JPanel panel = new JPanel();

        // create mini map and add to panel
        miniMapPanel = new MiniMapPanel(uiscenario);

        // terrain button
        final JButton terrainButton = Button.EditorTerrain.create();
        terrainButton.setToolTipText("Modify terrain of current tile.");
        terrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point p = terrainButton.getLocationOnScreen();
                p.x += 50;
                final EditorSelectTerrainDialog dialog = new EditorSelectTerrainDialog();
                dialog.setClosingListener(new WindowClosingListener() {
                    @Override
                    public boolean closing() {
                        selectedTerrain = dialog.getSelection();
                        return true;
                    }
                });
                dialog.setLocation(p);
                dialog.start();
            }
        });
        // province button
        JButton provinceButton = Button.EditorProvince.create();

        ButtonBar bar = new ButtonBar();
        bar.add(terrainButton, provinceButton);

        // info map panel and add
        infoPanel = new EditorMapInfoPanel(scenario);

        // create main map panel and add
        mainMapPanel = new MainMapPanel(uiscenario);
        mainMapPanel.addTileListener(new MapTileListener() {
            @Override
            public void focusChanged(MapPosition p) {
                infoPanel.mainMapTileChanged(p);
            }

            @Override
            public void tileClicked(MapPosition p) {
                if (selectedTerrain != null) {
                    scenario.setTerrainAt(p, selectedTerrain);
                }
            }
        });
        miniMapPanel.setFocusChangedListener(mainMapPanel);

        // set layout
        panel.setLayout(new MigLayout("wrap 2", "[][grow]", "[][][grow]"));
        panel.add(miniMapPanel, "width 200:300:, hmin 100");
        panel.add(mainMapPanel, "span 1 3, grow");
        panel.add(bar.get());
        panel.add(infoPanel, "grow");

        return panel;
    }

    @Override
    public void switchTo() {
        super.switchTo();
        // load initial scenario
        IOManager.setFromXML(Places.Scenarios, "scenario.Europe1814.xml", scenario);
    }
}