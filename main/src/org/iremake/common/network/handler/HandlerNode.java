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
package org.iremake.common.network.handler;

import org.iremake.common.network.messages.Message;
import org.tools.utils.TreeNode;

/**
 * Want to hide the implementation of the tree and add functionality.
 */
public class HandlerNode {

    private TreeNode<Handler> node;

    public HandlerNode(TreeNode<Handler> node) {
        this.node = node;
    }

    public void process(Message message) {
        node.get().process(message, this);
    }

    public void propagate(Message message) {
        for (TreeNode<Handler> child : node.asUnmodifiableList()) {
            child.get().process(message, new HandlerNode(child));
        }
    }

    public void propagate(Message message, String prefix) {
        for (TreeNode<Handler> child : node.asUnmodifiableList()) {
            Handler handler = child.get();
            if (handler.name().startsWith(prefix)) {
                handler.process(message, new HandlerNode(child));
            }
        }
    }

    public void remove() {
        if (node.isRoot()) {
            // TODO error
        }
        node.removeUsFromTree();
    }

    public HandlerNode add(Handler handler) {
        TreeNode<Handler> n = wrap(handler);
        node.addChild(n);
        return new HandlerNode(n);
    }

    public static HandlerNode createRoot(Handler handler) {
        return new HandlerNode(wrap(handler));
    }

    private static TreeNode<Handler> wrap(Handler handler) {
        TreeNode<Handler> node = new TreeNode<>();
        node.set(handler);
        return node;
    }
}
