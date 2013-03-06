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

/**
 * Interface for classes being able to read its state from XML. The idea is to
 * read all children (also of type ReadXMLable) recursively.
 */
public interface ReadXMLable {

    /**
     * Sets the current state of an object according to the content of the given
     * XML node.
     *
     * You can trust that Node parent is never null. Such cases will be caught
     * before by convention.
     *
     * @param parent a xml node
     */
    public void fromXML(Node parent);
}