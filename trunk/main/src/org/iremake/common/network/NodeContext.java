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

import org.iremake.common.network.NetworkContext;
import org.iremake.common.network.handler.Handler;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.tools.utils.TreeNode;

/**
 * Want to hide the implementation of the tree and add functionality.
 */
public class NodeContext {

    private final TreeNode<Handler> node;
    private final NetworkContext context;
    private final Integer id;

    public NodeContext(TreeNode<Handler> node, NetworkContext context, Integer id) {
        // TODO check not null
        this.node = node;
        this.context = context;
        this.id = id;
    }

    public void process(Message message) {
        node.get().process(message, this);
    }

    public void propagate(Message message) {
        for (TreeNode<Handler> child : node.asUnmodifiableList()) {
            child.get().process(message, new NodeContext(child, context, id));
        }
    }

    public void propagate(Message message, String prefix) {
        for (TreeNode<Handler> child : node.asUnmodifiableList()) {
            Handler handler = child.get();
            if (handler.name().startsWith(prefix)) {
                handler.process(message, new NodeContext(child, context, id));
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

    public static NodeContext createRoot(Handler handler) {
        // return new NodeContext(wrap(handler));
        return null;
    }

    private static TreeNode<Handler> wrap(Handler handler) {
        TreeNode<Handler> node = new TreeNode<>();
        node.set(handler);
        return node;
    }

    public void disconnect(TextMessage error) {
        // TODO if we aren't already disconnected, disconnect this connection
    }

    public String name() {
        // TODO get connection id plus name
        return null;
    }

    public void reply(Message message) {
        // TODO send to your own
    }

    public void broadcast(Message message) {
        // TODO tell all others
    }
}
