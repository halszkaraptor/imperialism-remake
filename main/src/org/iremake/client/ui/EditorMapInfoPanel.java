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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.iremake.client.ui.maps.MainMapTileFocusChangedListener;

/**
 *
 */
public class EditorMapInfoPanel extends JPanel implements MainMapTileFocusChangedListener {

    private static final long serialVersionUID = 1L;
    private JLabel tile;

    public EditorMapInfoPanel() {
        initComponents();
    }

    private void initComponents() {

        setBorder(new LineBorder(Color.black, 1));

        tile = new JLabel();
        add(tile);
    }

    @Override
    public void newTileFocus(int row, int column) {
        tile.setText("Tile: " + Integer.toString(row) + ", " + Integer.toString(column));
    }
}
