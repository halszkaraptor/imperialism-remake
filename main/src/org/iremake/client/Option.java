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

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.xml.XProperty;

/**
 * Global options for the whole application, stored in an XML file and accessed
 * within the program by enum fields. The fields contain a keyword that is used
 * to store the values.
 */
// TODO separate server and client options?, mayb common options with version number ... too?
public enum Option {

    // general options
    General_Version("general.version"),
    // graphics options
    Graphics_FullScreenMode("graphics.mode.fullscreen"),
    Graphics_MainScreenControlsRight("graphics.mainscreen.controlsright"),
    // music options
    Music_Mute("music.mute"),
    // client options
    Client_Alias("client.network.alias"); // this option does not need to be existing in the default options xml file, it will be added automatically

    private static final Logger LOG = Logger.getLogger(Option.class.getName());
    /* OS name */
    public static final boolean isOSWindows = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).startsWith("windows");
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

    @Override
    public String toString() {
        return name;
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
            Option.Client_Alias.put(System.getProperty("user.name"));
        } else {
            IOManager.setFromXML(Places.Common, name, options);
        }
        // TODO no option should be null, test
        LOG.log(Level.INFO, "Running version {0}", General_Version.get());
    }

    /**
     * Save options to xml.
     */
    public static void save() {
        IOManager.saveToXML(Places.Common, "options.xml", options);
    }
}
