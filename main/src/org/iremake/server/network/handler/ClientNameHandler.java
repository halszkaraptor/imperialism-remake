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
import org.iremake.common.network.handler.ErrorHandler;
import org.iremake.common.network.handler.Handler;
import org.iremake.common.network.handler.HandlerNode;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.TextMessageType;

/**
 *
 */
public class ClientNameHandler implements Handler {
    private static final Logger LOG = Logger.getLogger(ClientNameHandler.class.getName());

    @Override
    public void process(Message message, HandlerNode node) {
        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;
            if (TextMessageType.ClientName.equals(msg.getType())) {
                // has sent the client name
                    // passed version test
                    LOG.log(Level.FINE, "Client transmitted name: {0}", msg.getText());
                    // LOG.log(Level.FINE, "Client transmitted correct version" + client.name());
                    // remove from chain
                    // client.remove();
                    node.remove();
            }
        }
        // disconnect with ErrorMessage
        TextMessageType.Error.create("Need clients name.");
    }

    @Override
    public String name() {
        return "handler.register.name";
    }

}
