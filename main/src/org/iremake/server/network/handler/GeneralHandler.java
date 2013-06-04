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
import org.iremake.client.Option;
import org.iremake.common.network.messages.LoginData;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.MessageContainer;
import org.iremake.server.client.ServerClient;
import org.iremake.server.client.ServerClientState;

/**
 *
 */
public class GeneralHandler implements ServerHandler {

    private static final Logger LOG = Logger.getLogger(GeneralHandler.class.getName());

    @Override
    public boolean process(MessageContainer message, ServerClient client) {
        // filter, we only work on general messages
        if (!message.getType().isKindOf(Message.GENERAL)) {
            return false;
        }
        // go through each message and do something
        switch (message.getType()) {
            case GEN_ERROR:
                // an error was received, disconnect                
                LOG.log(Level.SEVERE, "Received error message: {0}", (String) message.getAttachment());
                client.disconnect(null);
                return true;
                
            case GEN_LOGIN:
                // a login attempt, compared version and if successful, add to lobby                
                LoginData data = (LoginData) message.getAttachment();
                // test version
                if (Option.General_Version.get().equals(data.getVersion())) {
                    // set the new name
                    client.setName(data.getClientName());
                    // add some handlers
                    client.addHandler(new LobbyHandler());
                    // put client in lobby
                    client.setState(ServerClientState.LOBBY);
                    // send a LobbyOverviewMessage
                    client.getContext().sendLobbyOverview(client);
                    // send others LobbyUpdateMessage
                    client.getContext().broadcastArrivingLobbyClient(client);
                } else {
                    // version different disconnect and LOG
                    client.disconnect("Client reported different version. Not compatible.");
                }
                return true;
        }
        // still here, then it has nothing to do with us
        return false;
    }
}
