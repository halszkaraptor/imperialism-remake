/*
 * Copyright (C) 2000 ymnk, JCraft,Inc.
 *               2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
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
package org.util;

public class Utils {

    /**
     * No instantiation.
     */
    private Utils() {
    }

    public static int ilog(int v) {
        int ret = 0;
        while (v != 0) {
            ret++;
            v >>>= 1;
        }
        return (ret);
    }

    public static int ilog2(int v) {
        int ret = 0;
        while (v > 1) {
            ret++;
            v >>>= 1;
        }
        return (ret);
    }

    public static int icount(int v) {
        int ret = 0;
        while (v != 0) {
            ret += (v & 1);
            v >>>= 1;
        }
        return (ret);
    }
}
