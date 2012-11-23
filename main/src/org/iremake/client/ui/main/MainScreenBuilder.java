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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.resources.Places;
import org.iremake.client.ui.CommonElementsFactory;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.model.ScenarioUIModel;
import org.iremake.common.model.Scenario;
import org.iremake.common.ui.ClockLabel;
import org.iremake.common.ui.utils.GraphicsUtils;

/**
 * Builds the main screen.
 */
// TODO unmake static
public class MainScreenBuilder {

    /**
     * No instantiation.
     */
    private MainScreenBuilder() {
    }

    /**
     *
     * @param owner
     */
    public static void build(JFrame owner) {
        final MainScreenManager manager = new MainScreenManager();

        Dimension s = GraphicsUtils.getScreenSize();
        Rectangle bounds = new Rectangle(0, 0, s.width, s.height);
        JDialog dialog = CommonElementsFactory.makeDialog(owner, null, false, bounds);
        dialog.setSize(s);
        dialog.setUndecorated(true);

        manager.setFrame(dialog);

        ScenarioUIModel model = new ScenarioUIModel();
        Scenario map = new Scenario();
        manager.setScenarioContent(map, model);

        // Add MapPanel
        MainMapPanel mainMapPanel = new MainMapPanel(model);

        mainMapPanel.setPreferredSize(s);
        manager.setPanels(mainMapPanel);

        // add control panel to layered pane and position
        JPanel controlPanel = MainScreenBuilder.createControlPanel(manager, model);
        
        dialog.setLayout(new MigLayout("wrap 2, fill", "[grow][]"));
        dialog.add(mainMapPanel, "grow");
        dialog.add(controlPanel, "growy, wmin 300");

        // we make it visible immediately
        dialog.setVisible(true);

        // and load a basic scenario
        manager.loadInitialScenario();
    }

    /**
     *
     * @param manager
     * @return
     */
    private static JPanel createControlPanel(final MainScreenManager manager, ScenarioUIModel model) {
        JPanel panel = new JPanel();

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
        
        // create mini map and add to panel
        MiniMapPanel miniMapPanel = new MiniMapPanel(model);        
        
        manager.setMiniMap(miniMapPanel);
        
        // create other buttons
        JToolBar toolBar = CommonElementsFactory.makeToolBar();
        JButton industryDialog = new JButton("Industry");
        industryDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        toolBar.add(industryDialog);
        
        // create info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new LineBorder(Color.black, 1));

        // layout
        panel.setLayout(new MigLayout("wrap 1, fill", "", "[][][][grow][]"));
        panel.add(menuBar);
        panel.add(miniMapPanel, "growx"); // because the extra space goes here
        panel.add(toolBar);
        panel.add(infoPanel, "grow");
        panel.add(new ClockLabel(), "alignx center");
        
        return panel;
    }
}
