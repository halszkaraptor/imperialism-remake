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

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import nu.xom.Element;
import nu.xom.ParsingException;
import org.iremake.client.resources.TerrainLoader;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MainMapTileListener;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.map.ScenarioChangedListener;
import org.iremake.client.ui.map.ScenarioModel;
import org.iremake.common.GeographicalMap;
import org.iremake.common.MapPosition;
import org.iremake.common.resources.Loader;
import org.iremake.common.resources.Places;
import org.tools.xml.XMLHelper;

/**
 * Editorframe manager.
 */
public class EditorManager implements MainMapTileListener, ScenarioChangedListener {

    private Component frame;
    private EditorMapInfoPanel mapInfoPanel;
    private MainMapPanel mainMapPanel;
    private MiniMapPanel miniMapPanel;
    private GeographicalMap map;
    private ScenarioModel model;
    private JFileChooser fileChooser;
    private String terrainSelectedID;

    public EditorManager() {
        fileChooser = new JFileChooser();
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
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    public void setMapTab(MainMapPanel mainPanel, MiniMapPanel miniPanel, EditorMapInfoPanel infoPanel) {
        mapInfoPanel = infoPanel;
        mainMapPanel = mainPanel;
        miniMapPanel = miniPanel;

        // wiring (main map tells manager, mini map tells main map)
        mainMapPanel.setTileListener(this);
        miniMapPanel.setFocusChangedListener(mainMapPanel);
    }

    public void setScenarioContent(GeographicalMap map, ScenarioModel model) {
        this.map = map;
        this.model = model;

        // wiring (map tells model, model tells manager)
        map.addMapChangedListener(model);
        model.setScenarioChangedListener(this);
    }

    @Override
    public void focusChanged(MapPosition p) {
        mapInfoPanel.mainMapTileChanged(p);
    }

    @Override
    public void tileClicked(MapPosition p) {
        map.setTerrainAt(p, terrainSelectedID);
    }

    @Override
    public void tileChanged(MapPosition p) {
        // although only a single tile has changed we redo the minimap completely
        miniMapPanel.tileChanged();

        mainMapPanel.tileChanged(p);

        mapInfoPanel.mainMapTileChanged(p);
    }

    @Override
    public void mapChanged() {
        // TODO size of map could also have changed!!!
        mainMapPanel.mapChanged();

        Dimension size = mainMapPanel.getSize();
        Dimension tileSize = TerrainLoader.getTileSize();
        // tell the minimap about our size
        float fractionRows = (float) size.height / tileSize.height / model.getNumberRows();
        float fractionColumns = (float) size.width / tileSize.width / model.getNumberColumns();
        miniMapPanel.mapChanged(fractionRows, fractionColumns);
    }

    public void loadInitialScenario() {
        //map.setEmptyMap(60, 100);
        loadScenario(Loader.getPath(Places.ScenarioMaps, "map.Europe1814.xml"));
    }

    private void loadScenario(String location) {
        Element xml = XMLHelper.read(location);
        map.fromXML(xml);
    }

    private void saveScenario(String location) {

    }

    public void loadScenarioDialog() {
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
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

    public void saveScenarioDialog() {
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
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

    void terrainSelected(String id) {
        terrainSelectedID = id;
    }
}
