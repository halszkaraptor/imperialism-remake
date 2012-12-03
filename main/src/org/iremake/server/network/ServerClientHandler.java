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
package org.iremake.server.network;

import com.esotericsoftware.kryonet.Connection;
import java.util.Timer;
import java.util.TimerTask;
import org.iremake.client.Option;
import org.iremake.client.StartClient;
import org.iremake.common.network.NetworkLogger;
import org.iremake.common.network.messages.ActionMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.NumberMessage;
import org.iremake.common.network.messages.TextMessage;

/**
 * One for each connected client. This is an stateful entity, so we have to take
 * care of thread-safety since all messages can come in any order...
 */
public final class ServerClientHandler {

    public static int idcounter = 0; // TODO this is a quick hack, not thread-safe, not re-using, not controled in any way, will be fixed later on
    private Connection connection;
    private ClientState state;
    private NetworkLogger logger;
    private Timer timer = new Timer();

    public ServerClientHandler(Connection connection, NetworkLogger logger) {
        this.connection = connection;
        this.logger = logger;
        // initially invalid
        state = ClientState.Invalid;
        // first thing send the client a request to validate (returning a the right version string)
        logger.log("New connection, send request for validation shortly.");
        sendLater(ActionMessage.Validate, 500); // TODO what is a good value here
    }

    public void received(Message message) {
        // action messages
        if (message instanceof ActionMessage) {
            switch ((ActionMessage) message) {
            case Register:
                logger.log("Client from " + connection.getRemoteAddressTCP() + " wants to register, send an ID. ");
                send(new NumberMessage(idcounter++, NumberMessage.Type.ID));
                break;
            }

            return;
        }

        // number messages
        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;
            switch (msg.getType()) {
            case Version:
                // verify version
                if (Option.Version.get().equals(msg.getText())) {
                    // right version
                    logger.log("Version accepted, promote state to valid.");
                    state = ClientState.Valid;
                } else {
                    // TODO send error message and disconnect
                    connection.close();
                }
                break;
            case ClientName:
                logger.log("New client name: " + msg.getText());
                break;

            }

            return;
        }
    }

    public void send(Message message) {
        // TODO check if connection still connected
        connection.sendTCP(message);
    }

    public void sendLater(final Message message, long period) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                send(message);
            }
        }, period);
    }

    public void setState(ClientState state) {
        this.state = state;
    }
}
