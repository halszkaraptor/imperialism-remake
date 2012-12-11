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
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.main.MainScreen;
import org.iremake.client.ui.main.MainScreenManager;
import org.iremake.common.model.ScenarioScanner;
import org.tools.io.Resource;
import org.tools.ui.ButtonBar;

/**
 *
 */
public class NewLocalScenarioDialog extends UIDialog {

    Resource selectedScenario;

    public NewLocalScenarioDialog() {
        super("Local Scenario");
        JPanel content = new JPanel();

        // placeholder for the real things
        final ScenarioScanner scanner = new ScenarioScanner();
        scanner.doScan();
        final List<String> titles = scanner.getScenarios();
        JList<String> selectList = new JList<>();
        selectList.setBorder(BorderFactory.createTitledBorder("Scenarios"));
        selectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // setting up a JList model
        selectList.setModel(new AbstractListModel<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getSize() {
                return titles.size();
            }

            @Override
            public String getElementAt(int index) {
                return titles.get(index);
            }
        });
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

        JPanel mapPanel = makeMapPanel();
        JPanel infoPanel = makeInfoPanel();


        // create menu bar and add to dialog
        JComponent menuBar = makeMenuBar();

        // layout - selectTree fixed width, infoPanel fixed height
        content.setLayout(new MigLayout("wrap 1, fill", "", "[][fill, grow][]"));
        content.add(menuBar);
        content.add(selectList, "width 200!, split 2");
        content.add(mapPanel, "grow");
        content.add(infoPanel, "height 200!, growx");

        setContent(content);
    }

    private JPanel makeMapPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        return panel;
    }

    private JComponent makeMenuBar() {
        // start button
        JButton startButton = Button.ScenarioStart.create();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedScenario != null) {
                    dispose();
                    UIFrame frame = new MainScreen();
                    frame.switchTo();
                    MainScreenManager.getInstance().loadScenario(selectedScenario);
                }
            }
        });

        ButtonBar bar = new ButtonBar();
        bar.add(startButton);

        return bar.get();
    }

    private JPanel makeInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Info"));
        return panel;
    }
}
