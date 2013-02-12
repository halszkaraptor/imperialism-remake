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
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.Option;
import org.iremake.client.network.handler.ErrorHandler;
import org.iremake.common.Settings;
import org.iremake.common.network.messages.KryoRegistration;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.TextMessageType;

/**
 * Fires up network connection for the client.
 */
public class ClientManager implements ClientContext {

    /* Timeout in ms for connection */
    private static final int TIMEOUT = 5000;
    private static final Logger LOG = Logger.getLogger(ClientManager.class.getName());
    /* Kryonet client */
    private Client client;
    private ClientNodeContext root;

    /**
     * Connects to server.
     *
     * @param host
     * @return
     */
    public boolean start(String host) {
        LOG.log(Level.FINE, "Client connection initiated.");
        if (client != null) {
            return false;
        }

        client = new Client();

        KryoRegistration.register(client.getKryo());

        client.start();

        Listener listener = new ClientListener(this);
        client.addListener(listener);

        try {
            client.connect(TIMEOUT, host, Settings.NETWORK_PORT);
        } catch (IOException ex) {
            // LOG.log(Level.SEVERE, null, ex);
            LOG.log(Level.SEVERE, "Client could not connect.");
            stop();
            return false;
        }

        root = new ClientNodeContext(new ErrorHandler(), this);

        send(TextMessageType.Version.create(Option.General_Version.get()));
        send(TextMessageType.ClientName.create("client-name"));

        return true;
    }

    /**
     * Send message.
     *
     * @param message
     */
    // TODO do we need this here?
    @Override
    public void send(Message message) {
        if (client != null && client.isConnected()) {
            LOG.log(Level.FINE, "Send message: {0}", message.toString());
            client.sendTCP(message);
        }
    }

    /**
     * Stops the client.
     */
    public void stop() {
        // TODO what happens to operations being sent
        if (client != null) {
            LOG.log(Level.FINE, "Will stop.");
            client.close();
            client.stop();
            client = null;
        }
    }

    public String getStatus() {
        if (client != null) {
            return String.format("Connected to server at %s.", client.getRemoteAddressTCP().getHostString());
        } else {
            return "Not connected.";
        }
    }

    public boolean isRunning() {
        return client != null;
    }

    @Override
    public void disconnect(TextMessage error) {
        if (client != null) {
            if (error != null) {
                send(error);
            }
            client.close();
        }
    }

    @Override
    public void process(Message message) {
        root.process(message);
    }
}
