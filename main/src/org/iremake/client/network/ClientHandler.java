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
import org.iremake.client.Option;
import org.iremake.common.network.messages.ActionMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.NumberMessage;
import org.iremake.common.network.messages.NumberMessageType;
import org.iremake.common.network.messages.TextMessageType;

/**
 * Handling of received messages on the client side.
 */
public class ClientHandler extends Listener {

    @Override
    public void connected(Connection connection) {
        ClientLogger.log("Connection from " + connection.getRemoteAddressTCP());
    }

    @Override
    public void disconnected(Connection connection) {
        ClientLogger.log("Disconnection");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Message) {
            ClientLogger.log("Message arrived: " + object.getClass().getSimpleName());

            // action messages
            if (object instanceof ActionMessage) {
                ActionMessage message = (ActionMessage) object;
                switch (message) {
                case Verify:
                    // server wants us to validate, so we send the version, followed by a request to register
                    ClientLogger.log("Received request to validate, send version and ask for Registration");
                    connection.sendTCP(TextMessageType.Version.create(Option.Version.get()));
                    connection.sendTCP(ActionMessage.Register);
                    break;
                }

                return;
            }

            // number messages
            if (object instanceof NumberMessage) {
                NumberMessage message = (NumberMessage) object;
                if (NumberMessageType.ID.equals(message.getType())) {
                    ClientLogger.log("New ID: " + message.getNumber());
                }
            }
        } else {
            // TODO log unexpected types of objects, should normally not happen
        }
    }
}
