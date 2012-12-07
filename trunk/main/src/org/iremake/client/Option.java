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

import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.tools.xml.common.XProperty;

/**
 *
 */
// TODO server and client options separated?, mayb common options with version number ... too?
public enum Option {

    Version("version"),
    FullScreenMode("graphics.mode.fullscreen"),
    MainScreenControlsRight("graphics.mainscreen.controlsright"),
    NetworkAlias("client.network.alias");

    public static boolean isOSWindows = System.getProperty("os.name", "generic").toLowerCase().startsWith("windows");

    private static XProperty options = new XProperty(0);
    private String name;

    Option(String id) {
        this.name = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String get() {
        return options.get(name);
    }

    public void put(String value) {
        options.put(name, value);
    }

    public boolean getBoolean() {
        return options.getBoolean(name);
    }

    public void putBoolean(boolean value) {
        options.putBoolean(name, value);
    }

    public static void load() {
        // either load the options or the default options
        String name = "options.xml";
        if (IOManager.exists(Places.Common, name) == false) {
            IOManager.setFromXML(Places.Common, "options.default.xml", options);
            // some first time initializations
            Option.NetworkAlias.put(System.getProperty("user.name"));
        } else {
            IOManager.setFromXML(Places.Common, name, options);
        }
    }

    public static void save() {
        IOManager.saveToXML(Places.Common, "options.xml", options);
    }
}
