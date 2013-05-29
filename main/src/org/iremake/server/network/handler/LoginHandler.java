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

import org.iremake.client.Option;
import org.iremake.common.network.messages.LoginMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.server.client.ServerClient;
import org.iremake.server.client.ServerClientState;

/**
 *
 */
public class LoginHandler implements ServerHandler {

    @Override
    public boolean process(Message message, ServerClient client) {
        if (message instanceof LoginMessage) {
            LoginMessage msg = (LoginMessage) message;

            // test version
            if (Option.General_Version.get().equals(msg.getVersion())) {
                // set the new name
                client.setName(msg.getClientName());
                // send a LobbyOverviewMessage
                client.getContext().sendLobbyOverview(client);
                // add some handlers
                client.addHandler(new ChatHandler());
                // put client in lobby
                client.setState(ServerClientState.LOBBY);
                // send others LobbyUpdateMessage
                client.getContext().broadcastArrivingLobbyClient(client);
            } else {
                // version different disconnect and LOG
                client.disconnect("Client reported different version. Not compatible.");
            }

            // no need to process further
            return true;
        }
        return false;
    }

}
