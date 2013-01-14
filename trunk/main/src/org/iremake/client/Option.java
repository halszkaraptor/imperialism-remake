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
package org.iremake.client;

import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.xml.XProperty;

/**
 * Global options for the whole application, stored in an XML file and accessed
 * within the program by enum fields. The fields contain a keyword that is used
 * to store the values.
 */
// TODO server and client options separated?, mayb common options with version number ... too?
public enum Option {

    // all options
    Version("version"),
    FullScreenMode("graphics.mode.fullscreen"),
    MainScreenControlsRight("graphics.mainscreen.controlsright"),
    NetworkAlias("client.network.alias"); // this option does not need to be existing in the default options xml file, it will be added automatically

    /* OS name */
    public static boolean isOSWindows = System.getProperty("os.name", "generic").toLowerCase().startsWith("windows");
    /* Holds the options */
    private static XProperty options = new XProperty(20);
    /* Option name */
    private String name;

    /**
     * Store the keyword.
     *
     * @param id
     */
    Option(String id) {
        this.name = id;
    }

    /**
     * Return content of the option as a String.
     *
     * @return value of the option.
     */
    public String get() {
        return options.get(name);
    }

    /**
     * Change content of an option.
     *
     * @param value new value of the option
     */
    public void put(String value) {
        options.put(name, value);
    }

    /**
     * Return as a boolean.
     *
     * @return boolean value of the option
     */
    public boolean getBoolean() {
        return options.getBoolean(name);
    }

    /**
     * Change content as a boolean
     *
     * @param value new boolean value of the option
     */
    public void putBoolean(boolean value) {
        options.putBoolean(name, value);
    }

    /**
     * Loads options from known location, if not existing loads a
     * default.options copy. If even this file is not existing, we are in
     * trouble.
     */
    public static void load() {
        // either load the options or the default options
        String name = "options.xml";
        if (IOManager.exists(Places.Common, name) == false) {
            // TODo what if this is not existing?
            IOManager.setFromXML(Places.Common, "options.default.xml", options);
            // some first time initializations
            Option.NetworkAlias.put(System.getProperty("user.name"));
        } else {
            IOManager.setFromXML(Places.Common, name, options);
        }
    }

    /**
     * Save options to xml.
     */
    public static void save() {
        IOManager.saveToXML(Places.Common, "options.xml", options);
    }
}
