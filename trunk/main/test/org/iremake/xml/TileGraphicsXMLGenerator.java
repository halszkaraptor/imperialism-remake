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
 * All the graphics used in a scenario (mostly tiles and units).
 */
public class TileGraphicsXMLGenerator {

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Element parent = new Element("Tile-Graphics");
        parent.addAttribute(new Attribute("tile-width", "80"));
        parent.addAttribute(new Attribute("tile-height", "80"));

        parent.appendChild(createTerrain());
        parent.appendChild(createRiver());
        parent.appendChild(createResources());
        parent.appendChild(createMisc());
        parent.appendChild(createUnits());
        Resource resource = ResourceUtils.asResource("content.xml");
        XMLHelper.write(resource, parent);
    }

    public static Element createRiver() {
        Element parent = new Element("River-Overlays");
        parent.addAttribute(new Attribute("location", "river.overlays.png"));
        return parent;
    }

    /**
     *
     * @return
     */
    public static Element createUnits() {
        Element parent = new Element("Unit-Overlays");
        parent.addAttribute(new Attribute("base", "units"));
        parent.appendChild(addUnitTile("infantry", "stand", "infantry.stand.png"));
        parent.appendChild(addUnitTile("infantry", "shoot", "infantry.shoot.png"));
        parent.appendChild(addUnitTile("infantry", "charge", "infantry.charge.png"));
        return parent;
    }

    /**
     *
     * @param type
     * @param action
     * @param location
     * @return
     */
    public static Element addUnitTile(String type, String action, String location) {
        Element child = new Element("Unit");
        child.addAttribute(new Attribute("type", type));
        child.addAttribute(new Attribute("action", action));
        child.addAttribute(new Attribute("location", location));
        return child;
    }

    /**
     *
     * @return
     */
    public static Element createMisc() {
        Element parent = new Element("Miscellaneous-Overlays");
        parent.appendChild(addMiscTile("city", "city.png"));
        parent.appendChild(addMiscTile("garrison", "garrison.png"));
        return parent;
    }

    /**
     *
     * @param id
     * @param location
     * @return
     */
    public static Element addMiscTile(String id, String location) {
        Element child = new Element("Overlay");
        child.addAttribute(new Attribute("id", id));
        child.addAttribute(new Attribute("location", location));
        return child;
    }

    /**
     *
     * @throws IOException
     */
    public static Element createResources() {
        Element parent = new Element("Resource-Overlays");
        parent.addAttribute(new Attribute("base", "resources"));
        parent.appendChild(addResourceOverlay(1, "resource.grain.png"));
        parent.appendChild(addResourceOverlay(2, "resource.orchard.png"));
        parent.appendChild(addResourceOverlay(3, "resource.buffalo.png"));
        parent.appendChild(addResourceOverlay(4, "resource.cotton.png"));
        parent.appendChild(addResourceOverlay(5, "resource.sheep.png"));
        parent.appendChild(addResourceOverlay(6, "resource.forest.png"));
        parent.appendChild(addResourceOverlay(7, "resource.scrubforest.png"));
        parent.appendChild(addResourceOverlay(8, "resource.oil.png"));
        parent.appendChild(addResourceOverlay(9, "resource.coal.png"));
        parent.appendChild(addResourceOverlay(10, "resource.ore.png"));
        return parent;
    }

    /**
     *
     * @param id
     * @param location
     * @param visible
     * @return
     */
    public static Element addResourceOverlay(int id, String location) {
        Element child = new Element("Overlay");
        child.addAttribute(new Attribute("id", String.valueOf(id)));
        child.addAttribute(new Attribute("location", location));
        return child;
    }

    /**
     *
     * @throws IOException
     */
    public static Element createTerrain() {
        Element parent = new Element("Terrain-Tiles");
        parent.addAttribute(new Attribute("base", "terrains"));
        parent.appendChild(addTerrainTile(1, "terrain.sea.png", "80a0e0"));
        parent.appendChild(addTerrainTile(2, "terrain.plains.png", "d0f0a0"));
        parent.appendChild(addTerrainTile(3, "terrain.hills.png", "604020"));
        parent.appendChild(addTerrainTile(4, "terrain.mountains.png", "909090"));
        parent.appendChild(addTerrainTile(5, "terrain.tundra.png", "c0c0c0"));
        parent.appendChild(addTerrainTile(6, "terrain.swamp.png", "309030"));
        parent.appendChild(addTerrainTile(7, "terrain.desert.png", "c0c0a0"));
        return parent;
    }

    /**
     *
     * @param id
     * @param location
     * @param color
     * @return
     */
    public static Element addTerrainTile(int id, String location, String color) {
        Element child = new Element("Tile");
        child.addAttribute(new Attribute("id", String.valueOf(id)));
        child.addAttribute(new Attribute("location", location));
        child.addAttribute(new Attribute("color", color));
        return child;
    }
}
