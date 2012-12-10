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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.Button;
import org.iremake.client.ui.StartScreen;
import org.iremake.client.ui.UIFrame;
import org.iremake.client.ui.game.GameDialogBuilder;
import org.iremake.client.ui.game.GamePanel;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.client.ui.map.MiniMapPanel;
import org.iremake.client.ui.model.ScenarioUIModel;
import org.iremake.common.model.Scenario;
import org.tools.ui.ButtonBar;
import org.tools.ui.ClockLabel;

/**
 *
 */
public class MainScreen extends UIFrame {

    public MainScreen() {
        JPanel panel = new JPanel();

        ScenarioUIModel model = new ScenarioUIModel();
        Scenario map = new Scenario();
        MainScreenManager.getInstance().setScenarioContent(map, model);

        // Add MapPanel
        MainMapPanel mainMapPanel = new MainMapPanel(model);

        MainScreenManager.getInstance().setPanels(mainMapPanel);

        // add control panel to layered pane and position
        JPanel controlPanel = createControlPanel(model);

        panel.setLayout(new MigLayout("fill", "[grow][]"));
        panel.add(mainMapPanel, "grow");
        panel.add(controlPanel, "growy, wmin 300");

        setContent(panel);
    }

    private JPanel createControlPanel(ScenarioUIModel model) {
        JPanel panel = new JPanel();

        JPanel infPanel = new JPanel();
        infPanel.setBorder(new LineBorder(Color.black, 1));

        // exit button
        JButton exitButton = Button.NormalExit.create();
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIFrame frame = new StartScreen();
                frame.switchTo();
            }
        });

        // save button
        JButton saveButton = Button.ScenarioSave.create();

        // add buttons to toolbar
        ButtonBar ubar = new ButtonBar();
        ubar.add(saveButton, exitButton);

        // create mini map and add to panel
        MiniMapPanel miniMapPanel = new MiniMapPanel(model);

        MainScreenManager.getInstance().setMiniMap(miniMapPanel);

        ButtonBar lbar = new ButtonBar();
        for (final GamePanel p : GamePanel.values()) {
            JButton button = p.getButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO only if not active yet, otherwise just change
                    GameDialogBuilder.build(p);
                }
            });
            lbar.add(button);
        }

        // create info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new LineBorder(Color.black, 1));

        JButton turnButton = new JButton("End Turn");
        turnButton.setFocusable(false);
        turnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        // layout
        panel.setLayout(new MigLayout("wrap 1, fill", "", "[][][][][grow][][]"));
        panel.add(infPanel, "growx");
        panel.add(ubar.get());
        panel.add(miniMapPanel, "growx"); // because the extra space goes here
        panel.add(lbar.get());
        panel.add(infoPanel, "grow");
        panel.add(turnButton, "growx, alignx center");
        panel.add(new ClockLabel(), "alignx center");

        return panel;
    }

    @Override
    public void switchTo() {
        super.switchTo();

        // and load a basic scenario
        MainScreenManager.getInstance().loadInitialScenario();
    }
}
