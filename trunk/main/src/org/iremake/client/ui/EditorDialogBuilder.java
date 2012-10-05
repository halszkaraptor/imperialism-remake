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

import java.awt.Color;
import java.awt.Container;
import java.awt.Rectangle;
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
        JPanel mapPanel = EditorDialogBuilder.createMapPanel();
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

        // exit button
        JButton saveButton = CommonElementsFactory.makeButton(Resources.fromUI("scenario.button.save.png"));

        // add buttons to toolbar
        bar.add(loadButton);
        bar.add(saveButton);

        return bar;
    }

    private static JPanel createMapPanel() {
        JPanel panel = new JPanel();

        JPanel miniMapPanel = MapPanelBuilder.makeMiniMapPanel();
        // TODO so that not need to be resized

        JToolBar menuBar = EditorDialogBuilder.makeMapMenuBar();

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.red);

        JPanel editableMapPanel = new JPanel();
        editableMapPanel.setBackground(Color.blue);

        // add all
        panel.add(miniMapPanel);
        panel.add(menuBar);
        panel.add(infoPanel);
        panel.add(editableMapPanel);

        // layout
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup()
                .addComponent(miniMapPanel, 200, 200, 200).addComponent(menuBar, 200, 200, 200).addComponent(infoPanel, 200, 200, 200))
                .addComponent(editableMapPanel));
        layout.setVerticalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup()
                .addComponent(miniMapPanel, 200, 200, 200).addComponent(menuBar, 100, 100, 100).addComponent(infoPanel))
                .addComponent(editableMapPanel));

        return panel;
    }

    private static JToolBar makeMapMenuBar() {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // terrain button
        JButton terrainButton = CommonElementsFactory.makeButton(Resources.fromUI("editor.button.terrain.png"));

        // nation button
        JButton nationButton = CommonElementsFactory.makeButton(Resources.fromUI("editor.button.nation.png"));

        // province button
        JButton provinceButton = CommonElementsFactory.makeButton(Resources.fromUI("editor.button.province.png"));

        // add buttons to toolbar
        bar.add(terrainButton);
        bar.add(nationButton);
        bar.add(provinceButton);

        return bar;
    }
}
