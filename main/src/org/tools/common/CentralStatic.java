/*
 * Copyright (C) 2010,2011 Trilarion
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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A very simple storage for all kind of static, global objects we might need
 * during the run of the program. In general, using static, global variables is
 * not a good idea and especially here an "unchecked" type cast cannot be
 * circumvented. (We cannot know what type was stored and what is expected, even
 * generics do not help because of type erasure.) So the generic cast is added
 * for convenience so that we do not have to suppress warning somewhere else in
 * the code, however it does not make anything safer. Also this class could
 * develop into a memory leak if objects are still stored which are not needed
 * anymore.
 *
 * Apart from this fallback, it can be sometimes handy (e.g. for storing global
 * language properties object).
 */
public class CentralStatic {

    /**
     * Static HashMap (for performance) storing an object with an ID string
     */
    private static Map<String, Object> storage = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private CentralStatic() {
    }

    /**
     * Just stores an object in the global map, does overwrite existing entries
     * with equal key, otherwise a new entry is created in the map.
     *
     * @param id ID string
     * @param entry arbitrary object
     */
    public static void store(String id, Object entry) {
        storage.put(id, entry);
    }

    /**
     * Checks if there is already an object stored for a certain id.
     *
     * @param id
     * @return
     */
    public static boolean contains(String id) {
        return storage.containsKey(id);
    }

    /**
     * Retrieves an object from the map if there is one with the given ID. If
     * there is no entry for the ID and Exception is thrown. Otherwise a cast to
     * generic type T is performed and the entry returned.
     *
     * @param <T> The type that is returned.
     * @param id ID string
     * @return an entry casted to T
     */
    @SuppressWarnings("unchecked")
    public static <T> T retrieve(String id) {
        Object entry = storage.get(id);
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return (T) entry;
    }

    /**
     * Clears all entries in the storage.
     */
    public static void clear() {
        storage.clear();
    }

    /**
     * Clears a specific entry in the storage.
     *
     * @param id ID String
     * @return True if there has been something deleted.
     */
    public static boolean remove(String id) {
        return storage.remove(id) != null;
    }
}