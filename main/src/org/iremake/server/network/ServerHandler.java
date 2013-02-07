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
package org.iremake.server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.ConnectedClient;
import org.iremake.common.network.NetworkContext;
import org.iremake.common.network.NodeContext;
import org.iremake.common.network.handler.ErrorHandler;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.TextMessageType;
import org.iremake.server.network.handler.ClientNameHandler;
import org.iremake.server.network.handler.VerifyVersionHandler;

/**
 *
 */
public class ServerHandler extends Listener implements NetworkContext {

    private static final Logger LOG = Logger.getLogger(ServerHandler.class.getName());
    private static final int MAX_CLIENTS = 100;
    private Map<Integer, ConnectedClient> map = new HashMap<>(10);

    @Override
    public void connected(Connection connection) {
        if (map.size() >= MAX_CLIENTS) {
            connection.sendTCP(TextMessageType.Error.create(String.format("Too many connected clients. Max = %d", MAX_CLIENTS)));
            connection.close();
        } else {
            // initial handler chain for every connected client
            NodeContext root = new NodeContext(new ErrorHandler(), this, connection.getID());
            root.add(new VerifyVersionHandler()).add(new ClientNameHandler());
            ConnectedClient client = new ConnectedClient(root, connection);
            map.put(connection.getID(), client);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        LOG.log(Level.FINE, "Connection {0} disconnected.", connection.getID());
        // whether it's there or not, we remove it
        map.get(connection.getID()).shutdown();
        map.remove(connection.getID());
    }

    @Override
    public void received(Connection connection, Object object) {
        if (connection.isConnected() && object instanceof Message) {
            Message message = (Message) object;
            LOG.log(Level.FINER, "Received message: {0}", message.toString());
            Integer id = connection.getID();
            if (!map.containsKey(id)) {
                throw new RuntimeException("connection not registered. internal error.");
            }
            map.get(id).process(message);
        } else {
            connection.close();
        }
    }

    @Override
    public void disconnect(Integer id, TextMessage error) {
        map.get(id).disconnect(error);
    }

    @Override
    public String name(Integer id) {
        return map.get(id).name();
    }

    @Override
    public void send(Integer id, Message message) {
        map.get(id).send(message);
    }

    @Override
    public void broadcast(Message message) {
        for (ConnectedClient client: map.values()) {
            client.send(message);
        }
    }
}
