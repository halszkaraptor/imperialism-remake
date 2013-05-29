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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.client.ui.main.MainScreen;
import org.iremake.server.client.ScenarioScanner;
import org.tools.io.Resource;
import org.tools.ui.ButtonBar;
import org.tools.ui.PanelWithBackground;
import org.tools.ui.SimpleListModel;

/**
 * Selecting and starting a local scenario (all players are AI and the server runs locally).
 */
public class NewLocalScenarioDialog extends UIDialog {

    private static final Logger LOG = Logger.getLogger(NewLocalScenarioDialog.class.getName());
    /* pointing to the selected scenario */
    private Resource selectedScenario;

    /**
     * Setup of the dialog. Also starts the scanner and searches for scenarios.
     */
    public NewLocalScenarioDialog() {
        super("Local Scenario");
        JPanel content = new PanelWithBackground(IOManager.getAsImage(Places.GraphicsIcons, "misc/dialog.background.png"));

        // create a scanner for scenarios and scan
        final ScenarioScanner scanner = new ScenarioScanner();
        scanner.doScan();
        // put them in a list
        final List<String> titles = scanner.getScenarios();
        JList<String> selectList = new JList<>();
        selectList.setBorder(BorderFactory.createTitledBorder("Scenarios"));
        selectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // setting up a JList model
        ListModel<String> model = new SimpleListModel<>(titles);
        selectList.setModel(model);
        // listen to the list and update the selected scenario upon selection
        selectList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = e.getFirstIndex();
                    selectedScenario = scanner.getScenarioResource(index);
                    // TODO load and update info
                }
            }
        });

        // create menu bar and add to dialog
        JComponent menuBar = makeMenuBar();

        // layout - selectTree fixed width, infoPanel fixed height
        content.setLayout(new MigLayout("wrap 1, fill", "", "[][fill, grow][]"));
        content.add(menuBar);
        content.add(selectList, "width 200!, split 2");
        content.add(makeMapPanel(), "grow");
        content.add(makeInfoPanel(), "height 200!, growx");

        setContent(content);
    }

    /**
     * Map panel.
     * @return
     */
    private JPanel makeMapPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        return panel;
    }

    /**
     * Menu bar holding all possible actions.
     *
     * @return
     */
    private JComponent makeMenuBar() {
        // start button
        JButton startButton = Button.ScenarioStart.create();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedScenario != null) {
                    close();
                    MainScreen frame = new MainScreen();
                    frame.switchTo(selectedScenario);
                }
            }
        });

        ButtonBar bar = new ButtonBar();
        bar.add(startButton);

        return bar.get();
    }

    /**
     * Info panel for the selected scenario. Also to adjust parameters.
     * @return
     */
    private JPanel makeInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Info"));
        return panel;
    }
}
