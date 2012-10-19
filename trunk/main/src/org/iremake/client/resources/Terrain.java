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
import java.util.Objects;
import java.util.Set;
import nu.xom.Element;
import org.tools.xml.XMLHelper;
import org.tools.xml.common.Table;

/**
 *
 */
public class Terrain {

    /**
     *      */
    private static Map<String, TerrainTile> map = new HashMap<>(0);

    private static class TerrainTile {

        public String type;
        public int variant;
        public Image image;
        public Color color;

        public TerrainTile(String t, int v, Image i, Color c) {
            type = t;
            variant = v;
            image = i;
            color = c;
        }

        @Override
        public int hashCode() {
            return type.hashCode() + variant;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TerrainTile other = (TerrainTile) obj;
            if (!Objects.equals(this.type, other.type)) {
                return false;
            }
            if (this.variant != other.variant) {
                return false;
            }
            return true;
        }
    }

    /**
     * Static class, prevent instantiation.
     */
    private Terrain() {
    }

    /**
     *
     * @param location
     */
    public static void loadTerrain(String location) {
        // read from xml file
        Element xml = XMLHelper.read("/data/game/artwork/graphics/terrain/terrain.xml");

        // parse xml and fill table
        Table table = new Table();
        table.fromXML(xml);

        // load terrain images, combine colors and fill map
        int n = table.getRowCount();
        map = new HashMap<>(n);
        for (int row = 0; row < n; row++) {
            String type = table.getEntryAt(row, 0);
            int variant = Integer.valueOf(table.getEntryAt(row, 1));
            String id = table.getEntryAt(row, 2);
            Image image = null;
            Color color = new Color(0, 0, 0);
            TerrainTile tile = new TerrainTile(type, variant, image, color);
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
    public static String getType(String id) {
        return map.get(id).type;
    }

    /**
     *
     * @param id
     * @return
     */
    public static int getVariant(String id) {
        return map.get(id).variant;
    }

    /**
     *
     * @param id
     * @return
     */
    public static Image getImage(String id) {
        return map.get(id).image;
    }

    /**
     *
     * @param id
     * @return
     */
    public static Color getColor(String id) {
        return map.get(id).color;
    }
}
