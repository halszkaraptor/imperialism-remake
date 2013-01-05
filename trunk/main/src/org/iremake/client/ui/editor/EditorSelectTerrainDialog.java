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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.UIDialog;
import org.iremake.client.ui.model.TerrainTiles;
import org.iremake.common.Settings;

/**
 * Selection of a new terrain.
 */
public class EditorSelectTerrainDialog extends UIDialog {

    private int selected;

    /**
     *
     * @param tiles
     */
    public EditorSelectTerrainDialog(TerrainTiles tiles) {
        super("Select Terrain");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ((JButton) e.getSource()).getName();
                selected = Integer.parseInt(name);
                close();
            }
        };

        JPanel panel = new JPanel();

        Set<Integer> IDs = tiles.getIDs();
        int n = IDs.size();
        int l = (int) Math.sqrt(n);
        int r, c;
        if (l * l >= n) {
            r = l;
            c = l;
        } else if ((l + 1) * l >= n) {
            r = l;
            c = l + 1;
        } else {
            r = l + 1;
            c = l + 1;
        }
        // here actually GridLayout does exactly the job and we do not need MigLayout
        panel.setLayout(new MigLayout("wrap " + c));

        for (Integer id : IDs) {
            JButton button = new JButton();
            button.setIcon(new ImageIcon(tiles.getImage(id)));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setName(id.toString());
            button.setFocusable(false);
            button.setToolTipText(Settings.getTerrainName(id));
            button.addActionListener(listener);
            panel.add(button);
        }

        setContent(panel);
        setMinimumSize(0, 0);
    }

    /**
     *
     * @return
     */
    public int getSelection() {
        return selected;
    }
}
