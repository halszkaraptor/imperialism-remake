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
 * A nation has an id (int) and a name and a color.
 */
public class Nation implements XMLable {

    private final int id;
    private final String name;
    private final String colorcode;

    /**
     * Set from values.
     *
     * @param id
     * @param name
     * @param colorcode
     */
    public Nation(int id, String name, String colorcode) {
        this.id = id;
        this.name = name;
        this.colorcode = colorcode;
    }

    /**
     * String representation is always the name. Allows easy usage in UI
     * elements.
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
    private static final String NAME = "Nation";

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
        element.addAttribute(new Attribute("colorcode", colorcode));

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
