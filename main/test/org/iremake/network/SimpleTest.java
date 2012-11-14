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
package org.iremake.network;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.network.ClientManager;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.server.network.ServerManager;

/**
 *
 */
public class SimpleTest {
    
    private static final Logger LOG = Logger.getLogger(SimpleTest.class.getName());    

    public static void main(String args[]) throws InterruptedException {
        
        ServerManager server = new ServerManager();
        if (!server.start()) {
            LOG.log(Level.SEVERE, "Server could not start.");
            return;
        }
        
        ClientManager client = new ClientManager();
        if (!client.start()) {
            LOG.log(Level.SEVERE, "Client could not start.");
            return;
        }
        
        TextMessage message = new TextMessage("Hello World!");
        client.send(message);
        
        Thread.sleep(5000);
        
        client.stop();
        
        server.stop();

    }
}
