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
package org.iremake.client.ui.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.common.model.Scenario;
import org.iremake.common.model.map.MapPosition;

/**
 * The client scenario extending the model scenario.
 */
public class UIScenario extends Scenario {

    private static final Logger LOG = Logger.getLogger(UIScenario.class.getName());
    private TileGraphicsRepository repository = new TileGraphicsRepository();

    /**
     * Load terrain and resource files.
     */
    public UIScenario() {
        IOManager.setFromXML(Places.GraphicsScenario, "content.xml", repository);
    }

    /**
     * Returns the tile size.
     *
     * @return
     */
    public Dimension getTileSize() {
        return repository.getTerrainTileSize();
    }

    /**
     * Returns the resource at a specific position.
     *
     * @param p the tile position
     * @return the image or null if position is outside of the map
     */
    public Image getResourceOverlayAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return repository.getResourceOverlay(getTileAt(p).resourceID);
    }

    /**
     * Returns the terrain image at a specific position.
     *
     * @param p the tile position
     * @return the image or null if position is outside of the map
     */
    public Image getTerrainTileAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return repository.getTerrainTile(getTerrainAt(p));
    }

    /**
     * Returns the terrain color at a specific position.
     *
     * @param p the tile position
     * @return the image of null if position is outside of the map
     */
    public Color getTerrainTileColorAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return repository.getTerrainTileColor(getTerrainAt(p));
    }

    /**
     * This gives away an internal variable which is bad style, but it was the
     * shortest way for now. Should be changed.
     *
     * @return
     */
    public TileGraphicsRepository getTileGraphicsRepository() {
        return repository;
    }

    /**
     *
     * @param p
     * @return
     */
    public Image getRiverOverlayAt(MapPosition p) {
        int id = getRiverIDAt(p);
        if (id == Scenario.RIVERID_NONE) {
            return null;
        }
        return repository.getRiverOverlay(id);
    }
}
