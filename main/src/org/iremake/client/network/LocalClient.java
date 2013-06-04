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
package org.iremake.client.network;

import java.util.LinkedList;
import java.util.List;
import org.iremake.client.network.handler.ClientHandler;
import org.iremake.common.network.messages.MessageContainer;
import org.iremake.server.network.LocalServer;

/**
 *
 */
public class LocalClient implements ClientContext {

    public static final ClientContext CONTEXT = new LocalClient();
    private List<ClientHandler> handlerList = new LinkedList<>();

    private LocalClient() {
    }

    @Override
    public void disconnect(String error) {
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean start(String address) {
        return false;
    }

    @Override
    public void stop() {
    }

    @Override
    public void addHandler(ClientHandler handler) {
        if (!handlerList.contains(handler)) {
            handlerList.add(handler);
        }
    }

    /**
     *
     * @param channel
     * @param handler
     * @return
     */
    @Override
    public boolean removeHandler(ClientHandler handler) {
        return handlerList.remove(handler);
    }

    @Override
    public void send(MessageContainer message) {
        LocalServer.CONTEXT.process(null, message);
    }

    @Override
    public void process(MessageContainer message) {
        for (ClientHandler handler: handlerList) {
            if (handler.process(message, this)) {
                break;
            }
        }
    }
}
