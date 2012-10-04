/*
 * Copyright (C) 2012 Trilarion 2012
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import org.iremake.client.utils.Resources;

/**
 *
 * @author Trilarion 2012
 */
public class EditorDialogBuilder {

    private EditorDialogBuilder() {
    }

    public static JDialog makeDialog(JFrame owner, String title, Rectangle bounds) {
        JDialog dialog = CommonElementsFactory.makeDialog(owner, title, bounds);

        // create menu bar and add to frame
        JToolBar menuBar = EditorDialogBuilder.menuBar();
        dialog.add(menuBar);

        // tabbed pane
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // placeholder for the real things
        JPanel mapPanel = new JPanel();
        JPanel nationPanel = new JPanel();

        // add panels to tabbed pane
        tabPane.addTab("Map", mapPanel);
        tabPane.addTab("Nation", nationPanel);

        // add to frame
        dialog.add(tabPane);

        // layout (vertically first menubar, then tabbed pane)
        Container c = dialog.getContentPane();
        GroupLayout layout = new GroupLayout(c);
        c.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(menuBar).addComponent(tabPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(menuBar).addComponent(tabPane));

        return dialog;
    }

    private static JToolBar menuBar() {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // exit button
        JButton loadButton = CommonElementsFactory.makeButton(Resources.fromUI("scenario.button.load.png"));

        // add buttons to toolbar
        bar.add(loadButton);

        return bar;
    }
}
