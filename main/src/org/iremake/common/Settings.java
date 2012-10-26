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
import nu.xom.Element;
import org.iremake.common.resources.Loader;
import org.iremake.common.resources.Places;
import org.tools.xml.common.Table;

/**
 *
 */
public class Settings {

    private static Map<String, String> terrainTypes = new HashMap<>(0);

    private Settings() {
    }

    public static void load() {

        Element xml = Loader.getAsXML(Places.ScenarioSettings, "terrain.xml");

        // parse xml and fill table
        Table table = new Table();
        table.fromXML(xml);

        // load terrain images, combine colors and fill map
        int n = table.getRowCount();
        terrainTypes = new HashMap<>(n);
        for (int row = 0; row < n; row++) {
            String id = table.getEntryAt(row, 0);
            String type = table.getEntryAt(row, 1);
            terrainTypes.put(id, type);
        }

    }

    // TODO check that the set of ids from the graphical tiles set is identical with the id set here
    public static String getTerrainType(String id) {
        return terrainTypes.get(id);
    }
}
