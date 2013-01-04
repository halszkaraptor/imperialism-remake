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
package org.iremake.client.network.clients;

import org.iremake.client.network.ClientHandler;
import org.iremake.common.network.messages.Message;

/**
 * A client state. It has access to the boss for sending messages and changing
 * the state. It is able to consume messages.
 */
public abstract class Client {

    /* The central handler of messages */
    protected final ClientHandler boss;

    /**
     * Initializes the client.
     *
     * @param boss
     */
    public Client(ClientHandler boss) {
        this.boss = boss;
    }

    /**
     * Consumes a message object.
     *
     * @param message the message
     */
    public abstract void consume(Message message);
}
