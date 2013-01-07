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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nu.xom.Element;
import nu.xom.Elements;
import org.iremake.common.model.map.items.MapItem;
import org.tools.xml.XMLHandler;
import org.tools.xml.XMLable;
import org.tools.xml.XProperty;

/**
 * A nation. It has a property list and a list of provinces.
 */
public class Nation implements XMLable {

    public static final String XMLNAME = "Nation";
    /* property list */
    private XProperty properties = new XProperty(10);
    /* list of owned provinces */
    private List<Integer> provinces = new ArrayList<>();
    /* list of engineers, ... */
    private List<MapItem> items = new ArrayList<>();;

    /**
     * Need an empty constructor for creation in fromXML in scenario, i.e.
     * loading a new scenario, where reflection newInstance() is used. (More
     * specific: see XList<E>.fromXML()).
     */
    public Nation() {
    }

    /**
     * Currently the only way to set the name property. Do we want to set it
     * differently?
     *
     * @param name
     */
    public Nation(String name) {
        properties.put("name", name);
    }

    /**
     * Returns an iterable over the provinces.
     *
     * @return the iterable
     */
    public Iterable<Integer> getProvinces() {
        return Collections.unmodifiableList(provinces);
    }

    /**
     * Adds a province. The same province is never added twice, but a log entry
     * is written in this case.
     *
     * @param id
     */
    public void addProvince(Integer id) {
        if (!provinces.contains(id)) {
            provinces.add(id);
        } else {
            // TODO log
        }
    }

    /**
     * Sets the id of the capital province. If this province does not belong to
     * the nation nothing is done (but a log entry is written).
     *
     * @param id the id of the capital province
     */
    public void setCapitalProvince(int id) {
        if (provinces.contains(id)) {
            properties.putInt("capital province", id);
        } else {
            // TODO log
        }
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
        element.appendChild(XMLHandler.fromIntegerList(provinces, "Provinces"));

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
        provinces = XMLHandler.toIntegerList(children.get(1));
    }
}
