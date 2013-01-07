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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nu.xom.Element;
import nu.xom.Elements;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.tools.xml.XMLable;

/**
 * UI part of the resource tiles, holding the image for each overlay.
 *
 * More specific a map: id -> image is stored that allows to retrieve overlay
 * images for each id.
 */
public class ResourceOverlays implements XMLable {

    /* the map storing the ui overlay image for each id */
    private Map<Integer, Image> map = new HashMap<>();

    /**
     * Returns the image for a given id.
     *
     * @param id id
     * @return an image or null if id not existing
     */
    public Image getImage(Integer id) {
        return map.get(id);
    }

    /**
     * Returns an unmodifiable set of ids in the map.
     *
     * @return set of ids
     */
    public Set<Integer> getIDs() {
        return Collections.unmodifiableSet(map.keySet());
    }

    /**
     * We never write.
     */
    @Override
    public Element toXML() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Reading the content from a xml object.
     *
     * @param parent
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
            Image image = IOManager.getAsImage(Places.GraphicsResources, location);
            map.put(id, image);
        }
    }
}
