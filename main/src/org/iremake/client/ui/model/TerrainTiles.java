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
package org.iremake.client.ui.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import nu.xom.Element;
import nu.xom.Elements;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.xml.ReadXMLable;

/**
 * UI part of the terrain tiles, holding the image and a color (for usage in the
 * mini map).
 *
 * More specific a map: id -> Tile is stored that allows to retrieve terrain
 * images for each terrain id.
 */
public class TerrainTiles implements ReadXMLable {

    /* the map storing the ui tile information for each terrain id */
    private Map<Integer, Tile> map = new HashMap<>();
    /* the size of the tiles, i.e. every stored image must have that size */
    private Dimension tileSize;

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

    /**
     * Returns the image for a given terrain id.
     *
     * @param id terrain id
     * @return an image
     */
    public Image getImage(Integer id) {
        // TODO id not contained?
        return map.get(id).image;
    }

    /**
     * Returns the color for a given terrain id.
     *
     * @param id terrain id
     * @return a color
     */
    public Color getColor(Integer id) {
        return map.get(id).color;
    }

    /**
     * Returns an unmodifiable set of all terrain ids in the map.
     *
     * @return set of terrain ids.
     */
    public Set<Integer> getIDs() {
        return Collections.unmodifiableSet(map.keySet());
    }

    /**
     * Returns the tile size.
     *
     * @return the size
     */
    public Dimension getTileSize() {
        return new Dimension(tileSize);
    }

    /**
     * Parses a six character string (hex presentation) into a Color object.
     *
     * @param hex a 6 character string as "ffffff" for white
     * @return the corresponding color
     */
    private static Color convertHexToColor(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }

    /**
     * Reading the content from an terrains.xml
     *
     * @param parent the xml object to read from
     */
    @Override
    public void fromXML(Element parent) {

        // clear the map
        map.clear();

        if (parent == null || parent.getLocalName().equals("TerrainTiles"));

        Elements children = parent.getChildElements();
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);

            if (!"Tile".equals(child.getLocalName())) {
                // TODO something is wrong
            }
            Integer id = Integer.valueOf(child.getAttributeValue("id"));
            String location = child.getAttributeValue("location");
            Tile tile = new Tile();
            tile.image = IOManager.getAsImage(Places.GraphicsTerrains, location);
            tile.color = TerrainTiles.convertHexToColor(child.getAttributeValue("color"));
            map.put(id, tile);
        }

        // TODO check tileSize is the same for all
        int width = Integer.parseInt(parent.getAttributeValue("tile-width"));
        int height = Integer.parseInt(parent.getAttributeValue("tile-height"));
        tileSize = new Dimension(width, height);
        for (Tile tile : map.values()) {
            width = tile.image.getWidth(null);
            height = tile.image.getHeight(null);
            Dimension size = new Dimension(width, height);
            if (!tileSize.equals(size)) {
                // LOG.log(Level.SEVERE, "A terrain tile differs in size");
                // TODO rescale(?) or exception
            }
        }
    }
}
