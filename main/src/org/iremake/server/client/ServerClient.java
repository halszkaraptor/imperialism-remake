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

import com.esotericsoftware.kryonet.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import org.iremake.client.network.ClientManager;
import org.iremake.client.network.handler.ErrorHandler;
import org.iremake.common.network.messages.ErrorMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.server.network.ServerListener;
import org.iremake.server.network.handler.ChatHandler;
import org.iremake.server.network.handler.LoginHandler;
import org.iremake.server.network.handler.ServerHandler;

/**
 *
 */
public class ServerClient {

    private static final Logger LOG = Logger.getLogger(ServerClient.class.getName());    
    private final ExecutorService threadPool = Executors.newFixedThreadPool(1);
    private final Connection connection;
    private final ServerListener listener;
    private String name;
    private ServerClientState state;
    
    private final ErrorHandler errorHandler = new ErrorHandler();
    private List<ServerHandler> handlerList = new LinkedList<>();

    /**
     *
     * @param root
     * @param connection
     */
    public ServerClient(Connection connection, ServerListener listener) {
        this.connection = connection;
        this.listener = listener;
        
        state = ServerClientState.UNIDENTIFIED;
        handlerList.add(new LoginHandler());

        /*
        InetSocketAddress address = connection.getRemoteAddressTCP();
        name = String.format("[%d,%s]", connection.getID(), address != null ? address.getHostString() : "");
        */
    }

    /**
     *
     * @param message
     */
    public void process(final Message message) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                errorHandler.process(message, ClientManager.NETWORK);
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
        connection.close();
    }

    /**
     *
     * @param message
     */
    public void send(Message message) {
        connection.sendTCP(message);
    }

    /**
     *
     */
    public void shutdown() {
        threadPool.shutdown();
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
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


}
