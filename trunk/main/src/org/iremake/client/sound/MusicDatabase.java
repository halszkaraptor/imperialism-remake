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
import nu.xom.Element;
import nu.xom.Elements;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.io.Resource;
import org.tools.xml.ReadXMLable;

/**
 *
 */
public class MusicDatabase implements ReadXMLable {

    private List<Resource> list;

    public List<Resource> getBackgroundMusicList() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public void fromXML(Element parent) {
        if (parent == null || parent.getLocalName().equals("Music")) {

        }

        Element child = parent.getFirstChildElement("Background");
        if (child == null) {

        }
        String base = child.getAttributeValue("base");

        Elements children = child.getChildElements();
        list = new ArrayList<>(children.size());
        for (int i = 0; i < children.size(); i++) {
            Element piece = children.get(i);

            if (!"Piece".equals(child.getLocalName())) {
                // TODO something is wrong
            }
            Resource resource = IOManager.getAsResource(Places.Music, base + "/" + piece.getValue());
            list.add(resource);
        }
    }

}
