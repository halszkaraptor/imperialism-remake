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
package org.iremake.server.network;

import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.Settings;
import org.iremake.common.network.messages.KryoRegistration;

/**
 * Starts the server.
 */
public class ServerManager {

    private static final Logger LOG = Logger.getLogger(ServerManager.class.getName());
    /* port on which we listen to clients */
    private Server server;

    /**
     * Starts the server.
     *
     * @return
     */
    public boolean start() {
        LOG.log(Level.FINE, "Server start initiated.");
        if (server != null) {
            return false;
        }

        server = new Server();

        KryoRegistration.register(server.getKryo());

        server.start();

        try {
            server.bind(Settings.NETWORK_PORT);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            LOG.log(Level.INFO, "Could not start.");

            stop();

            return false;
        }

        LOG.log(Level.FINE, "Bound to port.");

        server.addListener(new ServerListener());

        InetAddress own;
        try {
            own = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, null, ex); // TODO this shouldn't happen but we have to catch it.
            return true;
        }
        // ("Server running at " + own.getHostAddress());

        return true;
    }

    /**
     * Tells whether the server has been started.
     *
     * @return True if it is running
     */
    public boolean isRunning() {
        return server != null;
    }

    /**
     * Stops the server.
     */
    public void stop() {
        if (server != null) {
            LOG.log(Level.FINE, "Will stop.");
            server.stop();
            server = null;

            // fireStatusChanged("Server not running.");
        } else {
            LOG.log(Level.FINE, "Already stopped.");
        }
    }
}
