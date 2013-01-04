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
public class ResourceOverlays implements XMLable {

    private Map<Integer, Tile> map = new HashMap<>();

    private class Tile {

        public Image image;
        public boolean visible;

        @Override
        public int hashCode() {
            return image.hashCode();
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
            if (this.visible != other.visible) {
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
     * @return
     */
    public Set<Integer> getIDs() {
        return map.keySet();
    }

    @Override
    public Element toXML() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
            tile.image = IOManager.getAsImage(Places.GraphicsResources, location);
            tile.visible = Boolean.valueOf(child.getAttributeValue("visible"));
            map.put(id, tile);
        }
    }
}
