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

import java.awt.Color;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.resources.TerrainLoader;
import org.iremake.common.GeographicalMap;
import org.iremake.common.GeographicalMapChangedListener;

/**
 *
 */
public class MapModel implements GeographicalMapChangedListener {

    private static final Logger LOG = Logger.getLogger(MapModel.class.getName());

    private int rows;
    private int columns;
    private TerrainTile[][] map; // first rows, then columns

    public MapModel() {
    }

    public int getNumberRows() {
        return rows;
    }

    public int getNumberColumns() {
        return columns;
    }

    public Image getTileAt(int row, int column) {
        // TODO in range
        return map[row][column].getImage();
    }

    public Color getTileColorAt(int row, int column) {
        // TODO in range
        return map[row][column].getColor();
    }

    @Override
    public void tileChanged(int row, int column, String id) {
        // TODO check size
        TerrainTile tile = TerrainLoader.getTile(id);
        if (tile == null) {
            LOG.log(Level.SEVERE, "Unknown tile id {0}.", id);
            return;
        }
        map[row][column] = tile;
        // fire event
    }

    @Override
    public void mapChanged(GeographicalMap gmap) {
        rows = gmap.getNumberRows();
        columns = gmap.getNumberColumns();
        map = new TerrainTile[rows][columns];

        String id;
        TerrainTile tile;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                id = gmap.getTerrainAt(row, column);
                tile = TerrainLoader.getTile(id);
                if (tile == null) {
                    LOG.log(Level.SEVERE, "Unknown tile id {0}.", id);
                }
                map[row][column] = tile;
            }
        }

        // fire event
    }
}
