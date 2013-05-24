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
package org.iremake.server.network.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.MessageType;
import org.iremake.server.network.ServerContext;

/**
 *
 */
public class ChatHandler implements ServerHandler {

    private static final Logger LOG = Logger.getLogger(ChatHandler.class.getName());

    @Override
    public boolean process(Message message, ServerContext context) {
        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;
            if (MessageType.Chat.equals(msg.getType())) {
                // has sent a chat message
                LOG.log(Level.FINE, "Client transmitted chat message: {0}", msg.getText());
                // transmit to all others
                // context.broadcast(message);
                // done
                return true;
            }
        }
        return false;
    }
}
