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
import org.iremake.common.network.ClientContext;
import org.iremake.common.network.NetworkContext;
import org.iremake.common.network.handler.ErrorHandler;
import org.iremake.common.network.NodeContext;
import org.iremake.common.network.messages.Message;
import org.iremake.server.network.handler.ClientNameHandler;
import org.iremake.server.network.handler.VerifyVersionHandler;

/**
 *
 */
public class ServerHandler extends Listener implements NetworkContext {

    private static final int MAX_CLIENTS = 100;
    private Map<Integer, ClientContext> map = new HashMap<>(10);

    @Override
    public void connected(Connection connection) {
        if (map.size() >= MAX_CLIENTS) {
            // disconnect with message
        } else {
            ClientContext client = ServerFactory.createNewConnectedClient();
            map.put(connection.getID(), client);
        }
    }

    private ClientContext newConnectedClient() {
        NodeContext root = NodeContext.createRoot(new ErrorHandler());
        NodeContext node = root.add(new VerifyVersionHandler());
        node.add(new ClientNameHandler());

        // return new ConnectedClient(root, this);
        return null;
    }

    @Override
    public void disconnected(Connection connection) {
        map.remove(connection.getID());
    }

    public void disconnect(Integer id) {
        if (map.containsKey(id)) {
            // map.get(id)
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if (connection.isConnected() && object instanceof Message) {
            Integer id = connection.getID();
            if (!map.containsKey(id)) {
                throw new RuntimeException("connection not registered. internal error.");
            }
            map.get(id).process((Message) object);
        } else {
            connection.close();
        }
    }
}
