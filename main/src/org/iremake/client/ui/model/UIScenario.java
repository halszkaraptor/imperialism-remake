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
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.iremake.common.model.MapPosition;
import org.iremake.common.model.Scenario;

/**
 *
 */
public class UIScenario extends Scenario {

    private static final Logger LOG = Logger.getLogger(UIScenario.class.getName());
    private TerrainTiles terrainTiles = new TerrainTiles();
    private ResourceOverlays resourceOverlays = new ResourceOverlays();

    public UIScenario() {
        IOManager.setFromXML(Places.GraphicsTerrain, "terrains.xml", terrainTiles);
        IOManager.setFromXML(Places.GraphicsTerrain, "resources.xml", resourceOverlays);
    }

    /**
     * 
     * @return
     */
    public Dimension getTileSize() {
        return terrainTiles.getTileSize();
    }

    /**
     *
     * @param p
     * @return
     */
    public Image getResourceOverlayAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return resourceOverlays.getImage(getTileAt(p).resourceID);
    }

    /**
     *
     * @param p
     * @return
     */
    public Image getTerrainTileAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return terrainTiles.getImage(getTerrainAt(p));
    }

    /**
     *
     * @param p
     * @return
     */
    public Color getTerrainTileColorAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return terrainTiles.getColor(getTerrainAt(p));
    }

    /**
     *
     * @return
     */
    public TerrainTiles getTerrainTiles() {
        // TODO later replace it to more restricted calls
        return terrainTiles;
    }
}
