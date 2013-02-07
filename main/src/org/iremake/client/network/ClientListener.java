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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.ConnectedClient;
import org.iremake.common.network.NetworkContext;
import org.iremake.common.network.NodeContext;
import org.iremake.common.network.handler.ErrorHandler;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;

/**
 *
 */
public class ClientListener extends Listener implements NetworkContext {

    private static final Logger LOG = Logger.getLogger(ClientListener.class.getName());
    private ConnectedClient client;

    @Override
    public void connected(Connection connection) {
        LOG.log(Level.FINE, "Client has connected.");
        NodeContext root = new NodeContext(new ErrorHandler(), this, connection.getID());
        client = new ConnectedClient(root, connection);
    }

    @Override
    public void disconnected(Connection connection) {
        LOG.log(Level.FINE, "Connection {0} disconnected.", connection.getID());
        // TODO either we or somebody else disconnected
        client.shutdown();
        client = null;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (connection.isConnected() && object instanceof Message) {
            client.process((Message) object);
        } else {
            connection.close();
        }
    }

    @Override
    public void disconnect(Integer id, TextMessage error) {
        client.disconnect(error);
    }

    @Override
    public String name(Integer id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void send(Integer id, Message message) {
        client.send(message);
    }

    @Override
    public void broadcast(Message message) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
