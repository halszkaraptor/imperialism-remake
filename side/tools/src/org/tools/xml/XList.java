/*
 * Copyright (C) 2012 Trilarion
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
package org.tools.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A List consisting of generic XMLables and also implementing the ListModel
 * interface. Therefore it can be used directly as model for a JList. Can
 * serialize itself and its elements to XML.
 *
 * Because of type erasure the type of the elements to be serialized to if
 * reading from XML must be known beforehand and is given in the constructor.
 * Then only elements of this type will be read.
 *
 * The typical use case is that all elements have the same type. Otherwise one
 * would need to store the type during serialization and recreate upon
 * de-serialization which is not supported for now.
 *
 * Also the List can be kept sorted at all times if wished.
 *
 * @param <E>
 */
// TODo having a set also, could be sorted automatically (TreeSet)
public class XList<E extends FullXMLable> implements ListModel<E>, Iterable<E>, FullXMLable {

    private String XML_NAME;
    private static final Logger LOG = Logger.getLogger(XList.class.getName());
    /**
     * Standard comparator, compares on the toString methods.
     */
    private final Comparator<E> comparator = new Comparator<E>() {
        @Override
        public int compare(E o1, E o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };
    private List<E> list = new ArrayList<>(4);
    private transient List<ListDataListener> listeners = new LinkedList<>();
    private Class<E> clazz;

    /**
     * Empty list with initial capacity.
     *
     * @param clazz
     */
    public XList(Class<E> clazz) {
        this(clazz, "ListOf-" + clazz.getSimpleName());
    }

    public XList(Class<E> clazz, String XMLName) {
        this.clazz = clazz;
        this.XML_NAME = XMLName;
    }

    /**
     * Size of the list.
     *
     * @return Number of elements in the list.
     */
    @Override
    public int getSize() {
        return list.size();
    }

    /**
     * Checks if the index is within the list.
     *
     * @param index
     * @return False if not.
     */
    private boolean indexOutOfBounds(int index) {
        boolean b = index >= list.size() || index < 0;
        if (b) {
            LOG.log(Level.INFO, "Index out of bounds");
        }
        return b;
    }

    /**
     *
     */
    public void sort() {
        Collections.sort(list, comparator);

        fireListChanged();
    }

    /**
     * Returns the element at a specific position.
     *
     * @param index
     * @return
     */
    @Override
    public E getElementAt(int index) {
        if (indexOutOfBounds(index)) {
            return null;
        }
        return list.get(index);
    }

    /**
     * Replaces the element at a specific position.
     *
     * @param index
     * @param element
     */
    public void setElementAt(int index, E element) {
        if (indexOutOfBounds(index)) {
            return;
        }
        list.set(index, element);

        fireListChanged();
    }

    /**
     * Returns true if the list contains the specific element.
     *
     * @param element
     * @return
     */
    public boolean contains(E element) {
        return list.contains(element);
    }

    /**
     * Inserts an element at a specific position, all others shift to the right.
     *
     * @param index
     * @param element
     */
    public void insertElementAt(int index, E element) {
        if (indexOutOfBounds(index)) {
            return;
        }
        list.add(index, element);

        fireListChanged();
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param element
     */
    public void addElement(E element) {
        list.add(element);

        fireListChanged();
    }

    /**
     * Removes an element at a specific position.
     *
     * @param index
     */
    public void removeElementAt(int index) {
        if (indexOutOfBounds(index)) {
            return;
        }
        list.remove(index);

        // no sorting neccessary after deletion

        fireListChanged();
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    /**
     * Clears the list.
     */
    public void clear() {
        list.clear();
        fireListChanged();
    }

    /**
     * Adds a listener.
     *
     * @param l Listener.
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener.
     *
     * @param l Listener.
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    /**
     * Tells the listeners that everything has changed.
     */
    private void fireListChanged() {
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, list.size() - 1);
        for (ListDataListener l : listeners) {
            l.contentsChanged(event);
        }
    }

    /**
     * Listeners are not saved.
     *
     * @return
     */
    @Override
    public Node toXML() {
        Node parent = new Node(XML_NAME);

        // store each element as child
        for (E element : list) {
            Node child = element.toXML();
            parent.appendChild(child);
        }

        return parent;
    }

    /**
     * Listeners are not loaded.
     *
     * @param parent
     */
    @Override
    public void fromXML(Node parent) {

        // first clear
        clear();

        parent.checkNode(XML_NAME);

        int size = parent.getChildCount();
        list = new ArrayList<>(size);

        // parse each child and add to list
        for (Node child : parent.getChildren()) {
            // new instance of given class
            E element = null;
            try {
                element = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            element.fromXML(child);
            list.add(element);
        }
    }
}
