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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
import org.iremake.common.network.messages.game.setup.SetupSelectionMessage;
import org.iremake.common.network.messages.game.setup.TitleListEntry;
import org.tools.ui.ButtonBar;
import org.tools.ui.PanelWithBackground;
import org.tools.ui.SimpleListModel;

/**
 * Selecting and starting a local scenario (all players are AI and the server
 * runs locally).
 */
public class LocalScenarioSetupDialog extends UIDialog implements MinimalSetupDialog {

    private SimpleListModel<TitleListEntry> titleListModel = new SimpleListModel<>();
    private JLabel mapLabel;
    private SetupHandler handler = new SetupHandler(this);

    /**
     * Setup of the dialog. Also starts the scanner and searches for scenarios.
     */
    public LocalScenarioSetupDialog() {
        super("Local Scenario");

        JPanel content = new PanelWithBackground(IOManager.getAsImage(Places.GraphicsIcons, "misc/dialog.background.png"));

        LocalClient.CONTEXT.addHandler(handler);
        LocalClient.CONTEXT.send(SetupActionMessage.GET_SCENARIOS);

        JList<TitleListEntry> titleList = new JList<>();
        titleList.setOpaque(false);
        titleList.setBorder(CommonElements.createBorder("Scenarios"));
        titleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        titleList.setModel(titleListModel);

        titleList.setCellRenderer(new OurListCellRenderer(new OurListCellRenderer.ListCellInfoProvider() {
            @Override
            public String getToolTip(Object value) {
                // cast to TitleListEntry, we know it's one
                TitleListEntry entry = (TitleListEntry) value;
                return String.format("%s", entry.title);
            }
        }));

        // listen to the list and update the selected scenario upon selection
        titleList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Integer id = titleListModel.getElementAt(e.getFirstIndex()).id;
                    LocalClient.CONTEXT.send(new SetupSelectionMessage(id));
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
     * Map panel.
     *
     * @return
     */
    private JPanel makeMapPanel() {
        JPanel panel = CommonElements.createPanel("Map");
        mapLabel = new JLabel();
        mapLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point2D.Float point = new Point2D.Float((float) e.getX() / mapLabel.getWidth(), (float) e.getY() / mapLabel.getHeight());
            }
        });
        
        panel.setLayout(new MigLayout("fill, insets 0"));
        panel.add(mapLabel, "grow");

        return panel;
    }

    /**
     * Info panel for the selected scenario. Also to adjust parameters.
     *
     * @return
     */
    private JPanel makeInfoPanel() {
        JPanel panel = CommonElements.createPanel("Info");
        return panel;
    }

    @Override
    public void setTitles(List<TitleListEntry> titles) {
        titleListModel.set(titles);
        titleListModel.sort();
        // TODO clear other displays
    }

    @Override
    public Dimension getMapSize() {
        return mapLabel.getSize();
    }

    @Override
    public void setMap(BufferedImage mapImage) {
        mapLabel.setIcon(new ImageIcon(mapImage));
    }
}
