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
import org.iremake.common.model.MapPosition;
import org.iremake.common.model.Scenario;
import org.iremake.common.model.ScenarioChangedListener;

/**
 *
 */
public class UIScenario extends Scenario implements ScenarioChangedListener {

    private static final Logger LOG = Logger.getLogger(UIScenario.class.getName());

    private TerrainTile[][] map; // first rows, then columns

    public UIScenario() {
        addScenarioChangedListener(this);
    }

    /**
     *
     * @param p
     * @return
     */
    public Image getTerrainTileAt(MapPosition p) {
        if (!containsPosition(p)) {
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
    public Color getTerrainTileColorAt(MapPosition p) {
        if (!containsPosition(p)) {
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
    }

    @Override
    public void scenarioChanged(Scenario scenario) {
        int rows = scenario.getNumberRows();
        int columns = scenario.getNumberColumns();
        map = new TerrainTile[rows][columns];

        String id;
        TerrainTile tile;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                id = scenario.getTerrainAt(new MapPosition(row, column));
                tile = TerrainLoader.getTile(id);
                if (tile == null) {
                    LOG.log(Level.SEVERE, "Unknown tile id {0}.", id);
                }
                map[row][column] = tile;
            }
        }
    }
}
