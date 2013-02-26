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

import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.UIDialog;

/**
 * Dialog for creating a new (and mostly empty) scenario.
 */
public class EditorNewScenarioDialog extends UIDialog {

    /**
     * Setup.
     */
    public EditorNewScenarioDialog() {
        super("New Scenario");

        JPanel content = new JPanel();

        JTextField nameField = new JTextField();
        JTextField nRows = new JTextField();
        JTextField nColumns = new JTextField();
        JButton create = new JButton("Create");

        content.setLayout(new MigLayout("wrap 2", "[sizegroup a][sizegroup b, fill]"));
        content.add(new JLabel("Scenario name"));
        content.add(nameField, "wmin 100");
        content.add(new JLabel("Width (columns)"));
        content.add(nColumns);
        content.add(new JLabel("Height (rows)"));
        content.add(nRows);
        content.add(create, "dock south");

        setMinimumSize(0, 0);
        setContent(content);
    }
    private static final Logger LOG = Logger.getLogger(EditorNewScenarioDialog.class.getName());
}
