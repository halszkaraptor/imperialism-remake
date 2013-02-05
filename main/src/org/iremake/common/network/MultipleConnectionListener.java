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
package org.iremake.common.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.util.HashMap;
import java.util.Map;
import org.iremake.common.network.messages.Message;
import org.tools.utils.TreeNode;

/**
 *
 */
public class MultipleConnectionListener extends Listener {

    private Map<Integer, HandlerChainExecutor> map = new HashMap<>(10);

    @Override
    public void connected(Connection connection) {
        HandlerChainExecutor manager = new HandlerChainExecutor(new TreeNode<Handler>());
        map.put(connection.getID(), manager);
    }

    @Override
    public void disconnected(Connection connection) {
        map.remove(connection.getID());
    }

    @Override
    public void received(Connection connection, final Object object) {
        if (object instanceof Message) {
            map.get(connection.getID()).receive((Message) object);
        } else {
            // disconnect
        }
    }

}
