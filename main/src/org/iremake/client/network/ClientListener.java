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
import org.iremake.common.network.ClientContext;
import org.iremake.common.network.messages.Message;

/**
 *
 */
public class ClientListener extends Listener {

    private static final Logger LOG = Logger.getLogger(ClientListener.class.getName());
    private ClientContext client;

    @Override
    public void connected(Connection connection) {
        LOG.log(Level.FINE, "Client has connected.");
        client = ClientFactory.createNewConnectedClient();
    }

    @Override
    public void disconnected(Connection connection) {
        // TODO either we or somebody else disconnected
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
}
