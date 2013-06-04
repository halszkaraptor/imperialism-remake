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

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.iremake.client.network.ClientContext;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.MessageContainer;
import org.iremake.common.network.messages.lobby.LobbyListEntry;
import org.iremake.common.network.messages.lobby.LobbyServerOverview;
import org.tools.ui.SimpleListModel;

/**
 *
 */
public class LobbyHandler implements ClientHandler {

    private Document chatHistory;
    private SimpleListModel<LobbyListEntry> lobbyListModel;

    public LobbyHandler(Document chatHistory, SimpleListModel<LobbyListEntry> lobbyListModel) {
        // TODO cannot be null
        this.chatHistory = chatHistory;
        this.lobbyListModel = lobbyListModel;
    }

    @Override
    public boolean process(MessageContainer message, ClientContext context) {
        // filter, we only work on lobby messages
        if (!message.getType().isKindOf(Message.LOBBY)) {
            return false;
        }
        // go through each message and do something        
        switch (message.getType()) {
            case LOBBY_CHAT:
                try {
                    chatHistory.insertString(chatHistory.getLength(), (String) message.getAttachment(), null);
                } catch (BadLocationException ex) {
                    // should not happen
                }
                return true;

            case LOBBY_OVERVIEW:
                // a complete overview has been sent
                LobbyServerOverview serverOverview = (LobbyServerOverview) message.getAttachment();

                // update chatHistory
                try {
                    chatHistory.remove(0, chatHistory.getLength());
                    chatHistory.insertString(0, serverOverview.getChatHistory(), null);
                } catch (BadLocationException ex) {
                    // should not happen
                }

                // update lobby list
                lobbyListModel.set(serverOverview.getClients());

                return true;

            case LOBBY_UPDATE:
                // something changed
                return true;
        }

        // still here, then it has nothing to do with us                
        return false;
    }
}
