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
package org.iremake.server.network;

import org.iremake.client.network.LocalClient;
import org.iremake.common.network.messages.Message;
import org.iremake.server.client.ServerClient;

/**
 *
 */
public class LocalServer implements ServerContext {

    public static final ServerContext CONTEXT = new LocalServer();
    private ServerClient client;

    private LocalServer() {
    }

    public void reset() {
        client = new ServerClient(null, this);
    }

    @Override
    public void sendLobbyOverview(ServerClient recipient) {
    }

    @Override
    public void broadcastArrivingLobbyClient(ServerClient arriving) {
    }

    @Override
    public void broadcastNewChatMessage(String text, ServerClient sender) {
    }

    @Override
    public String getIP(Integer id) {
        return "";
    }

    @Override
    public void disconnect(Integer id) {
    }

    @Override
    public void sendMessage(Integer id, Message message) {
        LocalClient.CONTEXT.process(message);
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void stop() {
    }

    @Override
    public String getStatus() {
        return "";
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public void process(Integer id, Message message) {

    }
}
