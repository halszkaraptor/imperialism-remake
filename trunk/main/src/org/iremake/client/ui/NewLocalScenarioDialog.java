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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.client.network.LocalClient;
import org.iremake.client.network.handler.SetupHandler;
import org.iremake.client.ui.main.MainScreen;
import org.iremake.common.network.messages.game.setup.SetupActionMessage;
import org.iremake.common.network.messages.game.setup.TitleListEntry;
import org.tools.ui.ButtonBar;
import org.tools.ui.PanelWithBackground;
import org.tools.ui.SimpleListModel;

/**
 * Selecting and starting a local scenario (all players are AI and the server runs locally).
 */
public class NewLocalScenarioDialog extends UIDialog {

    private SimpleListModel<TitleListEntry> titleListModel = new SimpleListModel<>();

    /**
     * Setup of the dialog. Also starts the scanner and searches for scenarios.
     */
    public NewLocalScenarioDialog() {
        super("Local Scenario");

        JPanel content = new PanelWithBackground(IOManager.getAsImage(Places.GraphicsIcons, "misc/dialog.background.png"));

        LocalClient.CONTEXT.addHandler(new SetupHandler(titleListModel));
        LocalClient.CONTEXT.send(SetupActionMessage.GET_SCENARIOS);

        JList<TitleListEntry> titleList = new JList<>();
        titleList.setBorder(CommonElements.createBorder("Scenarios"));
        titleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        titleList.setModel(titleListModel);
        // listen to the list and update the selected scenario upon selection
        titleList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Integer id = titleListModel.getElementAt(e.getFirstIndex()).id;
                    // LocalClient.CONTEXT.send(new SetupSelectionMessage(id));
                }
            }
        });

        // layout - selectTree fixed width, infoPanel fixed height
        content.setLayout(new MigLayout("wrap 1, fill", "", "[][fill, grow][]"));
        content.add(makeMenuBar());
        content.add(titleList, "width 200!, split 2");
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
                    close();
                    MainScreen frame = new MainScreen();
                    // frame.switchTo(selectedScenario);
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
        JPanel panel = CommonElements.createPanel("Info");
        return panel;
    }
}
