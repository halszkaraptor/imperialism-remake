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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import org.iremake.common.model.ScenarioScanner;
import org.tools.io.Resource;
import org.tools.ui.ButtonBar;

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
     */
    public static void makeLoadDialog() {

        JPanel content = new JPanel();

        // placeholder for the real things
        final ScenarioScanner scanner = new ScenarioScanner();
        scanner.doScan();
        final List<String> titles = scanner.getScenarios();
        JList<String> selectList = new JList<>();
        selectList.setBorder(new LineBorder(Color.black, 1));
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
                    Resource resource = scanner.getScenarioResource(index);
                    // TODO load and update info
                }
            }
        });

        JPanel mapPanel = makeOverviewMapPanel();
        JPanel infoPanel = ScenarioDialogsBuilder.createInfoPanel();


        // create menu bar and add to dialog
        JComponent menuBar = ScenarioDialogsBuilder.loadMenuBar();

        // layout - selectTree fixed width, infoPanel fixed height
        content.setLayout(new MigLayout("wrap 1, fill", "", "[][fill, grow][]"));
        content.add(menuBar);
        content.add(selectList, "width 200!, split 2");
        content.add(mapPanel, "grow");
        content.add(infoPanel, "height 200!, growx");

        FrameManager.getInstance().startDialog(content, "Local Scenario");
    }

    private static JPanel makeOverviewMapPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 1));

        return panel;
    }

    private static JComponent loadMenuBar() {
        // start button
        JButton startButton = Button.ScenarioStart.create();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameManager.getInstance().getExitDialogListener().actionPerformed(null);
                FrameManager.getInstance().switchToMainScreen();
            }
        });

        ButtonBar bar = new ButtonBar();
        bar.add(startButton);

        return bar.get();
    }

    private static JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 1));
        return panel;
    }
}
