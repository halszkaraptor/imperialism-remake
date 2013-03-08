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
 * We have a tree of client handlers, that constitute of processing tree for messages. Now we built some infrastructure around that tree, mostly adding possible client actions.
 *
 * We completely hide the implementation of the tree and the way how we add handlers.
 */
public class ClientNodeContext {

    private static final Logger LOG = Logger.getLogger(ClientNodeContext.class.getName());
    private final TreeNode<ClientHandler> node;
    private final ClientContext context;

    /**
     * Make a new Node with the given handler and set the context. This will create a new tree and the node will be the root node.
     *
     * @param handler Handler for making a new root node of the processing tree.
     * @param context The client context.
     */
    public ClientNodeContext(ClientHandler handler, ClientContext context) {
        if (handler == null || context == null) {
            RuntimeException ex = new IllegalArgumentException("Arguments cannot be null.");
            LOG.log(Level.SEVERE, null, ex);
            throw ex;
        }
        this.node = new TreeNode<>();
        node.set(handler);
        this.context = context;
    }

    /**
     * Privately used when we added a new handler, we created a node of the tree and now make a node context.
     *
     * @param node New node.
     * @param context The client context.
     */
    private ClientNodeContext(TreeNode<ClientHandler> node, ClientContext context) {
        if (node == null || node.get() == null || context == null) {
            RuntimeException ex = new IllegalArgumentException("Arguments cannot be null. Node must have handler.");
            LOG.log(Level.SEVERE, null, ex);
            throw ex;
        }
        this.node = node;
        this.context = context;
    }

    /**
     * Tell the handler of this Node to process the message.
     *
     * @param message Message to be processed
     */
    public void process(Message message) {
        node.get().process(message, this);
    }

    /**
     * Privately used to propagate a message to a child node. Constructs a NodeContext around the child node and process.
     *
     * @param child
     * @param message
     */
    private void propagate(TreeNode<ClientHandler> child, Message message) {
        ClientNodeContext n = new ClientNodeContext(child, context);
        n.process(message);
    }

    /**
     * Propagate a message to all children of the actual node.
     *
     * @param message
     */
    public void propagate(Message message) {
        if (node.getChildCount() == 0) {
            LOG.log(Level.FINER, "Propagation of message {0} without any nodes to propagate to.", message.toString());
        }
        for (TreeNode<ClientHandler> child : node.asUnmodifiableList()) {
            propagate(child, message);
        }
    }

    /**
     * Propagate a message to all children whose name starts with a certain prefix.
     *
     * @param message
     * @param prefix
     */
    public void propagate(Message message, String prefix) {
        for (TreeNode<ClientHandler> child : node.asUnmodifiableList()) {
            ClientHandler handler = child.get();
            if (handler.name().startsWith(prefix)) {
                propagate(child, message);
            }
        }
    }

    /**
     * Removes this node and thereby this handler from the processing tree at this position.
     */
    public void remove() {
        if (node.isRoot()) {
            RuntimeException ex = new RuntimeException("Cannot remove root node.");
            LOG.log(Level.SEVERE, null, ex);
            throw ex;
        }
        node.removeUsFromTree();
    }

    /**
     * Adds a new node as child node to the current node. The new node will hold the given handler.
     *
     * @param handler
     * @return The Node context for the new child, so we directly can use it.
     */
    public ClientNodeContext add(ClientHandler handler) {
        TreeNode<ClientHandler> n = wrap(handler);
        node.addChild(n);
        return new ClientNodeContext(n, context);
    }

    /**
     * Creates a new node and sets a handler.
     *
     * @param handler
     * @return The new node.
     */
    private static TreeNode<ClientHandler> wrap(ClientHandler handler) {
        TreeNode<ClientHandler> node = new TreeNode<>();
        node.set(handler);
        return node;
    }

    /**
     * Sends a message to the server.
     *
     * @param message
     */
    public void reply(Message message) {
        context.send(message);
    }

    /**
     * Disconnects from the server.
     * 
     * @param error
     */
    public void disconnect(TextMessage error) {
        context.disconnect(error);
    }
}
