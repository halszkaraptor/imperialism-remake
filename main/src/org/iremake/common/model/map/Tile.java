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

import org.iremake.common.Settings;
import org.iremake.common.model.Province;

/**
 * A tile holds the map together. A map is an array of tiles.
 *
 * Useful default values are set.
 */
public class Tile {

    public int terrainID = Settings.getDefaultTerrainID();
    public int terrainSubID = 0; // TODO not used right now

    public int resourceID = Settings.RESOURCE_NONE;
    public boolean resourceVisible = false;

    public int provinceID = Province.NONE;

    public int railroadConfig = 0;
}
