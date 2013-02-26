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
package org.iremake.common.model.map;

import java.util.logging.Logger;

/**
 * A general position. Used in our context for addressing the position on the
 * map with (-1,-1) being the generic 'off' value.
 */
public class MapPosition {

    /**
     * Two coordinates (row, column).
     */
    public int row, column;

    /**
     * Initializes to (0, 0) by default.
     */
    public MapPosition() {
        this(0, 0);
    }

    /**
     * Initializes with given row and column.
     *
     * @param row
     * @param column
     */
    public MapPosition(int row, int column) {
        // TODO check non-negative or 'off'
        this.row = row;
        this.column = column;
    }

    /**
     * Set to 'off' state.
     */
    public void setOff() {
        row = -1;
        column = -1;
    }

    /**
     * Test if 'off' state
     *
     * @return
     */
    public boolean isOff() {
        return row == -1 && column == -1;
    }

    /**
     * Set from another position, copy the position.
     *
     * @param p
     */
    public void setFrom(MapPosition p) {
        row = p.row;
        column = p.column;
    }

    /**
     * Is equal if row and column are equal.
     *
     * @param p
     * @return
     */
    public boolean equals(MapPosition p) {
        return row == p.row && column == p.column;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return equals((MapPosition) obj);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return row + 13 * column;
    }
    private static final Logger LOG = Logger.getLogger(MapPosition.class.getName());
}
