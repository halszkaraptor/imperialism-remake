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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.network.handler.ClientHandler;
import org.iremake.client.network.handler.ErrorHandler;
import org.iremake.common.Settings;
import org.iremake.common.network.messages.ErrorMessage;
import org.iremake.common.network.messages.KryoRegistration;
import org.iremake.common.network.messages.Message;

/**
 * Fires up network connection for the client.
 */
public class RemoteClient implements ClientContext {

    /* Timeout in ms for connection */
    private static final Logger LOG = Logger.getLogger(RemoteClient.class.getName());
    /* Kryonet client */
    private Client kryoClient;
    /* The only instance */
    public static final RemoteClient INSTANCE = new RemoteClient();

    /**
     * Avoid instantiation.
     */
    private RemoteClient() {
    }

    /**
     * Connects to server.
     *
     * @param host
     * @return
     */
    public boolean start(String host) {
        LOG.log(Level.FINE, "Client connection initiated.");
        if (kryoClient != null) {
            return false;
        }

        kryoClient = new Client();

        KryoRegistration.register(kryoClient.getKryo());

        kryoClient.start();

        Listener listener = new ClientListener(this);
        kryoClient.addListener(listener);

        try {
            kryoClient.connect(60*60*1000, host, Settings.NETWORK_PORT);
        } catch (IOException ex) {
            // LOG.log(Level.SEVERE, null, ex);
            LOG.log(Level.SEVERE, "Client could not connect.");
            stop();
            return false;
        }

        return true;
    }

    /**
     * Send message.
     *
     * @param message
     */
    // TODO do we need this here?
    public void send(Message message) {
        if (kryoClient != null && kryoClient.isConnected()) {
            LOG.log(Level.FINE, "Send message: {0}", message.toString());
            kryoClient.sendTCP(message);
        }
    }

    /**
     * Stops the client.
     */
    public void stop() {
        // TODO what happens to operations being sent
        if (kryoClient != null) {
            LOG.log(Level.FINE, "Will stop.");
            kryoClient.close();
            kryoClient.stop();
            kryoClient = null;
        }
    }

    /**
     * @return A text line telling to whom we are connected
     */
    public String getStatus() {
        if (kryoClient != null) {
            return String.format("Connected to server at %s.", kryoClient.getRemoteAddressTCP().getHostString());
        } else {
            return "Not connected.";
        }
    }

    /**
     * @return True if running.
     */
    public boolean isConnected() {
        return kryoClient != null;
    }

    /**
     * Force disconnect.
     *
     * @param error Error message, if null nothing is sent.
     */
    @Override
    public void disconnect(String error) {
        if (kryoClient != null) {
            if (error != null) {
                send(new ErrorMessage(error));
            }
            kryoClient.close();
        }
    }
    private ErrorHandler errorHandler = new ErrorHandler();;
    private List<ClientHandler> handlerList = new LinkedList<>();

    /**
     *
     * @param channel
     * @param handler
     */
    public void addHandler(ClientHandler handler) {
        if (!handlerList.contains(handler)) {
            handlerList.add(handler);
        }
    }

    /**
     *
     * @param channel
     * @param handler
     * @return
     */
    public boolean removeHandler(ClientHandler handler) {
        return handlerList.remove(handler);
    }

    /**
     * Sends to all handler in a channel until someone processed it (returns
     * true).
     *
     * @param message
     */
    public void process(Message message) {

        // we always check the error handler first
        errorHandler.process(message, this);

        for (ClientHandler handler : handlerList) {
            if (handler.process(message, this)) {
                break;
            }
        }
    }
}
