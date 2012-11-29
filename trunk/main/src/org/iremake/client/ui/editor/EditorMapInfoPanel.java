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
package org.iremake.client.ui.editor;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.iremake.common.model.MapPosition;
import org.iremake.common.Settings;
import org.iremake.common.model.Scenario;

/**
 * The map info panel, located at the left lower part of the map tab.
 *
 */
public class EditorMapInfoPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel tile;
    private Scenario map;

    /**
     *
     * @param map
     */
    public EditorMapInfoPanel(Scenario map) {
        setBorder(new LineBorder(Color.black, 1));

        tile = new JLabel();
        add(tile);

        this.map = map;
    }

    /**
     * Focus changed of main map.
     *
     * @param p
     */
    public void mainMapTileChanged(MapPosition p) {
        if (p.isOff()) {
            tile.setText("");
        } else {
            tile.setText("Tile: " + Integer.toString(p.row) + ", " + Integer.toString(p.column) + "  " + Settings.getTerrainType(map.getTerrainAt(p)));
        }
    }
}
