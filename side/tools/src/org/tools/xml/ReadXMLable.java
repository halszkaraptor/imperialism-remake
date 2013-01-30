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
package org.tools.xml;

import nu.xom.Element;

/**
 * That's all we require from any class. That it knows how to read its state
 * from a XML node and how to save its state into a XML node. If necessary this
 * property can be nested, i.e. part of it is XMLAble itself.
 */
public interface ReadXMLable {

    /**
     * Sets the current state of an object according to the content of the given
     * XML node.
     *
     * @param parent a xml node
     */
    public void fromXML(Element parent);
}