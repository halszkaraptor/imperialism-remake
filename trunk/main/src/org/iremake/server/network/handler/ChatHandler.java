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

import java.util.logging.Logger;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.lobby.LobbyChatMessage;
import org.iremake.server.network.ServerClient;

/**
 *
 */
public class ChatHandler implements ServerHandler {

    private static final Logger LOG = Logger.getLogger(ChatHandler.class.getName());
    
    @Override
    public boolean process(Message message, ServerClient client) {
        if (message instanceof LobbyChatMessage) {
            LobbyChatMessage msg = (LobbyChatMessage) message;
            
            return true;
        }
        return false;
    }
}
