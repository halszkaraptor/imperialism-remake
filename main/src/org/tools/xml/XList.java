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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import nu.xom.Element;
import nu.xom.Elements;

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
 */
public class XList<E extends XMLable> implements ListModel<E>, XMLable {

    private final String XMLNAME;

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
    private List<E> list;
    private transient List<ListDataListener> listeners = new LinkedList<>();
    private Class<E> clazz;
    private boolean keepSorted = false;

    /**
     * Empty list with initial capacity.
     */
    public XList(Class<E> clazz) {
        this(new ArrayList<E>(4), clazz, "ListOf-" + clazz.getSimpleName());
    }

    public XList(Class<E> clazz, String XMLName) {
        this(new ArrayList<E>(4), clazz, XMLName);
    }

    /**
     * Sets the list from outside. Remains as is.
     *
     * @param list
     */
    public XList(List<E> list, Class<E> clazz, String XMLName) {
        this.list = list;
        this.clazz = clazz;
        XMLNAME = XMLName;
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
     *
     * @param keepSorted
     * @return
     */
    public void setKeepSorted(boolean keepSorted) {
        this.keepSorted = keepSorted;
        maybeSort();
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

    private void maybeSort() {
        if (keepSorted) {
            Collections.sort(list, comparator);
        }
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

        maybeSort();

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

        maybeSort();

        fireListChanged();
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param element
     */
    public void addElement(E element) {
        list.add(element);

        maybeSort();

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
    public Element toXML() {
        Element parent = new Element(XMLNAME);

        // store each element as child
        for (E element : list) {
            Element child = element.toXML();
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
    public void fromXML(Element parent) {

        // first clear
        clear();

        if (parent == null || !XMLNAME.equals(parent.getLocalName())) {
            LOG.log(Level.SEVERE, "Empty XML node or node name wrong.");
            return;
        }

        // TODO test names
        Elements children = parent.getChildElements();

        int size = children.size();
        list = new ArrayList<>(size);

        // parse each child and add to list
        for (int i = 0; i < size; i++) {
            // new instance of given class
            E element = null;
            try {
                element = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            element.fromXML(children.get(i));
            list.add(element);
        }

        // maybe do a sort
        maybeSort();

        // list changed
        fireListChanged();
    }
}
