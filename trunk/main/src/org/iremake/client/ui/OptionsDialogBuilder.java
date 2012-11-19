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

import java.awt.Container;
import java.awt.Rectangle;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * Options dialog wiring.
 */
public class OptionsDialogBuilder {

    /**
     * No instantiation.
     */
    private OptionsDialogBuilder() {
    }

    /**
     *
     * @param owner
     * @param title
     * @param bounds
     * @return
     */
    public static JDialog makeDialog(JFrame owner, String title, Rectangle bounds) {
        // create general dialog
        JDialog dialog = CommonElementsFactory.makeDialog(owner, title, false, bounds);

        // create JTabbedpane
        JTabbedPane pane = new JTabbedPane();
        pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // add panels for each tab
        pane.add(OptionsDialogBuilder.generalOptionsPanel(), "General");
        pane.add(OptionsDialogBuilder.serverOptionsPanel(), "Server");

        // add pane to dialog
        dialog.add(pane);

        // layout dialog (with GroupLayout so borders are respected)
        Container c = dialog.getContentPane();
        GroupLayout layout = new GroupLayout(c);
        c.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(pane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pane));

        return dialog;
    }

    /**
     *
     * @return
     */
    private static JPanel generalOptionsPanel() {
        JPanel panel = new JPanel();
        return panel;
    }
    
    private static JPanel serverOptionsPanel() {
        JPanel panel = new JPanel();
        JToggleButton serverStart = new JToggleButton();
        JToolBar menuBar = new JToolBar();
        menuBar.add(serverStart);
        panel.add(menuBar);
        
        return panel;
        
    }
}
