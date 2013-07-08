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
package org.iremake.xml;

import java.io.IOException;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;
import org.tools.xml.Node;
import org.tools.xml.XMLHelper;

/**
 * The bindings of terrain and resource types to numbers is the same for each
 * scenario. The names might differ though.
 */
public class SettingsXMLGenerator {

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Node parent = new Node("Settings");
        parent.appendChild(createTerrainSettings());
        parent.appendChild(createResourceSettings());

        Resource resource = ResourceUtils.asResource("settings.xml");
        XMLHelper.write(resource, parent);
    }

    private static Node createTerrainSettings() {
        Node parent = new Node("Terrains");
        parent.addAttribute("default-id", "1");
        parent.appendChild(addTerrainType(1, "Sea"));
        parent.appendChild(addTerrainType(2, "Plains"));
        parent.appendChild(addTerrainType(3, "Hills"));
        parent.appendChild(addTerrainType(4, "Mountains"));
        parent.appendChild(addTerrainType(5, "Tundra"));
        parent.appendChild(addTerrainType(6, "Swamp"));
        parent.appendChild(addTerrainType(7, "Desert"));
        return parent;
    }

    private static Node addTerrainType(int id, String name) {
        Node child = new Node("Terrain");
        child.addAttribute("id", String.valueOf(id));
        child.addAttribute("name", name);
        return child;
    }

    private static Node createResourceSettings() {
        Node parent = new Node("Resources");
        parent.appendChild(addResourceType(1, "Grain", true));
        parent.appendChild(addResourceType(2, "Orchard", true));
        parent.appendChild(addResourceType(3, "Buffalo", true));
        parent.appendChild(addResourceType(4, "Cotton", true));
        parent.appendChild(addResourceType(5, "Sheep", true));
        parent.appendChild(addResourceType(6, "Forest", true));
        parent.appendChild(addResourceType(7, "Scrub forest", true));
        parent.appendChild(addResourceType(8, "Oil", false));
        parent.appendChild(addResourceType(9, "Coal", false));
        parent.appendChild(addResourceType(10, "Ore", false));
        return parent;
    }

    private static Node addResourceType(int id, String name, boolean visible) {
        Node child = new Node("Resource");
        child.addAttribute("id", String.valueOf(id));
        child.addAttribute("name", name);
        child.addAttribute("visible", String.valueOf(visible));
        return child;
    }
}
