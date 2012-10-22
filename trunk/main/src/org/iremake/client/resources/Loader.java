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
package org.iremake.client.resources;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import nu.xom.Element;
import org.tools.xml.XMLHelper;

/**
 * Loads from within the application jar file.
 */
// TODO maybe some kind of intelligent caching (what is how often loaded,...)
public class Loader {

    private static final Logger LOG = Logger.getLogger(Loader.class.getName());
    // private static final String base = Loader.class.getProtectionDomain().getCodeSource().getLocation().getPath(); // path of jar file
    private static final String base = System.getProperty("user.dir") + File.separator; // starting from within NetBeans

    private Loader() {
    }

    /**
     *
     * @param place
     * @param location
     * @return
     */
    public static Icon getAsIcon(Places place, String location) {
        String path = base + place + location;
        ImageIcon icon = new ImageIcon(path);
        if (icon == null) {
            // TODO log entry
        }
        return icon;
    }

    /**
     *
     * @param place
     * @param location
     * @return
     */
    public static Image getAsImage(Places place, String location) {
        String path = base + place + location;
        ImageIcon icon = new ImageIcon(path);
        if (icon == null) {
            // TODO log entry
            return null;
        }
        return icon.getImage();
    }

    /**
     *
     * @param place
     * @param location
     * @return
     */
    public static Element getAsXML(Places place, String location) {
        String path = base + place + location;
        return XMLHelper.read(path);
    }

    public static InputStream getAsInputStream(Places place, String location) throws FileNotFoundException {
        String path = base + place + location;
        return new FileInputStream(path);
    }

    public static String getPath(String location) {
        // TODO replace folder separator symbol in path ??
        return base + location;
    }

    public static URL getURL(Places place, String location) {
        String path = base + place + location;
        try {
            return new File(path).toURI().toURL();
        } catch (MalformedURLException ex) {
            LOG.log(Level.INFO, "Could not obtain URL.", ex);
            return null;
        }
    }

    public static boolean createDirectory(String location) {
        File file = new File(base + location);
        return file.mkdirs();
    }
}
