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
import java.util.List;
import nu.xom.Element;
import nu.xom.Elements;
import org.tools.xml.XMLHandler;
import org.tools.xml.XMLable;
import org.tools.xml.XProperty;

/**
 * A nation.
 */
public class Nation implements XMLable {

    public static final String XMLNAME = "Nation";

    private XProperty properties = new XProperty(10);
    private List<Integer> provinces = new ArrayList<>();

    public Nation() {
    }

    public Nation(String name) {
        properties.put("name", name);
    }

    /**
     * Export to XML.
     *
     * @return
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
     * @param parent
     */
    @Override
    public void fromXML(Element parent) {
        Elements children = parent.getChildElements();
        // TODO checks (null, name)

        properties.fromXML(children.get(0));
        provinces = XMLHandler.toIntegerList(children.get(1));
    }

    @Override
    public String toString() {
        return properties.get("name");
    }

    Iterable<Integer> getProvinces() {
        return provinces;
    }

    void addProvince(Integer id) {
        provinces.add(id);
    }
}
