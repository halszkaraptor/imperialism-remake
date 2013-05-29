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
package org.iremake.client.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.messages.Message;

/**
 * Kryonet Listener implementation handles connection and disconnection. Has
 * it's own ExecutorService since processing incoming messages could take awhile
 * and we want to queue incoming messages during this time. However currently we
 * will process the messages one by one, so we don't have to care about thread
 * safety within the client.
 */
public class ClientListener extends Listener {

    private static final Logger LOG = Logger.getLogger(ClientListener.class.getName());
    private final RemoteClient manager;
    private ExecutorService threadPool;

    /**
     * @param context Context to store.
     */
    public ClientListener(RemoteClient manager) {
        this.manager = manager;
    }

    /**
     * We connected to the server.
     *
     * @param connection Can be ignored, we already know the connection from the
     * manager.
     */
    @Override
    public void connected(Connection connection) {
        LOG.log(Level.FINE, "Client has connected.");
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
        LOG.log(Level.FINE, "Connection {0} disconnected.", connection.getID());
        // TODO either we or somebody else disconnected, tell somebody about it
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
        // want only type Message, otherwise shut down
        if (connection.isConnected() && object instanceof Message) {
            // schedule for processing
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    manager.process((Message) object);
                }
            });
        } else {
            connection.close();
        }
    }
}
