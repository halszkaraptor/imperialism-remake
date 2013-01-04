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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.messages.KryoRegistration;

/**
 *
 */
public class ServerManager {

    private static final Logger LOG = Logger.getLogger(ServerManager.class.getName());
    private static final int PORT = 19876;
    private Server server;
    private List<ServerStatusListener> listeners = new LinkedList<>();

    public void addStatusListener(ServerStatusListener l) {
        listeners.add(l);
    }

    public void removeStatusListener(ServerStatusListener l) {
        listeners.remove(l);
    }

    private void fireStatusChanged(String message) {
        for (ServerStatusListener l : listeners) {
            l.statusUpdate(message);
        }
    }

    public boolean start() {
        ServerLogger.log("Server start initiated.");
        if (server != null) {
            return false;
        }

        server = new Server();

        KryoRegistration.register(server.getKryo());

        server.start();

        try {
            server.bind(PORT);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            ServerLogger.log("Could not start.");

            stop();

            return false;
        }

        ServerLogger.log("Bound to port.");

        server.addListener(new ServerHandler());

        InetAddress own;
        try {
            own = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, null, ex); // TODO this shouldn't happen but we have to catch it.
            return true;
        }
        fireStatusChanged("Server running at " + own.getHostAddress());

        return true;
    }

    public boolean isRunning() {
        return server != null;
    }

    public void stop() {
        if (server != null) {
            ServerLogger.log("Will stop.");
            server.stop();
            server = null;

            fireStatusChanged("Server not running.");
        } else {
            ServerLogger.log("Already stopped.");
        }
    }
}
