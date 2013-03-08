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

import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;

/**
 * What the client can do.
 */
public interface ClientContext {

    /**
     * Force disconnection, send message before.
     *
     * @param error Error message, if null nothing is sent before disconnection.
     */
    public void disconnect(TextMessage error);

    /**
     * Send message to the server.
     *
     * @param message Message to be sent.
     */
    public void send(Message message);

    /**
     * Process a message further, that means send it downstream in the
     * processing hierarchy.
     *
     * @param message Message to be processed.
     */
    public void process(Message message);
}
