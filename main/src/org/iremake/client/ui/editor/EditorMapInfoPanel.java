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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.iremake.common.Settings;
import org.iremake.common.model.MapPosition;
import org.iremake.common.model.Scenario;

/**
 * The map info panel, located at the left lower part of the map tab.
 *
 */
public class EditorMapInfoPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel tile = new JLabel();
    private JLabel terrain = new JLabel();
    private JLabel resource = new JLabel();
    private JLabel nation = new JLabel();
    private JLabel province = new JLabel();

    /**
     *
     * @param map
     */
    public EditorMapInfoPanel() {
        setBorder(BorderFactory.createTitledBorder("Info"));

        setLayout(new MigLayout("wrap 1"));
        add(tile);
        add(terrain);
        add(resource);
        add(nation);
        add(province);

        init();
    }

    private void init() {
        tile.setText("Tile: None");
        terrain.setText("Terrain: None");
        resource.setText("Resource: None");
        nation.setText("Nation: None");
        province.setText("Province: None");
    }

    /**
     * Focus changed of main map.
     *
     * @param p
     */
    public void mainMapTileChanged(MapPosition p, Scenario scenario) {
        if (p.isOff()) {
            init();
        } else {
            tile.setText("Tile: " + Integer.toString(p.row) + ", " + Integer.toString(p.column));
            terrain.setText("Terrain: " + Settings.getTerrainName(scenario.getTerrainAt(p)));
        }
    }
}
