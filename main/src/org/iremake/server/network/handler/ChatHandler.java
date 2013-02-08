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
import org.iremake.common.network.messages.TextMessageType;
import org.iremake.server.network.ServerNodeContext;

/**
 *
 */
public class ChatHandler implements ServerHandler {

    private static final Logger LOG = Logger.getLogger(ChatHandler.class.getName());

    @Override
    public void process(Message message, ServerNodeContext context) {
        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;
            if (TextMessageType.Chat.equals(msg.getType())) {
                // has sent a chat message
                LOG.log(Level.FINE, "Client transmitted chat message: {0}", msg.getText());
                // transmit to all others
                context.broadcast(message);
                // done
                return;
            }
        }
        // not a chat message just propagate further
        context.propagate(message);
    }

    @Override
    public String name() {
        return "handler.chat.message";
    }
}
