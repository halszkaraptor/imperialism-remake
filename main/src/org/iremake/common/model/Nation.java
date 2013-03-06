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
import org.iremake.common.model.map.MapItem;
import org.iremake.common.model.map.MapItem.MapItemType;
import org.tools.xml.FullXMLable;
import org.tools.xml.Node;
import org.tools.xml.XList;
import org.tools.xml.XMLHelper;
import org.tools.xml.XProperty;

/**
 * A nation. It has a property list and a list of provinces.
 */
public class Nation implements FullXMLable {

    private static final Logger LOG = Logger.getLogger(Nation.class.getName());
    /**
     *
     */
    public static final String KEY_CAPITAL = "capital province";
    /**
     *
     */
    public static final String KEY_COLOR = "color";
    /**
     *
     */
    public static final String KEY_NAME = "name";
    private static final String XML_NAME = "Nation";
    /* property list */
    private XProperty properties = new XProperty(10);
    /* list of owned provinces */
    private XList<Province> provinces = new XList<>(Province.class, "Provinces");
    /* list of owned map items */
    private XList<MapItem> units = new XList<>(MapItem.class, "Units");

    /**
     * Need an empty constructor for creation in fromXML in scenario, i.e.
     * loading a new scenario, where reflection newInstance() is used. (More
     * specific: see XList<E>.fromXML()).
     */
    public Nation() {
        // useful default properties
        properties.put(KEY_NAME, "Unnamed");
        properties.put(KEY_COLOR, "808080");
        properties.putInt(KEY_CAPITAL, Province.NONE);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    /**
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return properties.get(key);
    }

    /**
     * Returns an iterable over the provinces.
     *
     * @return the iterable
     */
    public XList<Province> getProvinces() {
        return provinces;
    }

    /**
     *
     * @return
     */
    public XList<MapItem> getUnits() {
        return units;
    }

    /**
     * Adds a province. The same province is never added twice, but a log entry
     * is written in this case.
     *
     * @param province
     */
    // TODO only give title?
    public void addProvince(Province province) {
        // TODO check if already contained
        provinces.addElement(province);
        // what if they are renamed, sorting here?
        provinces.sort();
    }

    /**
     * In ui elements, Nations are always shown by their name property.
     *
     * @return the name
     */
    @Override
    public String toString() {
        return properties.get("name");
    }

    /**
     * Export to XML.
     *
     * @return xml object
     */
    @Override
    public Node toXML() {
        Node element = new Node(XML_NAME);

        element.appendChild(properties.toXML());
        element.appendChild(provinces.toXML());
        element.appendChild(units.toXML());

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

        properties.fromXML(parent.getFirstChild(XProperty.XML_NAME));
        provinces.fromXML(parent.getFirstChild("Provinces"));

        // we add an engineer unit to the capital province
        int capital = properties.getInt(KEY_CAPITAL);
        for (Province province : provinces) {
            if (province.getID() == capital) {
                MapItem unit = new MapItem(MapItemType.Engineer, province.getTownPosition());
                units.addElement(unit);
            }
        }
    }
}
