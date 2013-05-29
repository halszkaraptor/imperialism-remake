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
package org.iremake.server.client;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import org.iremake.client.network.RemoteClient;
import org.iremake.client.network.handler.ErrorHandler;
import org.iremake.common.network.messages.ErrorMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.lobby.LobbyClientEntry;
import org.iremake.server.network.ServerContext;
import org.iremake.server.network.handler.LoginHandler;
import org.iremake.server.network.handler.ServerHandler;

/**
 *
 */
public class ServerClient {

    private static final Logger LOG = Logger.getLogger(ServerClient.class.getName());
    private final ExecutorService threadPool = Executors.newFixedThreadPool(1);
    private final Integer id;
    private final ServerContext context;
    private LobbyClientEntry lobbyEntry = new LobbyClientEntry();
    private ServerClientState state = ServerClientState.UNIDENTIFIED;;

    private final ErrorHandler errorHandler = new ErrorHandler();
    private List<ServerHandler> handlerList = new LinkedList<>();

    /**
     *
     * @param root
     * @param connection
     */
    public ServerClient(Integer id, ServerContext context) {
        this.id = id;
        this.context = context;

        handlerList.add(new LoginHandler());

        // fill lobbyEntry
        lobbyEntry.ip = context.getIP(id);
        lobbyEntry.joined = new Date().toString();
    }

    /**
     *
     * @param message
     */
    public void process(final Message message) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                errorHandler.process(message, RemoteClient.INSTANCE);
                for (ServerHandler handler: handlerList) {
                    if (handler.process(message, ServerClient.this)) {
                        break;
                    }
                }
            }
        });
    }

    /**
     *
     * @param error
     */
    public void disconnect(String error) {
        if (error != null) {
            send(new ErrorMessage(error));
        }
        context.disconnect(id);
    }

    /**
     *
     * @param message
     */
    public void send(Message message) {
        context.sendMessage(id, message);
    }

    /**
     *
     */
    public void shutdown() {
        threadPool.shutdown();
    }

    public void setName(String name) {
        lobbyEntry.name = name;
    }

    public void setState(ServerClientState state) {
        this.state = state;
    }

    public ServerClientState getState() {
        return state;
    }

    public void addHandler(ServerHandler handler) {
        handlerList.add(handler);
    }

    public boolean removeHandler(ServerHandler handler) {
        return handlerList.remove(handler);
    }

    public ServerContext getContext() {
        return context;
    }

    public LobbyClientEntry getLobbyEntry() {
        return lobbyEntry;
    }
}
