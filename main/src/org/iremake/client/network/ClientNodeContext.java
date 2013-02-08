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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.network.handler.ClientHandler;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.tools.utils.TreeNode;

/**
 * Want to hide the implementation of the tree and add functionality.
 */
public class ClientNodeContext {

    private static final Logger LOG = Logger.getLogger(ClientNodeContext.class.getName());
    private final TreeNode<ClientHandler> node;
    private final ClientContext context;

    public ClientNodeContext(ClientHandler handler, ClientContext context) {
        this.node = new TreeNode<>();
        node.set(handler);
        this.context = context;
    }

    private ClientNodeContext(TreeNode<ClientHandler> node, ClientContext context) {
        // TODO check not null
        this.node = node;
        this.context = context;
    }

    public void process(Message message) {
        node.get().process(message, this);
    }

    private void propagate(TreeNode<ClientHandler> child, Message message) {
        ClientNodeContext n = new ClientNodeContext(child, context);
        n.process(message);
    }

    public void propagate(Message message) {
        if (node.getChildCount() == 0) {
            LOG.log(Level.FINER, "Propagation of message {0} without any nodes to propagate to.", message.toString());
        }
        for (TreeNode<ClientHandler> child : node.asUnmodifiableList()) {
            propagate(child, message);
        }
    }

    public void propagate(Message message, String prefix) {
        for (TreeNode<ClientHandler> child : node.asUnmodifiableList()) {
            ClientHandler handler = child.get();
            if (handler.name().startsWith(prefix)) {
                propagate(child, message);
            }
        }
    }

    public void remove() {
        if (node.isRoot()) {
            // TODO error
        }
        node.removeUsFromTree();
    }

    public ClientNodeContext add(ClientHandler handler) {
        TreeNode<ClientHandler> n = wrap(handler);
        node.addChild(n);
        return new ClientNodeContext(n, context);
    }

    private static TreeNode<ClientHandler> wrap(ClientHandler handler) {
        TreeNode<ClientHandler> node = new TreeNode<>();
        node.set(handler);
        return node;
    }

    public void reply(Message message) {
        context.send(message);
    }

    public void disconnect(TextMessage error) {
        context.disconnect(error);
    }
}
