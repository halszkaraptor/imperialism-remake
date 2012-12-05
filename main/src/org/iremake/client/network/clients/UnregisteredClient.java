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

import org.iremake.client.Option;
import org.iremake.client.network.ClientHandler;
import org.iremake.client.network.ClientLogger;
import org.iremake.common.network.messages.ActionMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.NumberMessage;
import org.iremake.common.network.messages.NumberMessageType;
import org.iremake.common.network.messages.TextMessageType;

/**
 *
 */
public class UnregisteredClient extends Client {

    public UnregisteredClient(ClientHandler handler) {
        super(handler);
    }

    @Override
    public void consume(Message message) {
            if (message instanceof ActionMessage) {
                ActionMessage msg = (ActionMessage) message;
                if (ActionMessage.Verify.equals(msg)) {
                    // server wants us to validate, so we send the version, followed by a request to register
                    ClientLogger.log("Received request to validate, send version and ask for Registration");
                    boss.send(TextMessageType.Version.create(Option.Version.get()));
                    boss.send(ActionMessage.Register);
                    // TODO what if those two messages arrive in different order, do we need a system to ensure the order?
                }
            }

            // number messages
            if (message instanceof NumberMessage) {
                NumberMessage msg = (NumberMessage) message;
                if (NumberMessageType.ID.equals(msg.getType())) {
                    ClientLogger.log("New ID: " + msg.getNumber());
                    boss.registrationSuccess();
                }
            }
    }

}
