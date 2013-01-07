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

import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.server.network.ServerClientHandler;

/**
 * Not doing much for now. But we know the name of the client.
 */
public class RegisteredServerClient extends ServerClient {

    private String name;

    public RegisteredServerClient(ServerClientHandler handler, String name) {
        super(handler);
        this.name = name;
    }

    @Override
    public void consume(Message message) {
        if (message instanceof TextMessage) {
        }
    }
}
