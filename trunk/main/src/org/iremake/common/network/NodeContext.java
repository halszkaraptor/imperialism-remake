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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.handler.Handler;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.tools.utils.TreeNode;

/**
 * Want to hide the implementation of the tree and add functionality.
 */
public class NodeContext {

    private static final Logger LOG = Logger.getLogger(NodeContext.class.getName());
    private final TreeNode<Handler> node;
    private final NetworkContext context;
    private final Integer id;

    public NodeContext(Handler handler, NetworkContext context, Integer id) {
        this.node = new TreeNode<>();
        node.set(handler);
        this.context = context;
        this.id = id;
    }

    private NodeContext(TreeNode<Handler> node, NetworkContext context, Integer id) {
        // TODO check not null
        this.node = node;
        this.context = context;
        this.id = id;
    }

    public Integer getID() {
        return id;
    }

    public void process(Message message) {
        node.get().process(message, this);
    }

    private void propagate(TreeNode<Handler> child, Message message) {
        NodeContext n = new NodeContext(child, context, id);
        n.process(message);
    }

    public void propagate(Message message) {
        if (node.getChildCount() == 0) {
            LOG.log(Level.FINER, "Propagation of message {0} without any nodes to propagate to.", message.toString());
        }
        for (TreeNode<Handler> child : node.asUnmodifiableList()) {
            propagate(child, message);
        }
    }

    public void propagate(Message message, String prefix) {
        for (TreeNode<Handler> child : node.asUnmodifiableList()) {
            Handler handler = child.get();
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

    public NodeContext add(Handler handler) {
        TreeNode<Handler> n = wrap(handler);
        node.addChild(n);
        return new NodeContext(n, context, id);
    }

    private static TreeNode<Handler> wrap(Handler handler) {
        TreeNode<Handler> node = new TreeNode<>();
        node.set(handler);
        return node;
    }

    public String name() {
        return context.name(id);
    }

    public void reply(Message message) {
        context.send(id, message);
    }

    public void broadcast(Message message) {
        context.broadcast(message);
    }

    public void disconnect(TextMessage error) {
        context.disconnect(id, error);
    }
}
