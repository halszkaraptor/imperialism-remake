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
package org.iremake.common.resources;

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
import org.iremake.common.xml.XMLHelper;

/**
 * Loads from the data folder.
 *
 * The Tools library loads all resources from within their own jar. But here,
 * for easier editing and size considerations, everything is loaded from a same
 * parent folder as the executing jar folder, but with different sub directories
 * (Places).
 */
// TODO maybe some kind of intelligent caching (what is how often loaded,...)
public class Loader {

    private static final Logger LOG = Logger.getLogger(Loader.class.getName());
    private static final String base = "";
    // private static final String base = (new File(Loader.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParent() + File.separator;
    // private static final String base = System.getProperty("user.dir") + File.separator; // starting from within NetBeans

    /**
     * No instantiation.
     */
    private Loader() {
    }

    /**
     * Load as an Icon.
     *
     * @param place
     * @param location
     * @return
     */
    public static Icon getAsIcon(Places place, String location) {
        String path = base + place + location;
        ImageIcon icon = new ImageIcon(path);
        // ImageIcon waits automatically, no ImageObserver needed
        if (icon == null) {
            LOG.log(Level.INFO, "Image {0} not readable.", path);
        }
        return icon;
    }

    /**
     * Load as an Image.
     *
     * @param place
     * @param location
     * @return
     */
    public static Image getAsImage(Places place, String location) {
        String path = base + place + location;
        ImageIcon icon = new ImageIcon(path);
        if (icon == null) {
            LOG.log(Level.INFO, "Image {0} not readable.", path);
            return null;
        }
        return icon.getImage();
    }

    /**
     * Load from an XML file.
     *
     * @param place
     * @param location
     * @return
     */
    public static Element getAsXML(Places place, String location) {
        String path = base + place + location;
        return XMLHelper.read(path);
    }

    /**
     * Just an InputStream from a file.
     *
     * @param place
     * @param location
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getAsInputStream(Places place, String location) throws FileNotFoundException {
        String path = base + place + location;
        return new FileInputStream(path);
    }

    /**
     * Just the path.
     *
     * @param place
     * @param location
     * @return
     */
    public static String getPath(Places place, String location) {
        // TODO replace folder separator symbol in path ??
        return base + place + location;
    }

    /**
     *
     * @param place
     * @param location
     * @return
     */
    public static boolean exists(Places place, String location) {
        String path = base + place + location;
        return (new File(path)).exists();
    }

    /**
     * The path as URL.
     *
     * @param place
     * @param location
     * @return
     */
    public static URL getURL(Places place, String location) {
        String path = base + place + location;
        try {
            return new File(path).toURI().toURL();
        } catch (MalformedURLException ex) {
            LOG.log(Level.INFO, "Could not obtain URL.", ex);
            return null;
        }
    }

    /**
     * If not existent, we sometimes have to create directories.
     *
     * @param location
     * @return
     */
    public static boolean createDirectory(String location) {
        File file = new File(base + location);
        return file.mkdirs();
    }
}
