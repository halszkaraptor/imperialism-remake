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
import nu.xom.Attribute;
import nu.xom.Element;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;
import org.tools.xml.XMLHelper;

/**
 *
 */
public class SettingsXMLGenerator {

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Element parent = new Element("Settings");
        parent.appendChild(createTerrainSettings());
        parent.appendChild(createResourceSettings());

        Resource resource = ResourceUtils.asResource("settings.xml");
        XMLHelper.write(resource, parent);
    }

    private static Element createTerrainSettings() {
        Element parent = new Element("Terrains");
        parent.addAttribute(new Attribute("default-id", "1"));
        parent.appendChild(addTerrainType(1, "Sea"));
        parent.appendChild(addTerrainType(2, "Plains"));
        parent.appendChild(addTerrainType(3, "Hills"));
        parent.appendChild(addTerrainType(4, "Mountains"));
        parent.appendChild(addTerrainType(5, "Tundra"));
        parent.appendChild(addTerrainType(6, "Swamp"));
        parent.appendChild(addTerrainType(7, "Desert"));
        return parent;
    }

    private static Element addTerrainType(int id, String name) {
        Element child = new Element("Terrain");
        child.addAttribute(new Attribute("id", String.valueOf(id)));
        child.addAttribute(new Attribute("name", name));
        return child;
    }

    private static Element createResourceSettings() {
        Element parent = new Element("Resources");
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

    private static Element addResourceType(int id, String name, boolean visible) {
        Element child = new Element("Resource");
        child.addAttribute(new Attribute("id", String.valueOf(id)));
        child.addAttribute(new Attribute("name", name));
        child.addAttribute(new Attribute("visible", String.valueOf(visible)));
        return child;
    }
}
