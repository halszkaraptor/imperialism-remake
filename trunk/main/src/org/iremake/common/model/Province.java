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

import nu.xom.Attribute;
import nu.xom.Element;
import org.tools.xml.XMLable;

/**
 * A province has an id (int) and a name.
 */
public class Province implements XMLable {

    private int id;
    private String name;

    /**
     * String representation is always the name, makes it easy to use in UI
     * elements.
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
    public static final String NAME = "Province";

    /**
     * Export to XML.
     *
     * @return
     */
    @Override
    public Element toXML() {
        Element element = new Element(NAME);
        element.addAttribute(new Attribute("id", String.valueOf(id)));
        element.addAttribute(new Attribute("name", name));

        return element;
    }

    /**
     * Import from XML.
     *
     * @param parent
     */
    @Override
    public void fromXML(Element parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
