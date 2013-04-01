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
import java.util.logging.Logger;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;
import org.tools.xml.Node;
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
        Node parent = new Node("Tile-Graphics");
        parent.addAttribute("tile-width", "80");
        parent.addAttribute("tile-height", "80");

        parent.appendChild(createTerrain());
        parent.appendChild(createRiver());
        parent.appendChild(createResources());
        parent.appendChild(createMisc());
        parent.appendChild(createUnits());
        Resource resource = ResourceUtils.asResource("content.xml");
        XMLHelper.write(resource, parent);
    }

    /**
     *
     * @return
     */
    public static Node createRiver() {
        Node parent = new Node("River-Overlays");
        parent.addAttribute("location", "river.overlays.png");
        return parent;
    }

    /**
     *
     * @return
     */
    public static Node createUnits() {
        Node parent = new Node("Unit-Overlays");
        parent.addAttribute("base", "units");
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
    public static Node addUnitTile(String type, String action, String location) {
        Node child = new Node("Unit");
        child.addAttribute("type", type);
        child.addAttribute("action", action);
        child.addAttribute("location", location);
        return child;
    }

    /**
     *
     * @return
     */
    public static Node createMisc() {
        Node parent = new Node("Miscellaneous-Overlays");
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
    public static Node addMiscTile(String id, String location) {
        Node child = new Node("Overlay");
        child.addAttribute("id", id);
        child.addAttribute("location", location);
        return child;
    }

    /**
     *
     * @throws IOException
     */
    public static Node createResources() {
        Node parent = new Node("Resource-Overlays");
        parent.addAttribute("base", "resources");
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
        parent.appendChild(addResourceOverlay(11, "resource.horse.png"));        
        return parent;
    }

    /**
     *
     * @param id
     * @param location
     * @param visible
     * @return
     */
    public static Node addResourceOverlay(int id, String location) {
        Node child = new Node("Overlay");
        child.addAttribute("id", String.valueOf(id));
        child.addAttribute("location", location);
        return child;
    }

    /**
     *
     * @throws IOException
     */
    public static Node createTerrain() {
        Node parent = new Node("Terrain-Tiles");
        parent.addAttribute("base", "terrains");
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
    public static Node addTerrainTile(int id, String location, String color) {
        Node child = new Node("Tile");
        child.addAttribute("id", String.valueOf(id));
        child.addAttribute("location", location);
        child.addAttribute("color", color);
        return child;
    }
    private static final Logger LOG = Logger.getLogger(TileGraphicsXMLGenerator.class.getName());
}
