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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * Static methods for converting standard variables to DOM and forth
 *
 * appendChild(String) goes directly into the tag
 */
public class XMLHandler {

    private static final Logger LOG = Logger.getLogger(XMLHandler.class.getName());

    /**
     * To avoid instantiation.
     */
    private XMLHandler() {
    }

    /**
     * @param map
     * @param name
     * @return
     */
    // TODO does not work with null entries
    public static Element fromStringMap(Map<String, String> map, String name) {

        // new parent element
        Element parent = new Element(name);

        // loop over map entries and add new elements with according attributes
        for (Map.Entry<String, String> e : map.entrySet()) {
            Element entry = new Element("entry");
            entry.addAttribute(new Attribute("key", e.getKey()));
            entry.addAttribute(new Attribute("value", e.getValue()));
            parent.appendChild(entry);
        }
        return parent;
    }

    /**
     *
     * @param element
     * @return
     */
    public static Map<String, String> toStringMap(Element element) {

        Elements children = element.getChildElements();

        Map<String, String> map = new HashMap<>(children.size());

        for (int i = 0; i < children.size(); i++) {
            Element e = children.get(i);
            if (e.getAttribute("key") == null || e.getAttribute("value") == null) {
                throw new XMLException("Attributes missing in xml element during conversion to map.");
            }
            map.put(e.getAttributeValue("key"), e.getAttributeValue("value"));
        }

        return map;
    }

    /**
     *
     * @param element
     * @return
     */
    public static List<String> toStringList(Element element) {

        // get all child elements
        Elements children = element.getChildElements();
        int size = children.size();

        // put them one by one in the natural order into a new array list
        List<String> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(children.get(i).getValue());
        }
        return list;
    }

    /**
     *
     * @param list
     * @param name
     * @return
     */
    public static Element fromStringList(List<String> list, String name) {
        Element parent = new Element(name);
        for (String s : list) {
            Element child = new Element("e");
            child.appendChild(s);
            parent.appendChild(child);
        }
        return parent;
    }

    private final static String IntegerSeparator = ":";

    /**
     *
     * @param element
     * @return
     */
    public static List<Integer> toIntegerList(Element element) {
        int size = Integer.parseInt(element.getAttributeValue("size"));
        List<Integer> list = new ArrayList<>(size);

        if (size == 0 & element.getValue().length() == 0) {
            return list;
        }

        // TODO check, no childs, end node

        String[] integers = element.getValue().split(IntegerSeparator);
        // TODO check integers.length == size
        for (int i = 0; i < size; i++) {
            list.add(Integer.valueOf(integers[i]));
        }

        return list;
    }

    /**
     *
     * @param list
     * @param name
     * @return
     */
    public static Element fromIntegerList(List<Integer> list, String name) {
        Element parent = new Element(name);
        int size = list.size();
        parent.addAttribute(new Attribute("size", Integer.toString(size)));
        StringBuilder builder = new StringBuilder(size * 3);
        for (Integer value: list) {
            builder.append(value);
            builder.append(IntegerSeparator);
        }
        builder.deleteCharAt(builder.length() - 1); // delete the last separator
        parent.appendChild(builder.toString());

        return parent;
    }

    /**
     *
     * @param <T>
     * @param list
     * @param name
     * @return
     */
    public static <T extends XMLable> Element fromCollection(Collection<T> list, String name) {
        Element parent = new Element(name);
        for (T item: list) {
            Element child = item.toXML();
            parent.appendChild(child);
        }
        return parent;
    }

    /**
     *
     * @param <T>
     * @param parent
     * @param clazz
     * @return
     */
    public static <T extends XMLable> List<T> toList(Element parent, Class<T> clazz) {
        Elements children = parent.getChildElements();

        int size = children.size();
        List<T> list = new ArrayList<>(size);

        // parse each child and add to list
        for (int i = 0; i < size; i++) {
            // new instance of given class
            T element = null;
            try {
                element = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            element.fromXML(children.get(i));
            list.add(element);
        }

        return list;
    }
}