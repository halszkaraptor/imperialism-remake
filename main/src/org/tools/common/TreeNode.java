/*
 * Copyright (C) 2010-11 Trilarion
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
package org.tools.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.TreePath;

/**
 * A generic n-nary Tree implementation with some convenience functions.
 *
 * Each node contains one instance of type T and a list of children nodes as
 * well as one parent node at most. The rest is straightforward.
 *
 * This simple implementation is surely missing the Java standard libraries.
 *
 * @param <T> The type of the object that is stored in each node.
 */
public class TreeNode<T> {

    private static final Logger LOG = Logger.getLogger(TreeNode.class.getName());
    /**
     * Reference to the parent node.
     */
    private TreeNode<T> parent;
    /**
     * The data object stored in each node.
     */
    private T data;
    /**
     * A list of children nodes.
     */
    private List<TreeNode<T>> children = new LinkedList<>();

    /**
     * Set the data object associated with this node.
     *
     * @param data The data object.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Retrieves the data object associated with this node.
     *
     * @return The data object.
     */
    public T getData() {
        return data;
    }

    /**
     * Returns if there is a data object set for this node.
     *
     * @return True if there is a data object stored, false otherwise.
     */
    public boolean hasData() {
        return data == null;
    }

    /**
     * Convenience function.
     *
     * Clears the data object associated with this node.
     */
    public void clearData() {
        setData(null);
    }

    /**
     * Sets the parent node.
     *
     * @param parent The new parent node.
     */
    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    /**
     * Returns the reference to the parent node. Root has parent null.
     *
     * @return The parent node.
     */
    public TreeNode<T> getParent() {
        return parent;
    }

    /**
     * Convenience function.
     *
     * @return True, if there is a reference to a parent node stored, false
     * otherwise.
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Convenience function. Iterates over parents until a node is found without
     * a parent, which is the root node.
     *
     * If the tree structure is distorted, i.e. if circular dependencies occur,
     * the execution could loop here indefinitely. One would require more
     * careful checking to prevent this (e.g. mark all nodes already visited).
     *
     * @return The root node.
     */
    public TreeNode<T> getRoot() {
        TreeNode<T> node = this;
        while (node.getParent() != null) {
            node = node.getParent();
        }
        return node;
    }

    /**
     * Returns the child node specified by the given index.
     *
     * @param index The location of the child node in the list.
     * @return The child node.
     */
    public TreeNode<T> get(int index) {
        return children.get(index);
    }

    /**
     * Adds a new child node at a specified index position in the children's
     * list. Other children are shifted to higher index values. If the index is
     * out of bound, an exception will occur. The parent of the provided child
     * will be set to ourself.
     *
     * Comment: Specifying the index is probably never necessary and increases
     * the number of possible exceptions.
     *
     * Comment: Modifies also the child node's parent.
     *
     * @see List#add(int, Object)
     *
     * @param index The index in the children's list to insert the new child.
     * @param child The new child.
     */
    public void add(int index, TreeNode<T> child) {
        children.add(index, child);
        child.setParent(this);
    }

    /**
     * Adds a new child node to the end of the list of children. The parent of
     * the child is set to ourself.
     *
     * Comment: Modifies also the child node's parent.
     *
     * @param child The new child.
     */
    public void add(TreeNode<T> child) {
        add(children.size(), child);
    }

    /**
     * Removes the child node at a certain position and sets the parent of this
     * child to null.
     *
     * Comment: Modifies also the child node's parent.
     *
     * @param index The index indicating the child to remove.
     * @return The removed child node.
     */
    public TreeNode<T> remove(int index) {
        TreeNode<T> child = children.remove(index);
        child.setParent(null);
        return child;
    }

    /**
     * Removes a certain child node from the children's list and will certainly
     * throw an exception if the child node is not contained in the list. It's
     * parent field is set to null.
     *
     * Comment: This function modifies also the child node's parent.
     *
     * @param child The node to remove.
     */
    public void remove(TreeNode<T> child) {
        remove(indexOf(child));
    }

    /**
     * Returns the index of the first occurrence of the child node in the list
     * or -1 if it is not contained in the list.
     *
     * @param child The child node.
     * @return The index of the child node or -1 if not contained.
     */
    public int indexOf(TreeNode<T> child) {
        return children.indexOf(child);
    }

    /**
     * Replaces a child node at a certain position in the children's list with a
     * new child node.
     *
     * Comment: Modifies also the ex-child and new child nodes parents
     *
     * @param index The index in the children's list.
     * @param child The new child node.
     * @return The old child node.
     */
    public TreeNode<T> set(int index, TreeNode<T> child) {
        TreeNode<T> exchild = remove(index);
        add(index, child);
        return exchild;
    }

    /**
     * Removes all child nodes (setting their parent to null) from the
     * children's list of this node.
     *
     * Comment: Modifies also all children's parents.
     */
    public void removeAll() {
        for (TreeNode<T> node : children) {
            node.setParent(null);
        }
        children.clear();
    }

    /**
     * Returns the number of children nodes for this node.
     *
     * @return The number of children.
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Convenience function.
     *
     * @return True, if there are no children, false otherwise.
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * Checks for the existence of a certain node in the children's list.
     *
     * @param child The node to look for.
     * @return True if child is contained in the children's list, false
     * otherwise.
     */
    public boolean contains(TreeNode<T> child) {
        return children.contains(child);
    }

    /**
     * Returns an iterator of the children (as list) however without modifying
     * abilities.
     *
     * @return The iterator.
     */
    public Iterator<TreeNode<T>> iterator() {
        return asUnmodifiableList().iterator();
    }

