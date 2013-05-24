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
package org.iremake.network;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.Option;
import org.iremake.client.network.ClientManager;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.Channel;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.MessageType;
import org.iremake.server.network.ServerManager;

/**
 * Small brother of the console, not interactive. Tests the network capabilities.
 */
public class ClientServerTest {

    private static final Logger LOG = Logger.getLogger(ClientServerTest.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // setup logger
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s [%2$s]%n");
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.FINER);
        Logger.getLogger("").addHandler(handler);
        Logger.getLogger("").setLevel(Level.FINER);

        // load options
        Option.load();

        // start server
        final ServerManager server = new ServerManager();
        server.start();

        // start and connect client
        final ClientManager client = new ClientManager();
        client.start("localhost");

        client.send(new TextMessage("chat message", MessageType.Chat, Channel.LOBBY));

        // in 5s stop everything
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                client.stop();
                server.stop();
            }
        }, 5000);

    }
}
