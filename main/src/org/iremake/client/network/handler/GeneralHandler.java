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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.network.ClientContext;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.MessageContainer;

/**
 *
 */
public class GeneralHandler implements ClientHandler {

    private static final Logger LOG = Logger.getLogger(GeneralHandler.class.getName());

    @Override
    public boolean process(MessageContainer message, ClientContext context) {
        // filter, we only work on general messages
        if (!message.getType().isKindOf(Message.GENERAL)) {
            return false;
        }
        // go through each message and do something
        switch (message.getType()) {
            case GEN_ERROR:
                // an error was received, disconnect                
                LOG.log(Level.SEVERE, "Received error message: {0}", (String) message.getAttachment());
                context.disconnect(null);
                return true;
        }
        // still here, then it has nothing to do with us
        return false;
    }
}
