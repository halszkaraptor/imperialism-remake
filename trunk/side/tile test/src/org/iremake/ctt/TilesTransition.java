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
package org.iremake.ctt;

/**
 * Just copied from main project. (Which we do not want to depend on here.)
 */
public enum TilesTransition {

    East(0), SouthEast(1), SouthWest(2), West(3), NorthWest(4), NorthEast(5);

    int order;

    TilesTransition(int order) {
        this.order = order;
    }

    public int order() {
        return order;
    }
}
