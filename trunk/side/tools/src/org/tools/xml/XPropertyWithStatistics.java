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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Added ability to keep counting statistics.
 */
public class XPropertyWithStatistics extends XProperty {

    private Map<String, Integer> stats = new HashMap<>(0);

    /**
     *
     * @param capacity
     */
    public XPropertyWithStatistics(int capacity) {
        super(capacity);
    }

    /**
     *
     * @param content
     */
    public XPropertyWithStatistics(Map<String, String> content) {
        super(content);
    }

    @Override
    public String get(String key) {
        // statistics
        Integer count = stats.get(key);
        if (count == null) {
            stats.put(key, 1);
        } else {
            stats.put(key, count + 1);
        }

        return super.get(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public Integer getCount(String key) {
        return stats.containsKey(key) ? stats.get(key) : 0;
    }
    private static final Logger LOG = Logger.getLogger(XPropertyWithStatistics.class.getName());

}
