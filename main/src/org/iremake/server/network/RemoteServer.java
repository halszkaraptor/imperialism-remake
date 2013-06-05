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
package org.iremake.server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.Settings;
import org.iremake.common.network.messages.KryoRegistration;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.MessageContainer;
import org.iremake.common.network.messages.lobby.LobbyListEntry;
import org.iremake.common.network.messages.lobby.LobbyServerOverview;
import org.iremake.common.network.messages.lobby.LobbyServerUpdate;
import org.iremake.server.client.ServerClient;
import org.iremake.server.client.ServerClientState;
import org.iremake.server.network.handler.GeneralHandler;

/**
 * Starts the server.
 */
public class RemoteServer extends Listener implements ServerContext {

    private static final Logger LOG = Logger.getLogger(RemoteServer.class.getName());
    /* port on which we listen to clients */
    private Server server;
    public static final ServerContext CONTEXT = new RemoteServer();

    private static final int MAX_CLIENTS = 100;
    private Map<Integer, ServerClient> clients = new HashMap<>(10);
    private Map<Integer, Connection> connections = new HashMap<>(10);
    private List<String> chatHistory = new LinkedList<>();

    /**
     * Avoid instantiation
     */
    private RemoteServer() {
    }

    /**
     * Starts the server.
     *
     * @return
     */
    @Override
    public boolean start() {
        LOG.log(Level.INFO, "[SERVER] Server startup initiated.");
        if (server != null) {
            return false;
        }
        server = new Server();

        KryoRegistration.register(server.getKryo());

        server.start();

        try {
            server.bind(Settings.NETWORK_PORT);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            LOG.log(Level.INFO, "[SERVER] Server could not start.");

            stop();

            return false;
        }

        LOG.log(Level.INFO, "[SERVER] Server started successful, now listening on port.");
        server.addListener(this);

        return true;
    }

    /**
     * Tells whether the server has been started.
     *
     * @return True if it is running
     */
    @Override
    public boolean isRunning() {
        return server != null;
    }

    /**
     * Stops the server.
     */
    @Override
    public void stop() {
        if (server != null) {
            LOG.log(Level.INFO, "[SERVER] Server will shutdown.");
            for (Connection connection : server.getConnections()) {
                connection.close();
            }
            server.stop();
            server = null;

            // fireStatusChanged("Server not running.");
        } else {
            LOG.log(Level.INFO, "[SERVER] Already stopped.");
        }
    }

    @Override
    public String getStatus() {
        if (server != null) {
            String ip;
            try {
                InetAddress address = InetAddress.getLocalHost();
                ip = address.getHostAddress();
            } catch (UnknownHostException ex) {
                LOG.log(Level.SEVERE, null, ex);
                ip = "unknown IP";
            }
            return String.format("Server running at %s with %d connected clients.", ip, server.getConnections().length);
        } else {
            return "Server not running.";
        }
    }

    @Override
    public void connected(Connection connection) {
        if (clients.size() >= MAX_CLIENTS) {
            connection.sendTCP(new MessageContainer<>(String.format("Too many connected clients. Max = %d", MAX_CLIENTS), Message.GEN_ERROR));
            connection.close();
        } else {
            // initial handler chain for every connected client
            Integer ID = connection.getID();
            LOG.log(Level.INFO, "[SERVER] Client (ID={0}) has connected.", ID);
            connections.put(ID, connection);
            // we need the connection in the constructor of the serverclient already
            ServerClient sclient = new ServerClient(ID, this);
            sclient.addHandler(new GeneralHandler());
            clients.put(ID, sclient);
        }
    }

    /**
     * A disconnection event occurred.
     *
     * @param connection
     */
    @Override
    public void disconnected(Connection connection) {
        Integer ID = connection.getID();
        LOG.log(Level.INFO, "[SERVER] Client (ID={0}) was/has disconnected.", ID);
        if (clients.containsKey(ID)) {
            clients.get(ID).shutdown();
            clients.remove(ID);
            connections.remove(ID);
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if (connection.isConnected() && object instanceof MessageContainer) {
            MessageContainer message = (MessageContainer) object;
            LOG.log(Level.INFO, "[SERVER] Received message of type {0}", message.getType().name());
            Integer id = connection.getID();
            if (!clients.containsKey(id)) {
                throw new RuntimeException("[SERVER] ID not registered. Internal failure.");
            }
            process(id, message);
        }
        // if is wasn't a MessageContainer it might have been a keepalive message, so we just let it pass
    }

    @Override
    public void process(Integer id, MessageContainer message) {
        clients.get(id).process(message);
    }

    @Override
    public void sendLobbyOverview(ServerClient recipient) {
        List<LobbyListEntry> overviewList = new LinkedList<>();
        for (ServerClient client : clients.values()) {
            if (ServerClientState.LOBBY.equals(client.getState())) {
                overviewList.add(client.getLobbyEntry());
            }
        }
        LobbyServerOverview serverOverview = new LobbyServerOverview(overviewList, combineChatHistory());
        recipient.send(Message.LOBBY_OVERVIEW.createNew(serverOverview));
    }

    private String combineChatHistory() {
        StringBuilder sb = new StringBuilder(1_000);
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
        MessageContainer message = Message.LOBBY_CHAT.createNew(chatMessage);
        for (ServerClient client : clients.values()) {
            if (ServerClientState.LOBBY.equals(client.getState())) {
                client.send(message);
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
        LobbyServerUpdate update = new LobbyServerUpdate(arriving.getLobbyEntry(), null);
        MessageContainer message = Message.LOBBY_UPDATE.createNew(update);
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
    public void disconnect(Integer ID) {
        LOG.log(Level.INFO, "[SERVER] We want to disconnect client (ID={0}).", ID);
        connections.get(ID).close();
    }

    @Override
    public void sendMessage(Integer ID, MessageContainer message) {
        LOG.log(Level.INFO, "[SERVER] Send message of type {0} to client {1}.", new Object[]{message.getType().name(), ID});
        connections.get(ID).sendTCP(message);
    }
}
