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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.Button;
import org.iremake.client.ui.FrameManager;
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
    public static JComponent build() {

        JPanel panel = new JPanel();

        ScenarioUIModel model = new ScenarioUIModel();
        Scenario map = new Scenario();
        MainScreenManager.getInstance().setScenarioContent(map, model);

        // Add MapPanel
        MainMapPanel mainMapPanel = new MainMapPanel(model);

        MainScreenManager.getInstance().setPanels(mainMapPanel);

        // add control panel to layered pane and position
        JPanel controlPanel = MainScreenBuilder.createControlPanel(model);

        panel.setLayout(new MigLayout("wrap 2, fill", "[grow][]"));
        panel.add(mainMapPanel, "grow");
        panel.add(controlPanel, "growy, wmin 300");

        return panel;
    }

    /**
     *
     * @param manager
     * @return
     */
    private static JPanel createControlPanel(ScenarioUIModel model) {
        JPanel panel = new JPanel();

        JPanel infPanel = new JPanel();
        infPanel.setBorder(new LineBorder(Color.black, 1));

        // create menu bar and add to frame
        JToolBar menuBar = GraphicsUtils.makeToolBar(false, false);

        // exit button
        JButton exitButton = Button.NormalExit.create();
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameManager.getInstance().switchToStartScreen();
            }
        });

        // save button
        JButton saveButton = Button.ScenarioSave.create();

        // add buttons to toolbar
        menuBar.add(saveButton);
        menuBar.add(exitButton);

        // create mini map and add to panel
        MiniMapPanel miniMapPanel = new MiniMapPanel(model);

        MainScreenManager.getInstance().setMiniMap(miniMapPanel);

        // create other buttons
        JToolBar toolBar = GraphicsUtils.makeToolBar(false, false);
        JButton industryDialog = new JButton("Industry");
        // TODO build industy button also non-focusable (commonelementsfactory)
        industryDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = FrameManager.getInstance().makeDialog("Industry", new Dimension(800, 700));
                dialog.setVisible(true);
            }
        });
        toolBar.add(industryDialog);

        // create info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new LineBorder(Color.black, 1));

        // layout
        panel.setLayout(new MigLayout("wrap 1, fill", "", "[][][][][grow][]"));
        panel.add(infPanel, "growx");
        panel.add(menuBar);
        panel.add(miniMapPanel, "growx"); // because the extra space goes here
        panel.add(toolBar);
        panel.add(infoPanel, "grow");
        panel.add(new ClockLabel(), "alignx center");

        return panel;
    }
}
