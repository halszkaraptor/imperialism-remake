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
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.network.handler.ClientHandler;
import org.iremake.common.Settings;
import org.iremake.common.network.messages.KryoRegistration;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.MessageContainer;

/**
 * Fires up network connection for the client.
 */
public class RemoteClient extends Listener implements ClientContext {

    /* Timeout in ms for connection */
    private static final Logger LOG = Logger.getLogger(RemoteClient.class.getName());

    public static final ClientContext CONTEXT = new RemoteClient();

    /* Kryonet client */
    private Client kryoClient;
    /* The only instance */
    /**
     *
     */

    private List<ClientHandler> handlerList = new LinkedList<>();
    private ExecutorService threadPool;
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
    @Override
    public boolean start(String host) {
        LOG.log(Level.INFO, "[CLIENT] Client connection initiated.");
        if (kryoClient != null) {
            return false;
        }

        kryoClient = new Client();

        KryoRegistration.register(kryoClient.getKryo());

        kryoClient.start();
        kryoClient.addListener(this);

        try {
            kryoClient.connect(5_000, host, Settings.NETWORK_PORT);
        } catch (IOException ex) {
            // LOG.log(Level.SEVERE, null, ex);
            LOG.log(Level.SEVERE, "[CLIENT] Client could not connect.");
            stop();
            return false;
        }
        LOG.log(Level.INFO, "[CLIENT] Client connection successful.");

        return true;
    }

    /**
     * Send message.
     *
     * @param message
     */
    // TODO do we need this here?
    @Override
    public void send(MessageContainer message) {
        if (kryoClient != null && kryoClient.isConnected()) {
            LOG.log(Level.INFO, "[CLIENT] Send message of type {0}", message.getType().name());
            kryoClient.sendTCP(message);
        }
    }

    /**
     * Stops the client.
     */
    @Override
    public void stop() {
        // TODO what happens to operations being sent?
        if (kryoClient != null) {
            LOG.log(Level.INFO, "[CLIENT] Client will shutdown.");
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
            return String.format("[CLIENT] Connected to server at %s.", kryoClient.getRemoteAddressTCP().getHostString());
        } else {
            return "[CLIENT] Not connected.";
        }
    }

    /**
     * @return True if running.
     */
    @Override
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
            LOG.log(Level.INFO, "[CLIENT] We want to disconnect.");
            if (error != null) {
                send(Message.GEN_ERROR.createNew(error));
            }
            kryoClient.close();
        }
    }

    /**
     *
     * @param channel
     * @param handler
     */
    @Override
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
    @Override
    public boolean removeHandler(ClientHandler handler) {
        return handlerList.remove(handler);
    }

    /**
     * Sends to all handler in a channel until someone processed it (returns
     * true).
     *
     * @param message
     */
    @Override
    public void process(MessageContainer message) {

       for (ClientHandler handler : handlerList) {
            if (handler.process(message, this)) {
                break;
            }
        }
    }

    /**
     * We connected to the server.
     *
     * @param connection Can be ignored, we already know the connection from the
     * manager.
     */
    @Override
    public void connected(Connection connection) {
        LOG.log(Level.INFO, "[CLIENT] Client has connected.");
        // exactly one thread, so processing will be one by one
        threadPool = Executors.newFixedThreadPool(1);
    }

    /**
     * We disconnected from the server. Either the server disconnected us or we
     * disconnected from him.
     *
     * @param connection Can be ignored, is also stored in the context.
     */
    @Override
    public void disconnected(Connection connection) {
        LOG.log(Level.INFO, "[CLIENT] Our client was/has disconnected.");
        // TODO either we or somebody else disconnected, do something or tell somebody about it
        threadPool.shutdown();
    }

    /**
     * We received a message from the server, first we make sure it's of type
     * Message, then we schedule processing of this message in the
     * ExecutorService.
     *
     * @param connection Used connection
     * @param object Incoming object (message)
     */
    @Override
    public void received(Connection connection, final Object object) {
        if (connection.isConnected() && object instanceof MessageContainer) {
            final MessageContainer message = (MessageContainer) object;
            LOG.log(Level.INFO, "[CLIENT] Received message of type {0}", message.getType().name());
            // schedule for processing
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    process(message);
                }
            });
        }
        // if it was another message, it could have been a keepalive message from the framework
    }
}
