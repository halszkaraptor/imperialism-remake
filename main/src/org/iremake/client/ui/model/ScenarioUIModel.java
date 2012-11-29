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
package org.iremake.client.ui.model;

import java.awt.Color;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.client.resources.TerrainLoader;
import org.iremake.client.ui.model.TerrainTile;
import org.iremake.common.model.MapPosition;
import org.iremake.common.model.Scenario;
import org.iremake.common.model.ScenarioChangedListener;

/**
 * The model for a Scenario from a User Interface perspective. Add all user
 * interface specific stuff, mostly graphics.
 *
 */
// TODO fine-adjust playing together with Scenario
public class ScenarioUIModel implements ScenarioChangedListener {

    private static final Logger LOG = Logger.getLogger(ScenarioUIModel.class.getName());
    private int rows;
    private int columns;
    private TerrainTile[][] map; // first rows, then columns
    private ScenarioUIModelChangedListener listener;

    /**
     *
     */
    public ScenarioUIModel() {
    }

    /**
     * Width of game map.
     *
     * @return
     */
    public int getNumberRows() {
        return rows;
    }

    /**
     *
     * @return
     */
    public int getNumberColumns() {
        return columns;
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean contains(MapPosition p) {
        return p.row >= 0 && p.row < rows && p.column >= 0 && p.column < columns;
    }

    /**
     *
     * @param p
     * @return
     */
    public Image getTileAt(MapPosition p) {
        if (!contains(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return map[p.row][p.column].getImage();
    }

    /**
     *
     * @param p
     * @return
     */
    public Color getTileColorAt(MapPosition p) {
        if (!contains(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return map[p.row][p.column].getColor();
    }

    /**
     *
     * @param p
     * @param id
     */
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

    /**
     *
     * @param gmap
     */
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

    /**
     *
     * @param l
     */
    public void setScenarioChangedListener(ScenarioUIModelChangedListener l) {
        listener = l;
    }
}
