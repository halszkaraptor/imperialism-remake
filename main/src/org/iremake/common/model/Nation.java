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

import nu.xom.Element;
import nu.xom.Elements;
import org.tools.xml.FullXMLable;
import org.tools.xml.XList;
import org.tools.xml.XProperty;

/**
 * A nation. It has a property list and a list of provinces.
 */
public class Nation implements FullXMLable {

    public static final String XMLNAME = "Nation";
    /* property list */
    private XProperty properties = new XProperty(10);
    /* list of owned provinces */
    private XList<Province> provinces = new XList<>(Province.class);



    /**
     * Need an empty constructor for creation in fromXML in scenario, i.e.
     * loading a new scenario, where reflection newInstance() is used. (More
     * specific: see XList<E>.fromXML()).
     */
    public Nation() {
        provinces.setKeepSorted(true);
        provinces.setXMLName("Provinces");
    }

    /**
     * Currently the only way to set the name property. Do we want to set it
     * differently?
     *
     * @param name
     */
    public Nation(String name) {
        this();
        properties.put("name", name);
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
     * Adds a province. The same province is never added twice, but a log entry
     * is written in this case.
     *
     * @param province
     */
    // TODO only give title?
    public void addProvince(Province province) {
        // TODO check if already contained
        provinces.addElement(province);
    }

    /**
     * Sets the id of the capital province. If this province does not belong to
     * the nation nothing is done (but a log entry is written).
     *
     * @param province 
     */
    // TODO somehow select differently?
    public void setCapitalProvince(Province province) {
            properties.putInt("capital province", province.getID());
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
    public Element toXML() {
        Element element = new Element(XMLNAME);

        element.appendChild(properties.toXML());
        element.appendChild(provinces.toXML());

        return element;
    }

    /**
     * Import from XML.
     *
     * @param parent xml object
     */
    @Override
    public void fromXML(Element parent) {
        Elements children = parent.getChildElements();
        // TODO checks (null, name)

        properties.fromXML(children.get(0));
        provinces.fromXML(children.get(1));
        // TODO get children by name instead of fixed indices
    }
}
