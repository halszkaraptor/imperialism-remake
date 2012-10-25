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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import nu.xom.Element;
import nu.xom.ParsingException;
import org.iremake.client.resources.Places;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MapModel;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.common.GeographicalMap;
import org.tools.xml.XMLHelper;

/**
 *
 */
public class EditorDialogBuilder {

    private static JDialog dialog;
    private static MapModel model;
    private static GeographicalMap map;
    private static MainMapPanel mainMapPanel;
    private static MiniMapPanel miniMapPanel;
    
    private static EditorManager manager;

    private EditorDialogBuilder() {
    }

    public static void makeDialog(JFrame owner, String title, Rectangle bounds) {
        manager = new EditorManager();
        
        dialog = CommonElementsFactory.makeDialog(owner, title, false, bounds);

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
        
        mainMapPanel.initWithSize(miniMapPanel);
    }

    private static JToolBar menuBar() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isFile() && f.getName().endsWith(".xml");
            }

            @Override
            public String getDescription() {
                return "Map files (*.xml)";
            }
        });        
        
        
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // exit button
        JButton loadButton = CommonElementsFactory.makeButton(Places.UI, "scenario.button.load.png");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
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
                    map.fromXML(xml);
                }
            }
        });

        // exit button
        JButton saveButton = CommonElementsFactory.makeButton(Places.UI, "scenario.button.save.png");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    String name = f.getAbsolutePath();
                    if (!name.endsWith(".xml")) {
                        f = new File(name + ".xml");
                    }
                    Element xml = map.toXML();
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
        
        // add buttons to toolbar
        bar.add(loadButton);
        bar.add(saveButton);

        return bar;
    }

    private static JPanel createMapPanel() {
        JPanel panel = new JPanel();

        model = new MapModel();

        map = new GeographicalMap();
        map.addMapChangedListener(model);
        map.setEmptyMap(60, 100); // shift this backwards

        miniMapPanel = new MiniMapPanel(model);

        JToolBar menuBar = EditorDialogBuilder.makeMapMenuBar();

        EditorMapInfoPanel infoPanel = new EditorMapInfoPanel();

        mainMapPanel = new MainMapPanel(model);

        // wire them
        mainMapPanel.setTileListener(manager);
        miniMapPanel.setFocusChangedListener(mainMapPanel);

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
