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

import nu.xom.Attribute;
import nu.xom.Element;
import org.tools.xml.FullXMLable;

/**
 *
 */
public class MapItem implements FullXMLable {

    private static final String XML_NAME = "MapItem";

    public enum MapItemType {

        Engineer, Army, Fleet, City;
    }
    private MapItemType type;
    private MapPosition position;

    public MapItem(MapItemType type, MapPosition position) {
        this.type = type;
        this.position = position;
    }

    @Override
    public Element toXML() {
        Element element = new Element(XML_NAME);

        element.addAttribute(new Attribute("type", type.name()));
        element.addAttribute(new Attribute("position-row", String.valueOf(position.row)));
        element.addAttribute(new Attribute("position-column", String.valueOf(position.column)));

        return element;
    }

    @Override
    public void fromXML(Element parent) {

        // TODO checks (null, name)
        type = MapItemType.valueOf(parent.getAttributeValue("type"));
        int row = Integer.parseInt(parent.getAttributeValue("position-row"));
        int column = Integer.parseInt(parent.getAttributeValue("position-column"));
        position = new MapPosition(row, column);
    }
}
