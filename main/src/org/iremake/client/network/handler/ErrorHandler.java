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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.network.ClientNodeContext;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.MessageType;
import org.iremake.common.network.messages.TextMessage;

/**
 * This is the first handler in every processing tree. It filters out error
 * messages, logs them and disconnect. All other messages are propagated.
 */
public class ErrorHandler implements ClientHandler {

    private static final Logger LOG = Logger.getLogger(ErrorHandler.class.getName());

    @Override
    public void process(Message message, ClientNodeContext node) {
        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;
            if (MessageType.Error.equals(msg.getType())) {
                LOG.log(Level.SEVERE, "Received error message: {0}", msg.getText());
                node.disconnect(null);
                return;
            }
        }
        // continue broadcasting it
        node.propagate(message);
    }

    @Override
    public String name() {
        return "handler.error";
    }
}
