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

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import org.iremake.client.resources.TerrainLoader;
import org.iremake.common.Settings;

/**
 *
 */
public class EditorSelectionTerrainDialog extends JDialog implements ActionListener {
    
    private EditorManager manager;
    // TODO could be a listener only

    public EditorSelectionTerrainDialog(Dialog owner, EditorManager manager) {
        super(owner, "Terrain Selection", true);
        this.manager = manager;
        initComponents();
    }

    private void initComponents() {
        final int gap = 10;
        final Dimension tileSize = TerrainLoader.getTileSize();
        
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Set<String> IDs = Settings.getTerrainIDs();
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(r, c, gap, gap));        
        panel.setBorder(new EmptyBorder(gap, gap, gap, gap));
        
        for (String id: IDs) {
            JButton button = new JButton();
            button.setIcon(new ImageIcon(TerrainLoader.getTile(id).getImage()));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setName(id);
            button.setFocusable(false);
            button.setToolTipText(Settings.getTerrainType(id));
            button.addActionListener(this);
            panel.add(button);
        }
        add(panel);
        
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = ((JButton) e.getSource()).getName();
        manager.terrainSelected(id);
        dispose();
    }
}
