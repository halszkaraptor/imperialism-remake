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
package org.iremake.client.utils;

import java.awt.Image;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Loads from within the application jar file.
 */
public class Resources {

    private static final String ui_base = "/data/game/artwork/graphics/ui/";

    private Resources() {
    }

    public static Icon getAsIcon(String resource) {
        URL url = Resources.getAsURL(resource);
        ImageIcon icon = null;
        if (url != null) {
            icon = new ImageIcon(url);
        } else {
            // TODO log entry
        }
        return icon;
    }

    public static Image getAsImage(String resource) {
        URL url = Resources.getAsURL(resource);
        Image image = null;
        if (url != null) {
            image = new ImageIcon(url).getImage();
        } else {
            // TODO log entry
        }
        return image;
    }

    /**
     *
     * @param resource
     * @return The URL or null if not existing.
     */
    public static URL getAsURL(String resource) {
        return Resources.class.getResource(resource);
    }

    public static String fromUI(String name) {
        return ui_base + name;
    }
}
