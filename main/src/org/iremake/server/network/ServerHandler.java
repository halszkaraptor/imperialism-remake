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

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.util.HashMap;
import java.util.Map;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;

/**
 * Handling of the connected clients and the incoming message on the server
 * side. Each message is bound to a connection with a unique integer ID. We will
 * store a map: id -> client and forward each incoming Message accordingly.
 */
// TODO in case disconnections appear, we have to get an intermediate layer allowing reconnections
public class ServerHandler extends Listener {

    private static final int DISCONNECT_REMOVAL_DELAY = 10000;
    private Map<Integer, ServerClientHandler> clients = new HashMap<>();

    /**
     * A new connection arrived. Accept into the list and create a new client
     * handler.
     *
     * @param connection the new connection.
     */
    @Override
    public void connected(Connection connection) {
        ServerLogger.log("Connection from " + connection.getRemoteAddressTCP());
        clients.put(connection.getID(), new ServerClientHandler(connection, this));
    }

    /**
     * A connection disconnected, remove immediately.
     *
     * @param connection the disconnected connection
     */
    @Override
    public void disconnected(Connection connection) {
        Integer id = connection.getID();
        ServerLogger.log("Disconnected ID " + id);
        clients.remove(id);
    }

    /**
     * A message has been received. First check for type Message, then delegate
     * to the server client associated with this connection.
     *
     * @param connection the connection
     * @param object the received object
     */
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Message) {
            ServerLogger.log("Message arrived: " + object.getClass().getSimpleName());
            Integer id = connection.getID();
            if (clients.containsKey(id)) {
                clients.get(id).consume((Message) object);
            } else {
                // TODO this shouldn't happen, log
            }
        } else {
            // TODO log unexpected types of objects, should normally not happen
        }
    }

    /**
     * A certain connection shall be disconnected as a decision on our side, we
     * send a message before disconnecting.
     *
     * @param connection the connection
     * @param message the farewell message
     */
    void disconnect(Connection connection, TextMessage message) {
        clients.remove(connection.getID());
        connection.sendTCP(message);
        connection.close(); // TODO does it also occur in disconnected?
    }
}