    /**
     * Returns the children of this node as unmodifiable collection.
     *
     * @return The unmodifiable list.
     */
    public List<TreeNode<T>> asUnmodifiableList() {
        return Collections.unmodifiableList(children);
    }

    /**
     * A list of all nodes below the actual node.
     *
     * Comment: To make this list unmodifiable it would be more complex since
     * this function conveniently calls itself, so it must be a modifiable list
     * in this case. Be careful.
     *
     * @return The list.
     */
    public List<TreeNode<T>> subTreeNodesList() {
        List<TreeNode<T>> list = new LinkedList<>();
        list.add(this);
        for (TreeNode<T> node : children) {
            list.addAll(node.subTreeNodesList());
        }
        return list;
    }

    /**
     * Replaces this node with a another node correctly setting the current
     * parent and children in the new node. Parent and children of this node are
     * cleared and the new node is returned.
     *
     * Comment: Modifies also our parent, our children and the parent and
     * children of exchange.
     *
     * @param exchange The new node.
     * @return The new node again.
     */
    public TreeNode<T> replaceWith(TreeNode<T> exchange) {
        // we leave the work to other functions (doing an insert and a removal)
        insertBeforeUs(exchange);
        removeUsFromTree();
        return exchange;
    }

    /**
     * Removes this node from the tree, deleting all references from the parent
     * and the children. All current children are added to the children of the
     * parent node if there is one.
     *
     * Comment: This operation might separate the tree, in the case that there
     * was no parent node and more than one child node.
     *
     * Comment: Modifies also the parent and the children.
     */
    public void removeUsFromTree() {
        if (parent != null) {
            // remove us from the parent if there is one (setting our parent to null)
            parent.remove(this);
            // add our children to parent
            for (TreeNode<T> node : children) {
                parent.add(node);
            }
            // now we need to quietly remove our children (without changing their parent)
            children.clear();
        } else {
            // no parent, just remove all children (and setting their parent to null)
            removeAll();
        }
    }

    /**
     * Insert a node before the current in the tree hierarchy. The new node is
     * added to the children's list of the current parent and is set as new
     * parent. Furthermore we will make sure to be the only child of insert,
     * i.e. we want only 'fresh' nodes to be inserted.
     *
     * Comment: Modifies parent and children in insert and the parent of us.
     *
     * @param insert The node to be inserted.
     * @return The inserted node.
     */
    public TreeNode<T> insertBeforeUs(TreeNode<T> insert) {
        // first taking care about the parents
        if (parent != null) {
            // remove us from parent and insert 'insert' in parent instantly modifying inserts current parent and our current parent
            parent.set(indexOfUsInParent(), insert);
        } else {
            // insert is the new root, must have null as parent and our parent anyway is null, so we keep it
            insert.setParent(null);
        }
        // now the childrens, we only have to take care about insert (set us as the only child)
        insert.removeAll();
        insert.add(this);
        return insert;
    }

    /**
     * Internal function used to look this node up in the parent node.
     *
     * @return The index of us in the parent's children list.
     */
    private int indexOfUsInParent() {
        int index = parent.indexOf(this);
        if (index == -1) {
            // the index should never be -1 in a well defined tree, however we check for it
            LOG.log(Level.SEVERE, "Node not registered as child in parent.", this);
        }
        return index;
    }

    /**
     * Checks if the tree has a well organized (parents of children and children
     * as consistent) structure. Only test downwards (towards the leaves) of the
     * tree.
     *
     * @return True, if the tree is well organized.
     */
    public boolean hasTreeStructure() {
        return checkSubTree(parent);
    }

    /**
     * Internal function checking the subtree for well organization. Checks
     * recursively if parent of children is set to this.
     *
     * Comment: If the subtree is indeed completely out of order (i.e. circular)
     * this might result in endless recursion until an exception is thrown!
     *
     * @param parent The node to check.
     * @return True, if
     */
    private boolean checkSubTree(TreeNode<T> parent) {
        // check our parent with the parent in whos children list we are (consistency check)
        if (getParent() != parent) {
            return false;
        }
        for (TreeNode<T> node : children) {
            // recursive check of sub-nodes
            boolean b = checkSubTree(node);
            if (b == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sorts the nodes in the children list of the current node according to a
     * specified comparator.
     *
     * @param comparator The comparator to compare different nodes.
     */
    public void sort(Comparator<? super TreeNode<T>> comparator) {
        Collections.sort(children, comparator);
    }

    /**
     * Convenience function. Sorts all children lists of all nodes below the
     * current node.
     *
     * @param comparator The comparator to compare different nodes.
     */
    public void sortSubTree(Comparator<? super TreeNode<T>> comparator) {
        sort(comparator);
        for (TreeNode<T> node : children) {
            node.sortSubTree(comparator);
        }
    }

    /**
     * Returns a path for a node. A Path can be constructed from an Array of
     * nodes that describe a path from the root to a specified node. In order to
     * obtain this we start with the node and go up the tree until we arrive at
     * the root and then we reverse this list.
     *
     * @param <T> Type of object that is stored in each node.
     * @param node A node.
     * @return A TreePath to this node.
     */
    public static <T> TreePath getPathFor(TreeNode<T> node) {
        List<Object> list = new ArrayList<>(5);

        // add all parent nodes to the list
        TreeNode<T> n = node;
        while (n != null) {
            list.add(n);
            n = n.getParent();
        }
        Collections.reverse(list);

        // Convert array of nodes to TreePath
        return new TreePath(list.toArray());
    }

    /**
     * Refers to the toString function of the data object which is probably the
     * more interesting part of a node. Can be used in sorting.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return data.toString();
    }
}