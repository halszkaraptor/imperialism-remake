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
package org.iremake.server.network.clients;

import org.iremake.common.network.messages.ActionMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.NumberMessage;
import org.iremake.common.network.messages.NumberMessageType;
import org.iremake.server.network.ServerClientHandler;
import org.iremake.server.network.ServerLogger;

/**
 *
 */
public class VerifiedSClient extends SClient {

    private int id = 1; // TODO get running numbers, possibly from the boss

    public VerifiedSClient(ServerClientHandler handler) {
        super(handler);
    }

    @Override
    public void consume(Message message) {
        if (message instanceof ActionMessage) {
            if (ActionMessage.Register.equals((ActionMessage) message)) {
                ServerLogger.log("Client wants to register, send an ID. ");
                boss.send(NumberMessageType.ID.create(id));
            }
        }
        if (message instanceof NumberMessage) {
            NumberMessage msg = (NumberMessage) message;
            if (NumberMessageType.ID.equals(msg.getType())) {
                ServerLogger.log("Client sent ID.");
                if (id == msg.getNumber()) {
                    // TODO promote to registered client
                    boss.registrationSuccess();
                } else {
                    // possibly someone being disconnected, trying to reconnect
                    boss.registrationIncorrectID(msg.getNumber());
                }
            }
        }
    }
}
