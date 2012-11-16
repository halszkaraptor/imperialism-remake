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
package org.tools.common;

/**
 * For simple profiling - a chronometer. In the singleton pattern.
 */
public class Chrono {

    private static Chrono instance = null;
    private long start = 0;

    /**
     * No instantiation.
     */
    private Chrono() {
    }

    /**
     * Get a new instance.
     * @return 
     */
    public static Chrono getInstance() {
        if (instance == null) {
            instance = new Chrono();
        }
        return instance;
    }

    /**
     * Starts the chronometer.
     */
    public void reset() {
        start = System.nanoTime();
    }

    /**
     * Computes the time difference between now and last call to reset.
     *
     * @return Time difference in nanoseconds.
     */
    public long time() {
        return System.nanoTime() - start;
    }
}
