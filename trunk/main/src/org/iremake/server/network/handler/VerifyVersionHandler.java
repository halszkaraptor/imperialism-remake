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
import org.iremake.common.network.messages.Channel;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.MessageType;
import org.iremake.server.network.ServerNodeContext;

/**
 * This is a stopper, either you transmit the right version or you will be kicked out.
 */
public class VerifyVersionHandler implements ServerHandler {
    private static final Logger LOG = Logger.getLogger(ErrorHandler.class.getName());

    @Override
    public void process(Message message, ServerNodeContext context) {
        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;
            if (MessageType.Version.equals(msg.getType())) {
                if (Option.General_Version.get().equals(msg.getText())) {
                    LOG.log(Level.FINE, "Client {0} transmitted correct version", context.getName());
                    // passed version test, remove yourself from handler list
                    context.remove();
                    return;
                }
            }
        }
        // disconnect with ErrorMessage
        context.disconnect(new TextMessage(String.format("Did not receive version message or wrong version. Was waiting for version. My version is %s", Option.General_Version.get()),
                MessageType.Error, Channel.ERROR));
    }

    @Override
    public String name() {
        return "handler.registration.version";
    }
}
