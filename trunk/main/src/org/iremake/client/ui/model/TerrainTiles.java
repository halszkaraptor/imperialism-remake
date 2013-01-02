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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import nu.xom.Element;
import nu.xom.Elements;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.tools.xml.XMLable;

/**
 *
 */
public class TerrainTiles implements XMLable {

    private Map<Integer, Tile> map = new HashMap<>();
    private Dimension tileSize;

    private class Tile {

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
     *
     * @param id
     * @return
     */
    public Image getImage(Integer id) {
        // TODO id not contained
        return map.get(id).image;
    }

    /**
     *
     * @param id
     * @return
     */
    public Color getColor(Integer id) {
        return map.get(id).color;
    }

    /**
     *
     * @return
     */
    public Set<Integer> getIDs() {
        return map.keySet();
    }

    /**
     *
     * @return
     */
    public Dimension getTileSize() {
        return tileSize;
    }

    /**
     * Parses a six character string (hex presentation) into a Color object.
     *
     * @param hex
     * @return
     */
    private static Color ColorFromHex(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }

    @Override
    public Element toXML() {
        throw new UnsupportedOperationException("This XMLable is read-only.");
    }

    /**
     *
     * @param parent
     */
    @Override
    public void fromXML(Element parent) {

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
            tile.color = TerrainTiles.ColorFromHex(child.getAttributeValue("color"));
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
