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
package org.iremake.common.model.map;

import java.util.logging.Logger;
import org.tools.xml.FullXMLable;
import org.tools.xml.Node;

/**
 *
 */
public class MapItem implements FullXMLable {

    private static final Logger LOG = Logger.getLogger(MapItem.class.getName());
    private static final String XML_NAME = "MapItem";

    /**
     *
     */
    public enum MapItemType {

        /**
         *
         */
        Engineer,
        /**
         *
         */
        Army,
        /**
         *
         */
        Fleet,
        /**
         *
         */
        City;
    }
    private MapItemType type;
    private MapPosition position;

    /**
     *
     * @param type
     * @param position
     */
    public MapItem(MapItemType type, MapPosition position) {
        this.type = type;
        this.position = position;
    }

    /**
     *
     * @return
     */
    public MapPosition getPosition() {
        return position;
    }

    @Override
    public Node toXML() {
        Node element = new Node(XML_NAME);

        element.addAttribute("type", type.name());
        element.addAttribute("position-row", String.valueOf(position.row));
        element.addAttribute("position-column", String.valueOf(position.column));

        return element;
    }

    @Override
    public void fromXML(Node parent) {

        parent.checkNode(XML_NAME);

        type = MapItemType.valueOf(parent.getAttributeValue("type"));
        int row = parent.getAttributeValueAsInt("position-row");
        int column = parent.getAttributeValueAsInt("position-column");
        position = new MapPosition(row, column);
    }
}
