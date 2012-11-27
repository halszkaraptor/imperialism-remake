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
import org.iremake.client.Options;
import org.iremake.common.network.NetworkLogger;
import org.iremake.common.network.messages.ActionMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.NumberMessage;
import org.iremake.common.network.messages.TextMessage;

/**
 * Handling of received messages on the client side.
 */
public class ClientHandler extends Listener {

    private NetworkLogger logger;

    public ClientHandler(NetworkLogger logger) {
        this.logger = logger;
    }

    @Override
    public void connected(Connection connection) {
        logger.log("Connection from " + connection.getRemoteAddressTCP());
    }

    @Override
    public void disconnected(Connection connection) {
        logger.log("Disconnection");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Message) {
            logger.log("Message arrived: " + object.getClass().getSimpleName());

            // action messages
            if (object instanceof ActionMessage) {
                ActionMessage message = (ActionMessage) object;
                switch (message) {
                case Validate:
                    // server wants us to validate, so we send the version, followed by a request to register
                    logger.log("Received request to validate, send version and ask for Registration");
                    connection.sendTCP(new TextMessage(Options.Version.get(), TextMessage.Type.Version));
                    connection.sendTCP(ActionMessage.Register);
                    break;
                }

                return;
            }

            // number messages
            if (object instanceof NumberMessage) {
                NumberMessage message = (NumberMessage) object;
                if (NumberMessage.Type.ID.equals(message.getType())) {
                    logger.log("New ID: " + message.getNumber());
                }

                return;
            }
        } else {
            // TODO log unexpected types of objects, should normally not happen
        }
    }
}
