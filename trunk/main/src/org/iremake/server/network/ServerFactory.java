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

import org.iremake.common.network.ClientContext;
import org.iremake.common.network.handler.ErrorHandler;
import org.iremake.common.network.NodeContext;
import org.iremake.server.network.handler.ClientNameHandler;
import org.iremake.server.network.handler.VerifyVersionHandler;

/**
 *
 */
public class ServerFactory {

    private ServerFactory() {
    }

    public static ClientContext createNewConnectedClient() {
        NodeContext root = NodeContext.createRoot(new ErrorHandler());
        NodeContext node = root.add(new VerifyVersionHandler());
        node.add(new ClientNameHandler());

        return new ClientContext(root);
    }

}
