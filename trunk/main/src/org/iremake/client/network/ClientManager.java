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
package org.iremake.client.network;

import com.esotericsoftware.kryonet.Client;
import java.io.IOException;
import java.util.logging.Logger;
import org.iremake.common.Settings;
import org.iremake.common.network.messages.KryoRegistration;
import org.iremake.common.network.messages.Message;

/**
 * Fires up network connection for the client.
 */
public class ClientManager {

    /* Timeout in ms for connection */
    private static final int TIMEOUT = 5000;
    private static final Logger LOG = Logger.getLogger(ClientManager.class.getName());
    /* Kryonet client */
    private Client client;

    /**
     * Connects to server.
     *
     * @return
     */
    public boolean start() {
        ClientLogger.log("Client start initiated.");
        if (client != null) {
            return false;
        }

        client = new Client();

        KryoRegistration.register(client.getKryo());

        client.start();

        try {
            client.connect(TIMEOUT, "localhost", Settings.NETWORK_PORT);
        } catch (IOException ex) {
            // LOG.log(Level.SEVERE, null, ex);
            ClientLogger.log("Could not connect.");

            stop();

            return false;
        }

        ClientLogger.log("Connected.");

        client.addListener(new ClientHandler());

        return true;
    }

    /**
     * Send message.
     *
     * @param message
     */
    // TODO do we need this here?
    public void send(Message message) {
        if (client != null && client.isConnected()) {
            ClientLogger.log("Send message: " + message.getClass().getSimpleName());
            client.sendTCP(message);
        }
    }

    /**
     * Stops the client.
     */
    public void stop() {
        // TODO what happens to operations being sent
        if (client != null) {
            ClientLogger.log("Will stop.");
            client.stop();
            client = null;
        }
    }
}
