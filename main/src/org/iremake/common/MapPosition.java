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
package org.iremake.common;

/**
 *
 */
public class MapPosition {

    public final static MapPosition Off = new MapPosition(-1, -1);

    public int row, column;

    public MapPosition() {
        this(0, 0);
    }

    public MapPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public boolean isOff() {
        return equals(Off);
    }

    public void setFrom(MapPosition p) {
        row = p.row;
        column = p.column;
    }

    public boolean equals(MapPosition p) {
        return row == p.row && column == p.column;
    }

}
