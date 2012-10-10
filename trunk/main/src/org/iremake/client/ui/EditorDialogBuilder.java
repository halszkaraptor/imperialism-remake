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
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import org.iremake.client.utils.Resources;

/**
 *
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
        miniMapPanel.setBorder(new LineBorder(Color.black, 1));
        // TODO so that not need to be resized

        JToolBar menuBar = EditorDialogBuilder.makeMapMenuBar();

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new LineBorder(Color.black, 1));

        JPanel editableMapPanel = new JPanel() {
            private static final long serialVersionUID = 1L;
            private static final int GAP = 10;

            Image image = Resources.getAsImage("/data/game/artwork/graphics/terrain/terrain.plains.png");

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                // staggered drawing
                for (int col = 0; col < 100; col++) {
                    for (int row = 0; row < 60; row++) {
                        int x = GAP + col * 80 + ((row % 2 == 1) ? 40 : 0);
                        int y = GAP + row * 80;
                        g2d.drawImage(image, x, y, null);
                    }
                }
            }
        };
        editableMapPanel.setBackground(Color.white);
        editableMapPanel.setBorder(new LineBorder(Color.black, 1));

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
