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
package org.iremake.common.network.handler;

import org.iremake.common.network.NodeContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.TextMessageType;

/**
 *
 */
public class ErrorHandler implements Handler {

    private static final Logger LOG = Logger.getLogger(ErrorHandler.class.getName());

    @Override
    public void process(Message message, NodeContext context) {
        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;
            if (TextMessageType.Error.equals(msg.getType())) {
                LOG.log(Level.SEVERE, "Received error message: {0}", msg.getText());
                // will close the connection if not closed already
                context.disconnect(null);
                return;
            }
        }
        // continue broadcasting it
        context.propagate(message);
    }

    @Override
    public String name() {
        return "handler.error";
    }
}
