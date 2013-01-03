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
import nu.xom.Elements;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;

/**
 * Reads some settings, like ids of Terrain...
 */
// TODO do we need to know the ids here? (they are defined twice, in the terrain set and in the scenario settings)
// TODO unmake static
public class Settings {

    private static Map<Integer, String> terrainNames;
    private static Map<Integer, String> resourceNames;
    private static int defaultID;

    // TODO get this from xml
    public static int getDefaultTerrainID() {
        return defaultID;
    }

    private Settings() {
    }

    /**
     * Load everything.
     */
    public static void load() {

        Element xml = IOManager.getAsXML(Places.Common, "settings.xml");

        // terrains
        Element child = xml.getFirstChildElement("Terrains");
        defaultID = Integer.parseInt(child.getAttributeValue("default-id"));
        Elements elements = child.getChildElements("Terrain");
        terrainNames = new HashMap<>(elements.size());
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            int id = Integer.parseInt(element.getAttributeValue("id"));
            String name = element.getAttributeValue("name");
            terrainNames.put(id, name);
        }

        // resources
        child = xml.getFirstChildElement("Resources");
        elements = child.getChildElements("Resource");
        resourceNames = new HashMap<>(elements.size());
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            int id = Integer.parseInt(element.getAttributeValue("id"));
            String name = element.getAttributeValue("name");
            resourceNames.put(id, name);
        }
    }

    /**
     *
     * @return
     */
    public static Set<Integer> getTerrainIDs() {
        return terrainNames.keySet();
    }

    /**
     *
     * @param id
     * @return
     */
    // TODO check that the set of ids from the graphical tiles set is identical with the id set here
    public static String getTerrainName(Integer id) {
        return terrainNames.get(id);
    }
}
