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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.main.MainScreenBuilder;
import org.iremake.common.resources.Places;

/**
 * Scenario dialogs wiring.
 */
public class ScenarioDialogsBuilder {

    private static final int FIXEDSIZE = 200;

    /**
     * No instantiation.
     */
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
        JDialog dialog = CommonElementsFactory.makeDialog(owner, title, false, bounds);

        // placeholder for the real things
        JTree selectTree = new JTree();
        selectTree.setBorder(new LineBorder(Color.black, 1));
        JPanel mapPanel = makeOverviewMapPanel();
        JPanel infoPanel = ScenarioDialogsBuilder.createInfoPanel();


        // create menu bar and add to dialog
        JToolBar menuBar = ScenarioDialogsBuilder.loadMenuBar(owner);


        // layout - selectTree fixed width, infoPanel fixed height
        Container c = dialog.getContentPane();
        c.setLayout(new MigLayout("wrap 1, fill", "", "[][fill, grow][]"));
        dialog.add(menuBar);
        dialog.add(selectTree, "width 200!, split 2");
        dialog.add(mapPanel, "grow");
        dialog.add(infoPanel, "height 200!, growx");

        return dialog;
    }

    private static JPanel makeOverviewMapPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 1));

        return panel;
    }

    private static JToolBar loadMenuBar(final JFrame owner) {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // load button
        JButton loadButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "scenario.button.load.png");

        // start button
        JButton startButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "scenario.button.start.png");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreenBuilder.build(owner);
            }
        });

        // add buttons to tool bar
        bar.add(loadButton);
        bar.add(startButton);

        return bar;
    }

    private static JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 1));
        return panel;
    }
}
