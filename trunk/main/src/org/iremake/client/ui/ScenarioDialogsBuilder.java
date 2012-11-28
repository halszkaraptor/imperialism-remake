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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import org.iremake.common.model.ScenarioScanner;
import org.iremake.common.resources.Resource;

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
    public static JDialog makeLoadDialog(String title, Dimension size) {
        // create general dialog
        JDialog dialog = CommonElementsFactory.makeDialog(title, false, size);

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
        JToolBar menuBar = ScenarioDialogsBuilder.loadMenuBar();


        // layout - selectTree fixed width, infoPanel fixed height
        Container c = dialog.getContentPane();
        c.setLayout(new MigLayout("wrap 1, fill", "", "[][fill, grow][]"));
        dialog.add(menuBar);
        dialog.add(selectList, "width 200!, split 2");
        dialog.add(mapPanel, "grow");
        dialog.add(infoPanel, "height 200!, growx");

        return dialog;
    }

    private static JPanel makeOverviewMapPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 1));

        return panel;
    }

    private static JToolBar loadMenuBar() {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // start button
        JButton startButton = Button.ScenarioStart.create();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO close this dialog before
                FrameManager.getInstance().switchToMainScreen();
            }
        });

        // add buttons to tool bar
        bar.add(startButton);

        return bar;
    }

    private static JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 1));
        return panel;
    }
}
