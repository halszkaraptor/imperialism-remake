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
package org.iremake.client.resources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;
import org.iremake.client.ui.model.TerrainTile;
import org.iremake.common.xml.common.XTable;

/**
 * Loads the terrain tiles from the artwork folder and delivers if needed.
 *
 * Determines the tile size.
 */
// TODO unmake static
public class TerrainLoader {

    private static final Logger LOG = Logger.getLogger(TerrainLoader.class.getName());
    private static Dimension tileSize;
    /**
     *      */
    private static Map<String, TerrainTile> map = new HashMap<>(0);

    /**
     * Static class, prevent instantiation.
     */
    private TerrainLoader() {
    }

    /**
     * Loads everything and determines tile size.
     */
    public static void load() {

        Element xml = IOManager.getAsXML(Places.GraphicsTerrain, "terrain.xml");

        // parse xml and fill table
        XTable table = new XTable();
        table.fromXML(xml);

        // TODO checks about number of columns

        // load terrain images, combine colors and fill map
        int n = table.getRowCount();
        map = new HashMap<>(n);
        for (int row = 0; row < n; row++) {
            String id = table.getEntryAt(row, 0);
            String location = table.getEntryAt(row, 1);
            Image image = IOManager.getAsImage(Places.GraphicsTerrain, location);
            Color color = TerrainLoader.ColorFromHex(table.getEntryAt(row, 2));
            TerrainTile tile = new TerrainTile(image, color);
            map.put(id, tile);
        }

        // TODO check tileSize is the same for all
        Collection<TerrainTile> tiles = map.values();
        tileSize = null;
        for (TerrainTile tile : tiles) {
            int width = tile.getImage().getWidth(null);
            int height = tile.getImage().getHeight(null);
            Dimension size = new Dimension(width, height);
            if (tileSize == null) {
                tileSize = size;
            } else if (!tileSize.equals(size)) {
                LOG.log(Level.SEVERE, "A terrain tile differs in size");
                // TODO rescale(?) or exception
            }
        }
    }

    /**
     *
     * @return
     */
    public static Set<String> getIDs() {
        return map.keySet();
    }

    /**
     *
     * @param id
     * @return
     */
    public static TerrainTile getTile(String id) {
        return map.get(id);
    }

    /**
     * Parses a six character string (hex presentation) into a Color object.
     *
     * @param hex
     * @return
     */
    private static Color ColorFromHex(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }

    /**
     * Returns tile size.
     *
     * @return
     */
    public static Dimension getTileSize() {
        return tileSize; // or copy new?
    }
}
