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
package org.iremake.client.io;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import nu.xom.Element;
import nu.xom.ParsingException;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;
import org.tools.ui.utils.IconLoader;
import org.tools.xml.FullXMLable;
import org.tools.xml.XMLHelper;
import org.tools.xml.ReadXMLable;

/**
 * Loads from the data folder.
 *
 * The Tools library loads all resources from within their own jar. But here,
 * for easier editing and size considerations, everything is loaded from a same
 * parent folder as the executing jar folder, but with different sub directories
 * (Places).
 *
 * We always have a place (enum=subdirectory) and a location within that place.
 *
 * We have a statistics feature, so we can test if resources were requested
 * which aren't existing or to see which existing resources weren't requested.
 */
// TODO maybe some kind of intelligent caching (what is how often loaded,...)
public class IOManager {

    /* keeping track of which resource was asked for */
    private static Set<String> statistics = new HashSet<>(10);
    /* a file chooser for loading and saving */
    private static JFileChooser fileChooser;
    private static final Logger LOG = Logger.getLogger(IOManager.class.getName());

    /* path to the folder where the jar resides */
    private static final String base = "";
    // private static final String base = (new File(Loader.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParent() + File.separator;
    // private static final String base = System.getProperty("user.dir") + File.separator; // starting from within NetBeans

    /**
     * No instantiation.
     */
    private IOManager() {
    }

    /**
     * Obtains the file chooser. Uses lazy initialization.
     *
     * @return
     */
    public static JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser(IOManager.getPath(Places.Scenarios, ""));
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || (f.getName().startsWith("scenario.") && f.getName().endsWith(".xml"));
                }

                @Override
                public String getDescription() {
                    return "Scenario files (*.xml)";
                }
            });
        }
        return fileChooser;
    }

    /**
     * Load as an Icon.
     *
     * @param place
     * @param location
     * @return
     */
    public static Icon getAsIcon(Places place, String location) {
        Image image = getAsImage(place, location);
        return new ImageIcon(image);
    }

    /**
     * Load as an Image.
     *
     * @param place
     * @param location
     * @return
     */
    public static BufferedImage getAsImage(Places place, String location) {
        Resource resource = getAsResource(place, location);
        statistics.add(resource.getPath());
        BufferedImage image = null;
        try {
            image = ImageIO.read(resource.getInputStream());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, resource.getPath(), ex);
        }
        return image;
    }

    /**
     * Loads as a XML element presentation.
     *
     * @param place
     * @param location
     * @return
     */
    public static Element getAsXML(Places place, String location) {
        try {
            return XMLHelper.read(IOManager.getAsResource(place, location));
        } catch (IOException | ParsingException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Reads directly into a XMLable variable. The variable must have been
     * created before.
     *
     * @param place
     * @param location
     * @param target
     * @return
     */
    public static boolean setFromXML(Places place, String location, ReadXMLable target) {
        try {
            XMLHelper.read(IOManager.getAsResource(place, location), target);
        } catch (IOException | ParsingException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Saves directly from a XMLable variable.
     *
     * @param place
     * @param location
     * @param target
     * @return
     */
    public static boolean saveToXML(Places place, String location, FullXMLable target) {
        try {
            XMLHelper.write(IOManager.getAsResource(place, location), target);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Obtain an InputStream. Needed for example in Font.createFont().
     *
     * @param place
     * @param location
     * @return
     */
    public static InputStream getAsInputStream(Places place, String location) {
        try {
            Resource resource = getAsResource(place, location);
            if (resource != null) {
                return resource.getInputStream();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Obtain a resource. This allows to even read from inside archives.
     *
     * @param place
     * @param location
     * @return
     */
    public static Resource getAsResource(Places place, String location) {
        String path = IOManager.getPath(place, location);
        statistics.add(path);
        try {
            return ResourceUtils.asResource(path);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returns an IconLoader hiding the getAsIcon function above used in
     * packages org.tools.xxx to bridge to the IOManager without referring to
     * it.
     *
     * @param place
     * @return
     */
    public static IconLoader getAsLoader(final Places place) {
        IconLoader loader = new IconLoader() {
            @Override
            public Icon getAsIcon(String location) {
                return IOManager.getAsIcon(place, location);
            }
        };
        return loader;
    }

    /**
     * Path construction. Place and base are supposed to end with separators
     * already.
     *
     * @param place
     * @param location
     * @return
     */
    public static String getPath(Places place, String location) {
        return base + place + location;
    }

    /**
     * Tests for existence. Only works on files though.
     *
     * @param place
     * @param location
     * @return
     */
    public static boolean exists(Places place, String location) {
        String path = IOManager.getPath(place, location);
        return (new File(path)).exists();
    }

    /**
     * The path as URL. E.g. needed in JEditorPane.setPage().
     *
     * @param place
     * @param location
     * @return
     */
    public static URL getURL(Places place, String location) {
        String path = IOManager.getPath(place, location);
        statistics.add(path);
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
    public static boolean createDirectory(Places place) {
        String path = IOManager.getPath(place, "");
        File file = new File(path);
        return file.mkdirs();
    }

    /**
     * Returns the collected statistics, an ordered list of paths that were
     * requested.
     *
     * @return
     */
    public static List<String> getStatistics() {
        List<String> stats = new ArrayList<>(statistics);
        Collections.sort(stats);
        return stats;
    }
}
