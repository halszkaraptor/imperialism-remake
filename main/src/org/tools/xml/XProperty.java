/*
 * Copyright (C) 2010-12 Trilarion
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;

/**
 * Property implementation with a HashMap and XML import/export capability.
 *
 * If you want to use it as a Table, just convert to XTable and back
 */
// TODO null to XML and back? how is it handled
// TODO add statistics (how to make them transient)
public class XProperty implements XMLable {

    public final static String XMLNAME = "Properties";

    private static final Logger LOG = Logger.getLogger(XProperty.class.getName());
    private Map<String, String> map;
    private Map<String, Integer> stats = new HashMap<>(0);

    public XProperty(int capacity) {
        map = new HashMap<>(capacity);
    }

    public XProperty(Map<String, String> map) {
        this.map = map;
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    /**
     * Number of properties, i.e. entries in the map.
     *
     * @return The size.
     */
    public int size() {
        return map.size();
    }

    /**
     *
     * @param key
     * @return
     */
    public String get(String key) {
        // statistics
        Integer count = stats.get(key);
        if (count == null) {
            stats.put(key, 1);
        } else {
            stats.put(key, count + 1);
        }

        return map.get(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public Integer getCount(String key) {
        return stats.get(key);
    }

    /**
     *
     * @return
     */
    public Set<String> keySet() {
        return map.keySet();
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    /**
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        map.put(key, value);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putBoolean(String key, boolean value) {
        map.put(key, Boolean.toString(value));
    }

    /**
     *
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        map.put(key, Integer.toString(value));
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean removeKey(String key) {
        return map.remove(key) != null;
    }

    /**
     * Removes all entries.
     */
    public void clear() {
        map.clear();
    }

    /**
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    public boolean renameKey(String oldKey, String newKey) {
        if (map.containsKey(oldKey) && !map.containsKey(newKey)) {
            String value = get(oldKey);
            removeKey(oldKey);
            put(newKey, value);
            return true;
        } else {
            return false;
        }

    }

    /**
     *
     * @return
     */
    @Override
    public Element toXML() {
        Element element = XMLHandler.fromStringMap(map, XMLNAME);
        return element;
    }

    /**
     * Create from XML node.
     *
     * @param parent
     */
    @Override
    public void fromXML(Element parent) {

        if (parent == null || !XMLNAME.equals(parent.getLocalName())) {
            LOG.log(Level.SEVERE, "Empty XML node or node name wrong.");
            return; // TODO more than a LOG entry maybe
        }

        map = XMLHandler.toStringMap(parent);
    }
}