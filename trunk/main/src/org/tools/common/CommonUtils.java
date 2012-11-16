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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class holds some utility methods that have no specific purpose. At this
 * time it's mostly string operations.
 */
public class CommonUtils {

    private static final Logger LOG = Logger.getLogger(CommonUtils.class.getName());

    /**
     * Private constructor to avoid instantiation.
     */
    private CommonUtils() {
    }

    /**
     * Returns a string, where the first letter is converted to upper case and
     * all other letters to lower case.
     *
     * @param s The input string.
     * @return The converted string.
     */
    public static String capitalize(String s) {
        if (s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    /**
     * Tests if a string consists only of upper case letters.
     *
     * @param s The input string.
     * @return True if consisting only of upper case letters or null.
     */
    public static boolean isUpper(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 'A' || c > 'Z') {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests if a string consists only of lower case letters.
     *
     * @param s The input string.
     * @return True if consisting only of lower case letters or null.
     */
    public static boolean isLower(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 'a' || c > 'z') {
                return false;
            }
        }
        return true;
    }

    /**
     * Takes any collection, e.g. a Set, and converts it to a sorted List.
     *
     * @param <T> Anything that's comparable.
     * @param collection The initial collection.
     * @return The sorted List.
     */
    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        Collections.sort(list);
        return list;
    }

    /**
     * Checks if an [][] array has equal lengths in the second dimension and
     * returns this value which represents the numbers of columns of a 2D
     * rectangular array.
     *
     * If array is null, -1 is returned. If the array has no rows, -2 is
     * returned. If the rows do not have equal length, -3 is returned.
     *
     * @param array The input array.
     * @return Length in second dimension.
     */
    public static int getArrayColNumber(Object[][] array) {
        if (array == null) {
            // array is null, return -1
            return -1;
        }

        if (array.length == 0) {
            // no rows, return -2
            return -2;
        }

        for (int i = 1; i < array.length; i++) {
            if (array[i].length != array[0].length) {
                // row i has more or less elements than row zero, return -3
                return -3;
            }
        }

        // all rows have equal number of elements, return any of them
        return array[0].length;
    }
}
