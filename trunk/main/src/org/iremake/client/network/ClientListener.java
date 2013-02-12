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

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.messages.Message;

/**
 *
 */
public class ClientListener extends Listener {

    private static final Logger LOG = Logger.getLogger(ClientListener.class.getName());
    private final ClientContext context;
    private ExecutorService threadPool;

    public ClientListener(ClientContext context) {
        this.context = context;
    }


    @Override
    public void connected(Connection c) {
        LOG.log(Level.FINE, "Client has connected.");
        threadPool = Executors.newFixedThreadPool(1);
    }

    @Override
    public void disconnected(Connection c) {
        LOG.log(Level.FINE, "Connection {0} disconnected.", c.getID());
        // TODO either we or somebody else disconnected
        threadPool.shutdown();
    }

    @Override
    public void received(Connection c, final Object object) {
        if (c.isConnected() && object instanceof Message) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    context.process((Message) object);
                }
            });
        } else {
            c.close();
        }
    }
}
