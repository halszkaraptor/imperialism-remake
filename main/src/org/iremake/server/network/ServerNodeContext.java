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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.server.network.handler.ServerHandler;
import org.tools.utils.TreeNode;

/**
 * Want to hide the implementation of the tree and add functionality.
 */
public class ServerNodeContext {

    private static final Logger LOG = Logger.getLogger(ServerNodeContext.class.getName());
    private final TreeNode<ServerHandler> node;
    private final ServerContext context;
    private final Integer id;

    public ServerNodeContext(ServerHandler handler, ServerContext context, Integer id) {
        this.node = new TreeNode<>();
        node.set(handler);
        this.context = context;
        this.id = id;
    }

    private ServerNodeContext(TreeNode<ServerHandler> node, ServerContext context, Integer id) {
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

    private void propagate(TreeNode<ServerHandler> child, Message message) {
        ServerNodeContext n = new ServerNodeContext(child, context, id);
        n.process(message);
    }

    public void propagate(Message message) {
        if (node.getChildCount() == 0) {
            LOG.log(Level.FINER, "Propagation of message {0} without any nodes to propagate to.", message.toString());
        }
        for (TreeNode<ServerHandler> child : node.asUnmodifiableList()) {
            propagate(child, message);
        }
    }

    public void propagate(Message message, String prefix) {
        for (TreeNode<ServerHandler> child : node.asUnmodifiableList()) {
            ServerHandler handler = child.get();
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

    public ServerNodeContext add(ServerHandler handler) {
        TreeNode<ServerHandler> n = wrap(handler);
        node.addChild(n);
        return new ServerNodeContext(n, context, id);
    }

    private static TreeNode<ServerHandler> wrap(ServerHandler handler) {
        TreeNode<ServerHandler> node = new TreeNode<>();
        node.set(handler);
        return node;
    }

    public void setName(String name) {
        context.setName(id, name);
    }

    public String getName() {
        return context.getName(id);
    }

    public void reply(Message message) {
        context.send(id, message);
    }

    public void broadcast(Message message) {
        context.broadcast(message);
    }

    public void broadcast(String domain, Message message) {
        context.broadcast(domain, message);
    }

    public void disconnect(TextMessage error) {
        context.disconnect(id, error);
    }

    public boolean hasHandler(String domain) {
        for (TreeNode<ServerHandler> n: node.subTreeNodesList()) {
            if (n.get().name().startsWith(domain)) {
                return true;
            }
        }
        return false;
    }
}
