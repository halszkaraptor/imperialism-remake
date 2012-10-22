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
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nu.xom.Element;
import org.iremake.client.ui.map.TerrainTile;
import org.tools.xml.common.Table;

/**
 *
 */
public class TerrainLoader {

    /** */
    private static Map<String, TerrainTile> map = new HashMap<>(0);

    /**
     * Static class, prevent instantiation.
     */
    private TerrainLoader() {
    }

    /**
     *
     * @param location
     */
    public static void load() {

        // Element xml = Loader.getAsXML(Places.Terrain, "terrain.xml");
        Element xml = Loader.getAsXML(Places.None, "terrain.xml");

        // parse xml and fill table
        Table table = new Table();
        table.fromXML(xml);
        
        // TODO checks about number of columns

        // load terrain images, combine colors and fill map
        int n = table.getRowCount();
        map = new HashMap<>(n);
        for (int row = 0; row < n; row++) {
            String id = table.getEntryAt(row, 0);
            String location = table.getEntryAt(row, 1);
            Image image = Loader.getAsImage(Places.Terrain, location);
            Color color = TerrainLoader.ColorFromHex(table.getEntryAt(row, 2));
            TerrainTile tile = new TerrainTile(image, color);
            map.put(id, tile);
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
    
    private static Color ColorFromHex(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }

}
