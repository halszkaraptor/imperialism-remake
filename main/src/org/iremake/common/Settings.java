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
package org.iremake.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nu.xom.Element;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.tools.xml.XTable;

/**
 * Reads some settings, like ids of Terrain...
 */
// TODO do we need to know the ids here? (they are defined twice, in the terrain set and in the scenario settings)
// TODO unmake static
public class Settings {

    private static Map<Integer, String> terrainTypes = new HashMap<>(0);

    // TODO get this from xml
    public static Integer getDefaultTerrainID() {
        return 3;
    }

    private Settings() {
    }

    /**
     * Load everything.
     */
    public static void load() {

        Element xml = IOManager.getAsXML(Places.ScenarioSettings, "terrain.xml");

        // parse xml and fill table
        XTable table = new XTable();
        table.fromXML(xml);

        // load terrain images, combine colors and fill map
        int n = table.getRowCount();
        terrainTypes = new HashMap<>(n);
        for (int row = 0; row < n; row++) {
            Integer id = Integer.valueOf(table.getEntryAt(row, 0));
            String type = table.getEntryAt(row, 1);
            terrainTypes.put(id, type);
        }
    }

    /**
     *
     * @return
     */
    public static Set<Integer> getTerrainIDs() {
        return terrainTypes.keySet();
    }

    /**
     *
     * @param id
     * @return
     */
    // TODO check that the set of ids from the graphical tiles set is identical with the id set here
    public static String getTerrainType(Integer id) {
        return terrainTypes.get(id);
    }
}