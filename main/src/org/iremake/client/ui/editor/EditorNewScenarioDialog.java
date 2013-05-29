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
import org.iremake.client.ui.CommonElements;
import org.iremake.client.ui.UIDialog;

/**
 * Dialog for creating a new (and mostly empty) scenario. Quite simple for now.
 */
public class EditorNewScenarioDialog extends UIDialog {

    private static final Logger LOG = Logger.getLogger(EditorNewScenarioDialog.class.getName());

    /**
     * Setup.
     */
    public EditorNewScenarioDialog() {
        // call super but change minimum size
        super("New Scenario");
        setMinimumSize(0, 0);
        
        // text fields and buttons
        JTextField nameField = new JTextField("Title");
        JTextField numberRowsField = new JTextField("60");
        JTextField numberColumnsField = new JTextField("100");
        JButton createButton = new JButton("Create");

        JPanel content = new JPanel();        
        content.setLayout(new MigLayout("wrap 2", "[sizegroup a][sizegroup b, fill]"));
        content.add(CommonElements.createLabel("Scenario name"));
        content.add(nameField, "wmin 100");
        content.add(CommonElements.createLabel("Width (number of columns)"));
        content.add(numberColumnsField);
        content.add(CommonElements.createLabel("Height (number of rows)"));
        content.add(numberRowsField);
        content.add(createButton, "span 2, align center");

        setContent(content);
    }
}
