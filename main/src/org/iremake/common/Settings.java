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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.xml.Node;

/**
 * Holds the settings, which only (in opposition to options) effect the game
 * model.
 *
 * One example are the ids of terrain and resources and their descriptions. They
 * are defined in the UI client part again, but this is intentional because the
 * client has to make sure by himself that for all terrains defined in the
 * setting there is a terrain image entry existing in the terrain image
 * definition.
 *
 * If we want to switch between different settings we might want to make this
 * class non-static.
 */
public class Settings {

    private static final Logger LOG = Logger.getLogger(Settings.class.getName());
    /* common port for the network communication */
    public static final int NETWORK_PORT = 19_357;
    /* mapping: id -> description of terrain types */
    private static Map<Integer, String> terrainNames;
    /* default terrain id */
    private static int defaultTerrainID;
    /* mapping: id -> description of resource types */
    private static Map<Integer, String> resourceNames;
    /**
     * the default (none) resource id
     */
    public static final int RESOURCE_NONE = 0;

    /**
     * No instantiation.
     */
    private Settings() {
    }

    /**
     * Load everything.
     */
    public static void load() {

        Node parent = IOManager.getAsXML(Places.Common, "settings.xml");

        // terrains
        Node node = parent.getFirstChild("Terrains");
        defaultTerrainID = Integer.parseInt(node.getAttributeValue("default-id"));
        terrainNames = new HashMap<>(node.getChildCount());
        for (Node child: node.getChildren()) {
            int id = child.getAttributeValueAsInt("id");
            String name = child.getAttributeValue("name");
            terrainNames.put(id, name);
        }

        // resources
        node = parent.getFirstChild("Resources");
        resourceNames = new HashMap<>(node.getChildCount());
        for (Node child: node.getChildren()) {
            int id = child.getAttributeValueAsInt("id");
            String name = child.getAttributeValue("name");
            resourceNames.put(id, name);
        }
    }

    /**
     * Get a set of all possible terrain IDs.
     *
     * @return the set
     */
    public static Set<Integer> getTerrainIDs() {
        return Collections.unmodifiableSet(terrainNames.keySet());
    }

    /**
     * Returns the description of the terrain id.
     *
     * @param id the id
     * @return the description or null if id is not contained
     */
    // TODO check that the set of ids from the graphical tiles set is identical with the id set here
    public static String getTerrainName(int id) {
        return terrainNames.get(id);
    }

    /**
     * Returns the description of the resource id.
     *
     * @param id the id
     * @return the description or null if id is not contained
     */
    public static String getResourceName(int id) {
        return resourceNames.get(id);
    }

    /**
     * Returns the default id for terrain, e.g. used in new project without a
     * predefined map.
     *
     * @return the default id
     */
    public static int getDefaultTerrainID() {
        return defaultTerrainID;
    }
}
