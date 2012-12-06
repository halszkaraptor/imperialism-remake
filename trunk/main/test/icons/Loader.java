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
package icons;

import java.awt.Image;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 */
public class Loader {

    /**
     * Loads as ImageIcon.
     *
     * @param location
     * @return
     */
    public static Icon getAsIcon(String location) {
        URL url = getAsURL(location);
        if (url == null) {
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        if (icon == null) {
            return null;
        }
        return icon;
    }

    /**
     * Loads as ImageIcon, then returns the Image.
     *
     * @param location
     * @return
     */
    public static Image getAsImage(String location) {
        URL url = getAsURL(location);
        if (url == null) {
            return null;
        }
        ImageIcon icon = new ImageIcon(url);
        if (icon == null) {
            return null;
        }
        return icon.getImage();
    }

    /**
     * Just returns the URl to inside the jar file.
     *
     * @param location
     * @return
     */
    public static URL getAsURL(String location) {
        return Loader.class.getResource(location);
    }
}
