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
import org.iremake.common.network.messages.Channel;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.TextMessageType;
import org.iremake.server.network.handler.ClientNameHandler;
import org.iremake.server.network.handler.ErrorHandler;
import org.iremake.server.network.handler.VerifyVersionHandler;

/**
 *
 */
public class ServerHandler extends Listener implements ServerContext {

    private static final Logger LOG = Logger.getLogger(ServerHandler.class.getName());
    private static final int MAX_CLIENTS = 100;
    private Map<Integer, ServerConnectedClient> map = new HashMap<>(10);

    @Override
    public void connected(Connection connection) {
        if (map.size() >= MAX_CLIENTS) {
            connection.sendTCP(new TextMessage(TextMessageType.Error, String.format("Too many connected clients. Max = %d", MAX_CLIENTS), Channel.ERROR));
            connection.close();
        } else {
            // initial handler chain for every connected client
            ServerNodeContext root = new ServerNodeContext(new ErrorHandler(), this, connection.getID());
            root.add(new VerifyVersionHandler()).add(new ClientNameHandler());
            ServerConnectedClient client = new ServerConnectedClient(root, connection);
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
    public String getName(Integer id) {
        return map.get(id).getName();
    }

    @Override
    public void send(Integer id, Message message) {
        map.get(id).send(message);
    }

    @Override
    public void broadcast(Message message) {
        for (ServerConnectedClient client: map.values()) {
            client.send(message);
        }
    }

    @Override
    public void broadcast(String domain, Message message) {
        for (ServerConnectedClient client: map.values()) {
            if (client.hasHandler(domain)) {
                client.send(message);
            }
        }
    }

    @Override
    public void setName(Integer id, String name) {
        map.get(id).setName(name);
    }


}
