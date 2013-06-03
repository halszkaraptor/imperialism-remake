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
package org.iremake.common.model;

import org.tools.xml.Node;
import org.tools.xml.ReadXMLable;
import org.tools.xml.XProperty;

/**
 *
 */
// TODO exception handling
public class ScenarioTitleProvider implements ReadXMLable {
    
    String title;
    
    public String getTitle() {
        return title;
    }

    @Override
    public void fromXML(Node parent) {
        
        parent.checkNode(ServerScenario.XML_NAME);
        
        XProperty properties = new XProperty(10);
        properties.fromXML(parent.getFirstChild(XProperty.XML_NAME));
        title = properties.get("title");
    }
    
}
