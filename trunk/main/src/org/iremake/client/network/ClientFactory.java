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

import org.iremake.common.network.ConnectedClient;
import org.iremake.common.network.handler.ErrorHandler;
import org.iremake.common.network.handler.Handler;
import org.tools.utils.TreeNode;

/**
 *
 */
public class ClientFactory {

    private ClientFactory() {
    }

    public static ConnectedClient createNewConnectedClient() {
        TreeNode<Handler> root = new TreeNode<Handler>();
        root.set(new ErrorHandler());
        return new ConnectedClient(root);
    }
}
