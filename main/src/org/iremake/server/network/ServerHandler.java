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
import org.iremake.common.network.NetworkLogger;
import org.iremake.common.network.messages.Message;

/**
 * Handling of received messages on the server side.
 */
public class ServerHandler extends Listener {

    private Map<Connection, ServerClientHandler> handlers = new HashMap<>();
    private NetworkLogger logger;

    public ServerHandler(NetworkLogger logger) {
        this.logger = logger;
    }

    @Override
    public void connected(Connection connection) {
        logger.log("Connection from " + connection.getRemoteAddressTCP());
        handlers.put(connection, new ServerClientHandler(connection, logger));
    }

    @Override
    public void disconnected(Connection connection) {
        logger.log("Disconnection"); // TODO which one
        handlers.get(connection).setState(ClientState.Disconnected);
        // TODO from time to time check and delete all disconnected ones
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Message) {
            logger.log("Message arrived: " + object.getClass().getSimpleName());
            handlers.get(connection).received((Message) object);
        } else {
            // TODO log unexpected types of objects, should normally not happen
        }
    }
}
