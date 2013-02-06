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
import org.iremake.common.network.ConnectedClient;
import org.iremake.common.network.messages.Message;

/**
 *
 */
public class ServerListener extends Listener {

    private static final int MAX_CLIENTS = 100;
    private Map<Integer, ConnectedClient> map = new HashMap<>(10);

    @Override
    public void connected(Connection connection) {
        if (map.size() >= MAX_CLIENTS) {
            // disconnect with message
        } else {
            ConnectedClient client = ServerFactory.createNewConnectedClient();
            map.put(connection.getID(), client);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        map.remove(connection.getID());
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
