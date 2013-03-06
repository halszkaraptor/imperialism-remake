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
package org.iremake.client.sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.io.Resource;
import org.tools.xml.Node;
import org.tools.xml.ReadXMLable;

/**
 * Loads information about our music pieces from file. Just reading supported.
 */
public class MusicDatabase implements ReadXMLable {

    private static final Logger LOG = Logger.getLogger(MusicDatabase.class.getName());
    private static final String XML_NAME = "Music";
    private List<Resource> list;

    /**
     * Get background music list.
     * @return
     */
    public List<Resource> getBackgroundMusicList() {
        return Collections.unmodifiableList(list);
    }

    /**
     * Loads from XML.
     *
     * @param parent The XML node.
     */
    @Override
    public void fromXML(Node parent) {
        parent.checkNode(XML_NAME);

        Node node = parent.getFirstChild("Background");

        String base = node.getAttributeValue("base");

        list = new ArrayList<>(node.getChildCount());
        for (Node child: node.getChildren()) {
            child.checkNode("Piece");

            Resource resource = IOManager.getAsResource(Places.Music, base + "/" + child.getValue());
            list.add(resource);
        }
    }
}
