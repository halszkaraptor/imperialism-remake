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
package org.iremake.client.network.handler;

import org.iremake.client.network.ClientManager;
import org.iremake.common.network.messages.Message;

/**
 * A client handler can process a message and if the message is of a certain
 * type, react specifically on it, then either consume it (not doing anything)
 * or feeding it back to the processing tree or even feed other messages to the
 * processing tree.
 */
public interface ClientHandler {

    /**
     * Process a message given a context. The context is responsible for client
     * actions (replying with a message, disconnecting) or for actions regarding
     * the processing tree (feeding the message back, ...).
     *
     * @param message Message to be processed.
     * @param context Context for what we can do.
     */
    public boolean process(Message message, ClientManager manager);
}
