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
package org.iremake.network;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.Option;

/**
 *
 */
public class LocalClientServerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // setup logger
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s [%2$s]%n");
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.FINER);
        Logger.getLogger("").addHandler(handler);
        Logger.getLogger("").setLevel(Level.FINER);

        // load options
        Option.load();
    }
    private static final Logger LOG = Logger.getLogger(LocalClientServerTest.class.getName());
}
