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
package org.iremake.client.ui.main;

import org.iremake.client.ui.main.MainScreenManager;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.iremake.client.ui.CommonElementsFactory;
import org.iremake.client.ui.Model;
import org.tools.ui.layout.RelativeLayout;
import org.tools.ui.layout.RelativeLayoutConstraint;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.common.model.Scenario;
import org.iremake.common.resources.Places;
import org.tools.ui.common.ClockLabel;
import org.tools.ui.helper.GraphicsUtils;
import org.tools.ui.helper.WindowCorner;

/**
 *
 */
// TODO unmake static
public class MainScreenBuilder {

    private MainScreenBuilder() {
    }

    public static void build(JFrame owner) {
        final MainScreenManager manager = new MainScreenManager();

        Dimension s = GraphicsUtils.getScreenSize();
        Rectangle bounds = new Rectangle(0, 0, s.width, s.height);
        JDialog dialog = CommonElementsFactory.makeDialog(owner, null, false, bounds);
        dialog.setSize(s);
        dialog.setUndecorated(true);

        manager.setFrame(dialog);

        Model model = new Model();
        Scenario map = new Scenario();
        manager.setScenarioContent(map, model);

        // get layered pane
        JLayeredPane layers = dialog.getLayeredPane();

        // TODO change to RelativeLayout

        // Add MapPanel
        MainMapPanel mainMapPanel = new MainMapPanel(model);
        layers.add(mainMapPanel, Integer.valueOf(1));
        mainMapPanel.setPreferredSize(s);
        manager.setPanels(mainMapPanel);

        RelativeLayout layout = new RelativeLayout();
        layers.setLayout(layout);

        // add clock label to layered pane
        JLabel clockLabel = new ClockLabel();
        layers.add(clockLabel, Integer.valueOf(2));
        layout.addConstraint(clockLabel, RelativeLayoutConstraint.corner(WindowCorner.SouthWest, 10, 10));

        // add main panel to layout
        layout.addConstraint(mainMapPanel, RelativeLayoutConstraint.centered());

        // add control panel to layered pane and position
        JPanel controlPanel = MainScreenBuilder.createControlPanel(manager);
        layers.add(controlPanel, Integer.valueOf(3));
        layout.addConstraint(controlPanel, new RelativeLayoutConstraint(1.0f, -1.0f, -20, 0.5f, -0.5f, 0));

        // we make it visible immediately
        dialog.setVisible(true);

        // and load a basic scenario
        manager.loadInitialScenario();
    }

    private static JPanel createControlPanel(final MainScreenManager manager) {
        JPanel panel = new JPanel();

        panel.setPreferredSize(new Dimension(300, 800));

        // create menu bar and add to frame
        JToolBar menuBar = CommonElementsFactory.makeToolBar();

        // exit button
        JButton exitButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "main.button.exit.png");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.exit();
            }
        });

        // save button
        JButton saveButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "main.button.save.png");

        // add buttons to toolbar
        menuBar.add(exitButton);
        menuBar.add(saveButton);

        panel.add(menuBar);

        return panel;
    }
}
