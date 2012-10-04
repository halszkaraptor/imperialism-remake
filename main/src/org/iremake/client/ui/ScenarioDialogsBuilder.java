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
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import org.iremake.client.utils.Resources;

/**
 *
 * @author Trilarion 2012
 */
public class ScenarioDialogsBuilder {

    private ScenarioDialogsBuilder() {
    }

    /**
     *
     * @param owner
     * @param title
     * @param bounds
     * @return
     */
    public static JDialog makeLoadDialog(JFrame owner, String title, Rectangle bounds) {
        // create general dialog
        JDialog dialog = CommonElementsFactory.makeDialog(owner, title, bounds);

        // placeholder for the real things
        JLabel selectPanel = new JLabel("Select");
        JLabel mapPanel = new JLabel("Map");
        JLabel infoPanel = new JLabel("Info");
        dialog.add(selectPanel);
        dialog.add(mapPanel);
        dialog.add(infoPanel);

        // create menu bar and add to dialog
        JToolBar menuBar = ScenarioDialogsBuilder.loadMenuBar();
        dialog.add(menuBar);

        // define the layout
        Container c = dialog.getContentPane();
        GroupLayout layout = new GroupLayout(c);
        c.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(menuBar)
                .addGroup(layout.createSequentialGroup().addComponent(selectPanel)
                .addComponent(mapPanel)).addComponent(infoPanel));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(menuBar)
                .addGroup(layout.createParallelGroup().addComponent(selectPanel).addComponent(mapPanel))
                .addComponent(infoPanel));

        return dialog;
    }

    private static JToolBar loadMenuBar() {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // load button
        JButton loadButton = CommonElementsFactory.makeButton("");

        // start button
        JButton startButton = CommonElementsFactory.makeButton(Resources.fromScenario("button.start.png"));

        // add buttons to tool bar
        bar.add(loadButton);
        bar.add(startButton);

        return bar;
    }
}
