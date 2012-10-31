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
package org.iremake.client.ui;

import java.awt.Color;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.resources.TerrainLoader;
import org.iremake.client.ui.map.TerrainTile;
import org.iremake.common.MapPosition;
import org.iremake.common.model.Scenario;
import org.iremake.common.model.ScenarioChangedListener;

/**
 *
 */
public class Model implements ScenarioChangedListener {

    private static final Logger LOG = Logger.getLogger(Model.class.getName());

    private int rows;
    private int columns;
    private TerrainTile[][] map; // first rows, then columns
    private ModelChangedListener listener;

    public Model() {
    }

    public int getNumberRows() {
        return rows;
    }

    public int getNumberColumns() {
        return columns;
    }

    public boolean contains(MapPosition p) {
        return p.row >= 0 && p.row < rows && p.column >= 0 && p.column < columns;
    }

    public Image getTileAt(MapPosition p) {
        if (!contains(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return map[p.row][p.column].getImage();
    }

    public Color getTileColorAt(MapPosition p) {
        if (!contains(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return map[p.row][p.column].getColor();
    }

    @Override
    public void tileChanged(MapPosition p, String id) {
        // TODO check size
        TerrainTile tile = TerrainLoader.getTile(id);
        if (tile == null) {
            LOG.log(Level.SEVERE, "Unknown tile id {0}.", id);
            return;
        }
        map[p.row][p.column] = tile;
        if (listener != null) {
            listener.tileChanged(p);
        }
    }

    @Override
    public void mapChanged(Scenario gmap) {
        rows = gmap.getNumberRows();
        columns = gmap.getNumberColumns();
        map = new TerrainTile[rows][columns];

        String id;
        TerrainTile tile;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                id = gmap.getTerrainAt(new MapPosition(row, column));
                tile = TerrainLoader.getTile(id);
                if (tile == null) {
                    LOG.log(Level.SEVERE, "Unknown tile id {0}.", id);
                }
                map[row][column] = tile;
            }
        }

        if (listener != null) {
            listener.mapChanged();
        }
    }

    public void setScenarioChangedListener(ModelChangedListener l) {
        listener = l;
    }
}
