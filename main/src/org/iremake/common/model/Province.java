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
package org.iremake.common.model;

import java.util.logging.Logger;
import org.iremake.common.model.map.MapPosition;
import org.tools.xml.FullXMLable;
import org.tools.xml.Node;
import org.tools.xml.XMLHelper;

/**
 * A province, currently with id, name and town position.
 */
public class Province implements FullXMLable {

    private static final Logger LOG = Logger.getLogger(Province.class.getName());
    /* Sea tiles have a default value which is defined here and not in the xml */
    /**
     *
     */
    public static final int NONE = 0;
    public static final String XML_NAME = "Province";
    private int id;
    private String name;
    private MapPosition town = new MapPosition();

    /**
     * Empty constructor for creation in XList<E>.fromXML() using
     * class.newInstance().
     */
    public Province() {
    }

    /**
     * Set id and name.
     *
     * @param id the id
     * @param name the name
     */
    public Province(int id, String name) {
        // TODO check null or ID == NONE
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id.
     *
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * Returns the town's position.
     *
     * @return the position
     */
    public MapPosition getTownPosition() {
        return town;
    }

    /**
     * Sets a new town position
     *
     * @param position the new position
     */
    public void setTownPosition(MapPosition position) {
        town = position;
    }

    /**
     * String representation is always the name, makes it easy to use in UI
     * elements.
     *
     * @return the name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Export to XML.
     *
     * @return xml object
     */
    @Override
    public Node toXML() {
        Node element = new Node(XML_NAME);
        element.addAttribute("id", Integer.toString(id));
        element.addAttribute("name", name);
        element.addAttribute("town-row", String.valueOf(town.row));
        element.addAttribute("town-column", String.valueOf(town.column));

        return element;
    }

    /**
     * Import from XML.
     *
     * @param parent xml object
     */
    @Override
    public void fromXML(Node parent) {

        parent.checkNode(XML_NAME);

        id = parent.getAttributeValueAsInt("id");
        name = parent.getAttributeValue("name");
        int row = parent.getAttributeValueAsInt("town-row");
        int column = parent.getAttributeValueAsInt("town-column");
        town = new MapPosition(row, column);
    }
}
