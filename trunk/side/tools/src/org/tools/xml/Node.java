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
package org.tools.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * Wrapper around Element. Inherently thread safe.
 */
public class Node {

    private static final Logger LOG = Logger.getLogger(Node.class.getName());
    private final static String IntegerSeparator = ":";
    private final Element element;

    public Node(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        element = new Element(name);
    }

    public Node(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null.");
        }
        this.element = element;
    }

    /**
     * Conversion: Map<String, String> -> xml
     *
     * For each <Key,Value> entry a new xml node is created and key, value are
     * stored as attributes.
     *
     * @param map
     * @param name
     * @return
     */
    // TODO does not work with null entries
    public Node(Map<String, String> map, String name) {
        this(name);

        // loop over map entries and add new elements with according attributes
        for (Map.Entry<String, String> e : map.entrySet()) {
            Node child = new Node("entry");
            child.addAttribute("key", e.getKey());
            child.addAttribute("value", e.getValue());
            appendChild(child);
        }
    }

    /**
     * Conversion: Iterable<T extends XMLAble> -> xml
     *
     * Each item of the Iterable is converted to its own xml node and all nodes
     * are appended to a freshly created node.
     *
     * @param <T>
     * @param list
     * @param name
     * @return
     */
    public <T extends FullXMLable> Node(Iterable<T> list, String name) {
        this(name);

        for (T item : list) {
            Node child = item.toXML();
            appendChild(child);
        }
    }

    /**
     * Conversion: List<String> -> xml
     *
     * @param list
     * @param name
     * @return
     */
    public <T extends String> Node(List<T> list, String name) {
        this(name);

        for (String s : list) {
            Node child = new Node("e");
            child.appendChild(s);
            appendChild(child);
        }
    }

    /**
     * Conversion: List<Integer> -> xml
     *
     * @param list
     * @param name
     * @return
     *
     * public <T extends Integer> Node(List<T> list, String name) { this(name);
     *
     * int size = list.size(); addAttribute("size", Integer.toString(size));
     * StringBuilder builder = new StringBuilder(size * 3); for (Integer value :
     * list) { builder.append(value); builder.append(IntegerSeparator); }
     * appendChild(builder.toString()); }
     */
    public void appendChild(Node child) {
        element.appendChild(child.element);
    }

    public void appendChild(String content) {
        element.appendChild(content);
    }

    public void checkNode(String name) {
        if (name == null || !name.equals(element.getLocalName())) {
            LOG.log(Level.SEVERE, "Check of element {0} to name {1} failed.", new Object[]{element, name});
            throw new RuntimeException("Not the right XML node.");
        }
    }

    public int getChildCount() {
        return element.getChildElements().size();
    }

    public Node getFirstChild(String name) {
        Element child = element.getFirstChildElement(name);
        if (child == null) {
            LOG.log(Level.SEVERE, "Element {0} does not have a child with name {1}", new Object[]{element, name});
            throw new RuntimeException("XML node does not have requested child.");
        }
        return new Node(child);
    }

    public Iterable<Node> getChildren() {
        final Elements elements = element.getChildElements();
        return new Iterable<Node>() {
            @Override
            public Iterator<Node> iterator() {
                return new Iterator<Node>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < elements.size();
                    }

                    @Override
                    public Node next() {
                        return new Node(elements.get(index++));
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Cannot remove an Element here.");
                    }
                };
            }
        };
    }

    public void addAttribute(String key, String value) {
        element.addAttribute(new Attribute(key, value));
    }
    
    public boolean hasAttribute(String key) {
        return element.getAttribute(key) != null;
    }

    public String getAttributeValue(String key) {
        String value = element.getAttributeValue(key);
        if (value == null) {
            LOG.log(Level.SEVERE, "Element {0} does not have an attribute with name {1}", new Object[]{element, key});
            throw new RuntimeException("XML node does not have requested attribute.");
        }
        return value;
    }

    public int getAttributeValueAsInt(String key) {
        return Integer.parseInt(getAttributeValue(key));
    }

    public String getValue() {
        return element.getValue();
    }

    public Document getDocument() {
        Document document = element.getDocument();
        if (document == null) {
            document = new Document(element);
        }
        return document;
    }

    /**
     * Conversion: xml -> Map<String, String>
     *
     * @param node
     * @return
     */
    public Map<String, String> toStringMap() {

        Map<String, String> map = new HashMap<>(getChildCount());

        for (Node child : getChildren()) {
            map.put(child.getAttributeValue("key"), child.getAttributeValue("value"));
        }

        return map;
    }

    /**
     * Conversion: xml -> List<String>
     *
     * @param node
     * @return
     */
    public List<String> toStringList() {

        List<String> list = new ArrayList<>(getChildCount());

        // put them one by one in the natural order into a new array list

        for (Node child : getChildren()) {
            list.add(child.getValue());
        }
        return list;
    }

    /**
     * Conversion: xml -> List<Integer>
     *
     * @param element
     * @return
     */
    public List<Integer> toIntegerList() {
        int size = getAttributeValueAsInt("size");
        List<Integer> list = new ArrayList<>(size);

        if (size == 0 && getValue().length() == 0) {
            return list;
        }

        // TODO check, no childs, end node

        String[] integers = getValue().split(IntegerSeparator);
        // TODO check integers.length == size
        for (int i = 0; i < size; i++) {
            list.add(Integer.valueOf(integers[i]));
        }

        return list;
    }

    /**
     * Conversion: xml -> List<T extends XMLAble>
     *
     * @param <T>
     * @param parent
     * @param clazz
     * @return
     */
    public <T extends ReadXMLable> List<T> toList(Class<T> clazz) {
        int size = getChildCount();
        List<T> list = new ArrayList<>(size);

        // parse each child and add to list
        for (Node child : getChildren()) {
            // new instance of given class
            T item = null;
            try {
                item = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            item.fromXML(child);
            list.add(item);
        }

        return list;
    }
}
