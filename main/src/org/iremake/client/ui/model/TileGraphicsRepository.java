/*
 * Copyright (C) 2013 Trilarion
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
package org.iremake.client.ui.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.ui.utils.GraphicsUtils;
import org.tools.xml.Node;
import org.tools.xml.ReadXMLable;
import org.tools.xml.XMLHelper;

/**
 *
 */
// TODO unit overlay action, type as enum
// TODO image not existing test
public class TileGraphicsRepository implements ReadXMLable {

    private static final Logger LOG = Logger.getLogger(TileGraphicsRepository.class.getName());
    private static final String XML_NAME = "Tile-Graphics";
    /* the map storing the ui tile information for each terrain id */
    private Map<Integer, Tile> terrainTiles = new HashMap<>();
    /* the map storing the ui overlay image for each id */
    private Map<Integer, Image> resourceOverlays = new HashMap<>();
    /* */
    private Map<String, Image> miscOverlays = new HashMap<>();
    /* */
    private Map<String, Image> unitOverlays = new HashMap<>();
    private Image[] riverOverlays;

    /* the size of the tiles, i.e. every stored image must have that size */
    private Dimension tileSize;

    /**
     * Returns the image for a given terrain id.
     *
     * @param id terrain id
     * @return an image
     */
    public Image getTerrainTile(Integer id) {
        // TODO id not contained?
        return terrainTiles.get(id).image;
    }

    /**
     * Returns the color for a given terrain id.
     *
     * @param id terrain id
     * @return a color
     */
    public Color getTerrainTileColor(Integer id) {
        return terrainTiles.get(id).color;
    }

    /**
     * Returns an unmodifiable set of all terrain ids in the map.
     *
     * @return set of terrain ids.
     */
    public Set<Integer> getTerrainTileIDs() {
        return Collections.unmodifiableSet(terrainTiles.keySet());
    }

    /**
     * Returns the tile size.
     *
     * @return the size
     */
    public Dimension getTerrainTileSize() {
        return new Dimension(tileSize);
    }

    /**
     * Returns the image for a given id.
     *
     * @param id id
     * @return an image or null if id not existing
     */
    public Image getResourceOverlay(Integer id) {
        return resourceOverlays.get(id);
    }

    /**
     * Returns an unmodifiable set of ids in the map.
     *
     * @return set of ids
     */
    public Set<Integer> getResourceOverlayIDs() {
        return Collections.unmodifiableSet(resourceOverlays.keySet());
    }

    /**
     *
     * @param id
     * @return
     */
    public Image getMiscOverlay(String id) {
        return miscOverlays.get(id);
    }

    /**
     *
     * @param id
     * @return
     */
    public Image getUnitOverlay(String type, String action) {
        return unitOverlays.get(type + "|" + action);
    }

    /**
     *
     * @param id
     * @return
     */
    public Image getRiverOverlay(int id) {
        return riverOverlays[id];
    }

    /**
     * internal class bundling an image and a color and defining a reasonable
     * hash code and equals method
     */
    private static class Tile {

        public Image image;
        public Color color;

        @Override
        public int hashCode() {
            return image.hashCode() * 13 + color.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Tile other = (Tile) obj;
            if (!Objects.equals(this.image, other.image)) {
                return false;
            }
            if (!Objects.equals(this.color, other.color)) {
                return false;
            }
            return true;
        }
    }

    @Override
    public void fromXML(Node parent) {
        // clear all maps
        terrainTiles.clear();
        resourceOverlays.clear();
        miscOverlays.clear();

        parent.checkNode(XML_NAME);

        int width = parent.getAttributeValueAsInt("tile-width");
        int height = parent.getAttributeValueAsInt("tile-height");
        tileSize = new Dimension(width, height);

        // import terrain tiles
        Node element = parent.getFirstChild("Terrain-Tiles");

        String base = element.getAttributeValue("base");

        for (Node child : element.getChildren()) {
            child.checkNode("Tile");

            Integer id = child.getAttributeValueAsInt("id");
            String location = child.getAttributeValue("location");
            Tile tile = new Tile();
            tile.image = IOManager.getAsImage(Places.GraphicsScenario, base + "/" + location);
            tile.color = GraphicsUtils.convertHexToColor(child.getAttributeValue("color"));
            terrainTiles.put(id, tile);
        }

        // TODO check tileSize is the same for all
        for (Tile tile : terrainTiles.values()) {
            width = tile.image.getWidth(null);
            height = tile.image.getHeight(null);
            Dimension size = new Dimension(width, height);
            if (!tileSize.equals(size)) {
                LOG.log(Level.SEVERE, "A terrain tile differs in size");
                // TODO rescale(?) or exception
            }
        }

        {
            // import river overlay
            element = parent.getFirstChild("River-Overlays");
            String location = element.getAttributeValue("location");
            BufferedImage rivers = IOManager.getAsImage(Places.GraphicsScenario, location);
            // test if width and height is a multiple of tileSize
            if (rivers.getWidth(null) % tileSize.width != 0 || rivers.getHeight(null) % tileSize.height != 0) {
                // LOG
            }
            int columns = rivers.getWidth(null) / tileSize.width;
            int rows = rivers.getHeight(null) / tileSize.height;
            // image too small
            if (rows * columns < 36) {
            }
            // lets copy out the parts and store them
            riverOverlays = new Image[36];
            for (int i = 0; i < 36; i++) {
                int x = i % 8 * tileSize.width;
                int y = i / 8 * tileSize.height;
                riverOverlays[i] = rivers.getSubimage(x, y, tileSize.width, tileSize.height);
            }
        }

        // import resource overlays
        element = parent.getFirstChild("Resource-Overlays");

        base = element.getAttributeValue("base");

        for (Node child : element.getChildren()) {
            child.checkNode("Overlay");

            Integer id = child.getAttributeValueAsInt("id");
            String location = child.getAttributeValue("location");
            Image image = IOManager.getAsImage(Places.GraphicsScenario, base + "/" + location);
            resourceOverlays.put(id, image);
        }

        // import misc overlays
        element = parent.getFirstChild("Miscellaneous-Overlays");

        for (Node child : element.getChildren()) {
            child.checkNode("Overlay");

            String id = child.getAttributeValue("id");
            String location = child.getAttributeValue("location");
            Image image = IOManager.getAsImage(Places.GraphicsScenario, location);
            miscOverlays.put(id, image);
        }

        // import unit overlays
        element = parent.getFirstChild("Unit-Overlays");
        base = element.getAttributeValue("base");

        for (Node child : element.getChildren()) {
            child.checkNode("Unit");

            String type = child.getAttributeValue("type");
            String action = child.getAttributeValue("action");
            String location = child.getAttributeValue("location");
            Image image = IOManager.getAsImage(Places.GraphicsScenario, base + "/" + location);
            unitOverlays.put(type + "|" + action, image);
        }
    }
}
