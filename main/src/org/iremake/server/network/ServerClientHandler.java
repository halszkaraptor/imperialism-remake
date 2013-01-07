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
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessageType;
import org.iremake.server.network.clients.RegisteredServerClient;
import org.iremake.server.network.clients.ServerClient;
import org.iremake.server.network.clients.UnregisteredServerClient;

/**
 * A handler for handling different server client states on one connection.
 */
public class ServerClientHandler {

    private Connection connection;
    private ServerHandler boss;
    private ServerClient client;

    public ServerClientHandler(Connection connection, ServerHandler boss) {
        this.connection = connection;
        client = new UnregisteredServerClient(this);
    }

    /**
     * Delegate the message to the current client state.
     *
     * @param message the message
     */
    public void consume(Message message) {
        client.consume(message);
    }

    /**
     * Send a new message over the network.
     *
     * @param message the message
     */
    public void send(Message message) {
        connection.sendTCP(message);
    }

    /**
     * Promote client to ...
     *
     * @param name
     */
    public void registrationSuccess(String name) {
        client = new RegisteredServerClient(this, name);
    }

    /**
     * Return id of the connection, will also be the id for the client on the
     * client side.
     *
     * @return the id
     */
    public int getID() {
        return connection.getID();
    }

    /**
     * Demote server client ...
     *
     * @param text
     */
    public void registrationFailed(String text) {
        boss.disconnect(connection, TextMessageType.Error.create(text));
    }
}
