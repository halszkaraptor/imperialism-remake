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
package org.tools.common;

import java.util.Comparator;

/**
 * This class can be used if a number of objects must be sorted according to a
 * clear name which is provided additionally.
 *
 * Potential applications include sorting of language related classes by their
 * real name.
 *
 * @param <T> The type of the objects that are named.
 * @author Trilarion 2011
 */
public class Named<T> {

    private String name;
    private T object;
    /**
     * The comparator redirects to the compareTo method of the string
     * components.
     *
     * We don't rely on special properties of T here since we only use the name
     * for comparison.
     */
    public static final Comparator<Named<?>> comparator = new Comparator<Named<?>>() {
        @Override
        public int compare(Named<?> o1, Named<?> o2) {
            String s1 = o1.getName();
            String s2 = o2.getName();
            if (s1 == null) {
                // one name is null, cannot be equal
                return -1;
            }
            return s1.compareTo(s2);
        }
    };

    /**
     * Sets name and object.
     *
     * @param name The name.
     * @param object The object.
     */
    public Named(String name, T object) {
        this.name = name;
        this.object = object;
    }

    /**
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The object.
     */
    public T getObject() {
        return object;
    }

    @Override
    public String toString() {
        return getName();
    }
}
