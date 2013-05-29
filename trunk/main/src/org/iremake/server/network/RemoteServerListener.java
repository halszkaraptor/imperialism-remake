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

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.messages.ErrorMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.lobby.LobbyChatMessage;
import org.iremake.common.network.messages.lobby.LobbyClientEntry;
import org.iremake.common.network.messages.lobby.LobbyServerOverviewMessage;
import org.iremake.common.network.messages.lobby.LobbyServerUpdateMessage;
import org.iremake.server.client.ServerClient;
import org.iremake.server.client.ServerClientState;

// TODO throw out connections that do not make successful login within a certain time limit
/**
 *
 */
public class RemoteServerListener extends Listener implements ServerContext {

    private static final Logger LOG = Logger.getLogger(RemoteServerListener.class.getName());
    private static final int MAX_CLIENTS = 100;
    private Map<Integer, ServerClient> clients = new HashMap<>(10);
    private Map<Integer, Connection> connections = new HashMap<>(10);

    @Override
    public void connected(Connection connection) {
        if (clients.size() >= MAX_CLIENTS) {
            connection.sendTCP(new ErrorMessage(String.format("Too many connected clients. Max = %d", MAX_CLIENTS)));
            connection.close();
        } else {
            // initial handler chain for every connected client
            Integer id = connection.getID();
            ServerClient client = new ServerClient(id, this);
            clients.put(id, client);
            connections.put(id, connection);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        LOG.log(Level.FINE, "Connection {0} disconnected.", connection.getID());
        if (clients.containsKey(connection.getID())) {
            clients.get(connection.getID()).shutdown();
            clients.remove(connection.getID());
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if (connection.isConnected() && object instanceof Message) {
            Message message = (Message) object;
            LOG.log(Level.FINER, "Received message: {0}", message.toString());
            Integer id = connection.getID();
            if (!clients.containsKey(id)) {
                throw new RuntimeException("connection not registered. internal error.");
            }
            clients.get(id).process(message);
        } else {
            connection.close();
        }
    }
    private List<String> chatHistory = new LinkedList<>();

    @Override
    public void sendLobbyOverview(ServerClient recipient) {
        List<LobbyClientEntry> overviewList = new LinkedList<>();
        for (ServerClient client : clients.values()) {
            if (ServerClientState.LOBBY.equals(client.getState())) {
                overviewList.add(client.getLobbyEntry());
            }
        }
        recipient.send(new LobbyServerOverviewMessage(overviewList, combineChatHistory()));
    }

    private String combineChatHistory() {
        StringBuilder sb = new StringBuilder(1000);
        for (String item : chatHistory) {
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * We send to all.
     *
     * @param text
     * @param sender
     */
    @Override
    public void broadcastNewChatMessage(String text, ServerClient sender) {
        String chatMessage = String.format("[%s] %s\n", sender.getLobbyEntry().name, text);
        for (ServerClient client : clients.values()) {
            if (ServerClientState.LOBBY.equals(client.getState())) {
                client.send(new LobbyChatMessage(chatMessage));
            }
        }
        // add to history (delete old entries if more than 20 entries)
        chatHistory.add(text);
        if (chatHistory.size() > 20) {
            chatHistory.remove(0);
        }
    }

    @Override
    public void broadcastArrivingLobbyClient(ServerClient arriving) {
        LobbyServerUpdateMessage message = new LobbyServerUpdateMessage(arriving.getLobbyEntry(), null);
        for (ServerClient client : clients.values()) {
            if (ServerClientState.LOBBY.equals(client.getState())) {
                client.send(message);
            }
        }
    }

    @Override
    public String getIP(Integer id) {
        Connection connection = connections.get(id);
        InetSocketAddress address = connection.getRemoteAddressTCP();
        return address != null ? address.getHostString() : "";
    }

    @Override
    public void disconnect(Integer id) {
        connections.get(id).close();
    }

    @Override
    public void sendMessage(Integer id, Message message) {
        connections.get(id).sendTCP(message);
    }
}
