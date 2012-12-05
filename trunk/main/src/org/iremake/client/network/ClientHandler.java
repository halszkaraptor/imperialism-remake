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

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.iremake.client.network.clients.Client;
import org.iremake.client.network.clients.RegisteredClient;
import org.iremake.client.network.clients.UnregisteredClient;
import org.iremake.common.network.messages.Message;

/**
 * Handling of received messages on the client side. Only one connection.
 */
public class ClientHandler extends Listener {

    private Client client;
    private Connection connection;

    public ClientHandler() {
        client = new UnregisteredClient(this);
    }

    @Override
    public void disconnected(Connection connection) {
        ClientLogger.log("Disconnection");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (this.connection == null) {
            this.connection = connection;
        }
        if (object instanceof Message) {
            ClientLogger.log("Message arrived: " + object.getClass().getSimpleName());
            client.consume((Message) object);
        } else {
            // TODO log unexpected types of objects, should normally not happen
        }
    }

    public void send(Message message) {
        connection.sendTCP(message);
    }

    public void registrationSuccess() {
        client = new RegisteredClient(this);
    }
}
