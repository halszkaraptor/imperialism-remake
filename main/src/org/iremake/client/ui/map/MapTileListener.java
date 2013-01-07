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
package org.iremake.client.ui.map;

import org.iremake.common.model.map.MapPosition;

/**
 * Changes of the main map, mainly due to mouse actions (moves or clicks).
 */
public interface MapTileListener {

    /**
     * The mouse hoovers now above a different tile.
     *
     * @param p position of the tile
     */
    public void focusChanged(MapPosition p);

    /**
     * The mouse clicked on a specific tile.
     *
     * @param p position of the tile
     */
    public void tileClicked(MapPosition p);
}
