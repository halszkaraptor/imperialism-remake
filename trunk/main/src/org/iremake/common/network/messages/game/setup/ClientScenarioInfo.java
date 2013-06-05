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
package org.iremake.common.network.messages.game.setup;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import org.iremake.common.model.Nation;
import org.iremake.common.model.ServerScenario;
import org.iremake.common.model.map.MapPosition;
import org.tools.xml.XList;

/**
 *
 */
public class ClientScenarioInfo {

    private int rows;
    private int columns;
    private int[][] politicalMap;
    private int[] colors;
    private String[] names;

    public ClientScenarioInfo(ServerScenario scenario) {
        // get map dimension and create political map
        rows = scenario.getNumberRows();
        columns = scenario.getNumberColumns();
        politicalMap = new int[rows][columns];

        // get nations, number of nations and create names array
        XList<Nation> nations = scenario.getNations();
        int numNations = nations.getSize();
        names = new String[numNations];
        colors = new int[numNations];

        // traverse nations by index and store names as well as index for each nation
        Map<Nation, Integer> nationIDs = new HashMap<>(numNations);
        for (int i = 0; i < numNations; i++) {
            Nation nation = nations.getElementAt(i);
            nationIDs.put(nation, i);
            names[i] = nation.getProperty(Nation.KEY_NAME);
            colors[i] = nation.getColor().getRGB();
        }

        // traverse map and store index for each nation or Max value if ther eis no nation (sea)
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Nation nation = scenario.getNationAt(new MapPosition(row, column));
                if (nation != null) {
                    // put the nations ID
                    politicalMap[row][column] = nationIDs.get(nation);
                } else {
                    // it's sea
                    politicalMap[row][column] = Integer.MAX_VALUE;
                }
            }
        }
    }

    public int getNumberColumns() {
        return columns;
    }

    public int getNumberRows() {
        return rows;
    }

    public int getColor(int row, int column) {
        int id = politicalMap[row][column];
        if (id == Integer.MAX_VALUE ) {
            return Color.WHITE.getRGB();
        }
        return colors[id];
    }

    public String getNationAt(Point2D.Float point) {
        int column = (int)(point.x * columns);
        int row = (int)(point.y * rows);
        Integer nation = politicalMap[row][column];
        if (nation == Integer.MAX_VALUE) {
            return null;
        }
        return names[nation];
    }
}
