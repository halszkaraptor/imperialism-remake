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
 * Tests sending and receiving of messages. We see that the client receives
 * messages through the same thread, so a queued, threaded layer in between
 * seems appropriate.
 *
 * Also the connection on the client side is the client itself which is an
 * implementation of Connection.
 */
public class KryonetSimpleSendReceiveTest {

    private static final Logger LOG = Logger.getLogger(KryonetSimpleSendReceiveTest.class.getName());

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final Server server = new Server();
        server.start();
        server.bind(12345);

        final Client client = new Client();
        client.start();
        client.connect(5000, "localhost", 12345);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                LOG.log(Level.INFO, "Received message {0} in thread {1}", new Object[]{object.toString(), Thread.currentThread().toString()});
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        });

        final String text = "test";

        final Timer timer = new Timer("timer 1");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                server.sendToAllTCP(text);
            }
        }, 0, 100);

        final Timer timer2 = new Timer("timer 2");
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                LOG.log(Level.INFO, "cancel everything");
                timer.cancel();
                client.stop();
                server.stop();
                timer2.cancel();
            }
        }, 2000);
    }
}
