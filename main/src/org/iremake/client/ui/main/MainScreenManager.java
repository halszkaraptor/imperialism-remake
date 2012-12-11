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
package org.iremake.client.ui.main;

import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.ParsingException;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.iremake.client.resources.TerrainLoader;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MapTileListener;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.model.ScenarioUIModel;
import org.iremake.client.ui.model.ScenarioUIModelChangedListener;
import org.iremake.common.model.MapPosition;
import org.iremake.common.model.Scenario;
import org.tools.io.Resource;
import org.tools.xml.XMLHelper;

/**
 * Manages all the wirings of the main screen maps.
 *
 */
// TODO generalize between EditorManager and this class (some code duplicates)
public class MainScreenManager implements MapTileListener, ScenarioUIModelChangedListener {

    private static final Logger LOG = Logger.getLogger(MainScreenManager.class.getName());

    private Scenario scenario;
    private MainMapPanel mainMapPanel;
    private MiniMapPanel miniMapPanel;
    private ScenarioUIModel model;

    private static MainScreenManager singleton;

    private MainScreenManager() {

    }

    /**
     *
     * @return
     */
    public static MainScreenManager getInstance() {
        if (singleton == null) {
            singleton = new MainScreenManager();
        }
        return singleton;
    }

    /**
     *
     * @param mainPanel
     */
    public void setPanels(MainMapPanel mainPanel) {
        mainMapPanel = mainPanel;
        // wiring (main map tells manager, mini map tells main map)
        mainMapPanel.addTileListener(this);
    }

    public void setMiniMap(MiniMapPanel miniMapPanel) {
        this.miniMapPanel = miniMapPanel;
        miniMapPanel.setFocusChangedListener(mainMapPanel); // TODO make sure mainMapPanel is set before, set them together or later
    }

    /**
     *
     * @param map
     * @param model
     */
    public void setScenarioContent(Scenario map, ScenarioUIModel model) {
        this.scenario = map;
        this.model = model;

        // wiring (map tells model, model tells manager)
        map.addMapChangedListener(model);
        model.setScenarioChangedListener(this);
    }

    /**
     *
     */
    public void loadInitialScenario() {
        // map.setEmptyMap(60, 100);
        loadScenario("scenario.Europe1814.xml");
    }

    /**
     *
     * @param location
     */
    private void loadScenario(String location) {
        IOManager.setFromXML(Places.Scenarios, location, scenario);
    }

    public void loadScenario(Resource location) {
        try {
            XMLHelper.read(location, scenario);
        } catch (IOException | ParsingException ex) {
            LOG.log(Level.SEVERE, null, ex);
            // TODO message
        }
    }

    /**
     *
     * @param p
     */
    @Override
    public void tileChanged(MapPosition p) {
        // TODO do we need this method? (adapter)

        miniMapPanel.tileChanged();

        mainMapPanel.tileChanged(p);
    }

    /**
     *
     */
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

    @Override
    public void focusChanged(MapPosition p) {

    }

    @Override
    public void tileClicked(MapPosition p) {

    }
}
