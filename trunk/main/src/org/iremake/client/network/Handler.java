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
package org.iremake.client.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.NumberMessage;

/**
 * Handling of received messages on the client side.
 */
public class Handler extends Listener {
    
    @Override
    public void connected(Connection connection) {
        
    }
    
    @Override
    public void disconnected(Connection connection) {
        
    }
    
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Message) {
            if (object instanceof NumberMessage) {
                NumberMessage message = (NumberMessage) object;
                if (NumberMessage.Type.ID.equals(message.getType())) {
                    System.out.println("[Client] My new ID is: " + message.getNumber());
                }
            }
        } else {
            // TODO log unexpected types of objects
        }
    }    
}
