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
import org.iremake.common.network.messages.ActionMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.server.network.ServerNodeContext;

/**
 *
 */
public class LobbyRegistrationHandler implements ServerHandler {

    private static final Logger LOG = Logger.getLogger(LobbyRegistrationHandler.class.getName());

    @Override
    public void process(Message message, ServerNodeContext context) {
        /*
        if (ActionMessage.LOBBY_REGISTER.equals(message)) {
            // Todo: do registration
            return;
        }
        if (ActionMessage.LOBBY_UNREGISTER.equals(message)) {
            // TODo: do unregistration
            return;
        }*/
        // just propagate
        context.propagate(message);
    }

    @Override
    public String name() {
        return "handler.lobby.registration";
    }

}
