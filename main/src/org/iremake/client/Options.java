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
public enum Options {

    Version("version"),
    FullScreenMode("graphics.mode.fullscreen");
    private static XProperty options = new XProperty(0);
    private String name;

    Options(String id) {
        this.name = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String get() {
        return options.get(name);
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
            name = "options.default.xml"; // TODO this file doesn't exist yet

            // TODO this should be deleted and replaced by a XML property editor
            options.put("version", "0.1.1");
            options.putBoolean("graphics.mode.fullscreen", true);
        }

        IOManager.setFromXML(Places.Common, name, options);

        StartClient.fullscreen = Options.FullScreenMode.getBoolean();
    }

    public static void save() {
        IOManager.saveToXML(Places.Common, "options.xml", options);
    }
}
