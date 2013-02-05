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

import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.tools.utils.TreeNode;

/**
 *
 */
public abstract class AbstractHandler implements Handler {

    private final String name;
    private final TreeNode<Handler> node;

    public AbstractHandler(String name, TreeNode<Handler> node) {
        this.name = name;
        this.node = node;
    }

    @Override
    public void send(Message message) {

    }

    @Override
    public void disconnect(TextMessage message) {
        if (message != null) {
            send(message);
        }
    }

    @Override
    public abstract void consume(Message message);

    @Override
    public void broadcastAll(Message message) {
        for (TreeNode<Handler> child: node.asUnmodifiableList()) {
            child.get().consume(message);
        }
    }

    @Override
    public void broadcastSpecific(Message message, String name) {

    }

    @Override
    public String name() {
        return name;
    }

}
