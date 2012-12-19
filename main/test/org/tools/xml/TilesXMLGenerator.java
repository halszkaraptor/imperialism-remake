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
package org.tools.xml;

import java.io.IOException;
import nu.xom.Attribute;
import nu.xom.Element;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;

/**
 *
 */
public class TilesXMLGenerator {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        createTerrain();

        createResources();
    }

    public static void createResources() throws IOException {
        Element parent = new Element("Resource-Overlays");
        parent.appendChild(addResourceOverlay(1, "terrain.sea.png", true));
        parent.appendChild(addResourceOverlay(2, "terrain.plains.png", false));

        Resource resource = ResourceUtils.asResource("resources.xml");
        XMLHelper.write(resource, parent);
    }

    public static Element addResourceOverlay(Integer id, String location, boolean visible) {
        Element child = new Element("Overlay");
        child.addAttribute(new Attribute("id", String.valueOf(id)));
        child.addAttribute(new Attribute("location", location));
        child.addAttribute(new Attribute("visible", String.valueOf(visible)));
        return child;
    }

    /**
     *
     */
    public static void createTerrain() throws IOException {
        Element parent = new Element("Terrain-Tiles");
        parent.addAttribute(new Attribute("tile-width", "80"));
        parent.addAttribute(new Attribute("tile-height", "80"));
        parent.appendChild(addTerrainTile(1, "terrain.sea.png", "80a0e0"));
        parent.appendChild(addTerrainTile(2, "terrain.plains.png", "d0f0a0"));
        parent.appendChild(addTerrainTile(3, "terrain.hills.png", "604020"));
        parent.appendChild(addTerrainTile(4, "terrain.mountains.png", "909090"));
        parent.appendChild(addTerrainTile(5, "terrain.tundra.png", "c0c0c0"));
        parent.appendChild(addTerrainTile(6, "terrain.swamp.png", "309030"));
        parent.appendChild(addTerrainTile(7, "terrain.desert.png", "c0c0a0"));

        Resource resource = ResourceUtils.asResource("terrains.xml");
        XMLHelper.write(resource, parent);
    }

    public static Element addTerrainTile(Integer id, String location, String color) {
        Element child = new Element("Tile");
        child.addAttribute(new Attribute("id", String.valueOf(id)));
        child.addAttribute(new Attribute("location", location));
        child.addAttribute(new Attribute("color", color));
        return child;
    }
}
