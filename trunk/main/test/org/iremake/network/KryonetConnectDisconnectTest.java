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

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gained wisdom: Not only when the client disconnects, but also when the server
 * disconnects a connection, the disconnected method of a listener is called.
 */
public class KryonetConnectDisconnectTest {

    private static final Logger LOG = Logger.getLogger(KryonetConnectDisconnectTest.class.getName());

    private static class LogListener extends Listener {

        @Override
        public void connected(Connection connection) {
            LOG.log(Level.INFO, "connected from {0}", connection.toString());
        }

        @Override
        public void disconnected(Connection connection) {
            LOG.log(Level.INFO, "disconnected from {0}", connection.toString());
        }

        @Override
        public void received(Connection connection, Object object) {
            LOG.log(Level.INFO, "received message: {0} from {1}", new Object[]{object.toString(), connection.toString()});
        }

        public void disconnect(Connection connection) {
            LOG.log(Level.INFO, "will force disconnect of {0}", connection.toString());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // start server and add listener
        final Server server = new Server();
        server.start();
        server.bind(12_345);
        LogListener listener = new LogListener();
        server.addListener(listener);

        // start and connect client
        final Client client = new Client();
        client.start();
        client.connect(1_000, "localhost", 12_345);

        // send some messages
        client.sendTCP("hallo");
        client.sendTCP("bye");

        // force disconnect
        listener.disconnect(client);


        // set timer and stop after 5s
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                client.stop();
                server.stop();
            }
        }, 5_000);
    }
}
