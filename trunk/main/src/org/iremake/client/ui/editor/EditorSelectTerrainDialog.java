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
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.UIDialog;
import org.iremake.client.ui.model.TileGraphicsRepository;
import org.iremake.common.Settings;

/**
 * Selection of a new terrain.
 */
public class EditorSelectTerrainDialog extends UIDialog {

    private static final Logger LOG = Logger.getLogger(EditorSelectTerrainDialog.class.getName());    
    /* Keeps track of the selected terrain */
    private int selected = -1;

    /**
     *
     * @param tiles
     */
    public EditorSelectTerrainDialog(TileGraphicsRepository repository) {
        super("Select Terrain");

        // this listener is added to every button, the button is coded in the name
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = ((JButton) e.getSource()).getName();
                selected = Integer.parseInt(name);
                // we close immediately after a selection
                close();
            }
        };

        // the new panel
        JPanel panel = new JPanel();

        // get all available tiles and find out which rectangular form is closest to a square
        Set<Integer> IDs = Settings.getTerrainIDs();
        int n = IDs.size();
        int c = (int) Math.sqrt(n);
        if (c * c >= n) {
            c++;
        }

        // here GridLayout would also exactly do the job but we use Miglayout
        // for consistent borders everywhere in the application
        panel.setLayout(new MigLayout("wrap " + c));

        // add a button for each terrain tile and set tooltip text and name accordingly
        for (Integer id : IDs) {
            JButton button = new JButton();
            button.setIcon(new ImageIcon(repository.getTerrainTile(id)));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setName(id.toString());
            button.setFocusable(false);
            button.setToolTipText(Settings.getTerrainName(id));
            button.addActionListener(listener);
            panel.add(button);
        }

        setContent(panel);

        // this means the true size is just enough to hold all the content
        setMinimumSize(0, 0);
    }

    /**
     * Return the selected element.
     *
     * @return id of the element or -1 if not yet selected
     */
    public int getSelection() {
        return selected;
    }
}
