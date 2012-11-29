/*
 * Copyright (C) 2011 Trilarion
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
package org.tools.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class realizes a simple two key hash map, i.e. a 2D hash map. Data can
 * be stored for fast access according to keys in two different categories.
 *
 * We achieve this internally by nesting ordinary hash maps. A generalization to
 * n-key hash maps would be nice, but is not straight forward with the current
 * approach. Also performance will depend on the order of the two keys.
 *
 * @param <K1> A type for key one.
 * @param <K2> A type for key two.
 * @param <V> A type for the elements.
 */
public class TwoKeysHashMap<K1, K2, V> {

    /**
     * The two-fold nested map is the internal storage.
     */
    private Map<K1, Map<K2, V>> map;

    /**
     * Creates a new generic hash map.
     */
    public TwoKeysHashMap() {
        map = new HashMap<>();
    }

    /**
     * Given a pair of keys, returns the element stored by these keys or null
     * otherwise.
     *
     * @param key1 Key one.
     * @param key2 Key two.
     * @return The element stored by these two keys.
     */
    public V get(K1 key1, K2 key2) {
        Map<K2, V> sub = map.get(key1);
        if (sub == null) {
            return null;
        }
        return sub.get(key2);
    }

    /**
     * Stores an element in the hash map and both keys so that it can later be
     * retrieved.
     *
     * @param key1 Key one.
     * @param key2 Key two.
     * @param value The associated element.
     */
    public void put(K1 key1, K2 key2, V value) {
        Map<K2, V> sub = map.get(key1);
        if (sub == null) {
            // there is no element with key1 ever stored, must create new sub hash map
            sub = new HashMap<>();
            sub.put(key2, value);
            map.put(key1, sub);
        } else {
            // just put it in the sub hash map
            sub.put(key2, value);
        }
    }

    /**
     * Removes the element specified by key one and key two from the map.
     *
     * @param key1 Key one.
     * @param key2 Key two.
     * @return The removed element.
     */
    public V remove(K1 key1, K2 key2) {
        Map<K2, V> sub = map.get(key1);
        if (sub == null) {
            // there is no element with such a key one.
            return null;
        }
        // remove element with key two from map consisting of all elements with key one.
        V value = sub.remove(key2);
        // if the sub map is then empty (i.e. no more elements with key one) remove it also (to save storage)
        if (sub.isEmpty()) {
            map.remove(key1);
        }
        return value;
    }

    /**
     * Returns a map from key two to elements that contain all entries for a
     * certain key one.
     *
     * @param key1 The key one.
     * @return The map (unmodifiable).
     */
    public Map<K2, V> getKeyMap(K1 key1) {
        return Collections.unmodifiableMap(map.get(key1));
    }

    /**
     * Returns the overall number of elements in this map.
     *
     * @return The number of elements.
     */
    public int size() {
        int size = 0;
        // iterate over each sub map for each key one
        for (Map<K2, V> sub : map.values()) {
            size += sub.size();
        }
        return size;
    }

    /**
     * Removes all elements from the map.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Is the map empty.
     *
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }
}