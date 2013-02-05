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
import org.iremake.common.network.messages.Message;

/**
 *
 */
public class SingleConnectionListener extends Listener {

    private final HandlerChainExecutor executor;

    public SingleConnectionListener(HandlerChainExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void received(Connection connection, final Object object) {
        if (object instanceof Message) {
            executor.receive((Message) object);
        } else {
            // disconnect
        }
    }
}
