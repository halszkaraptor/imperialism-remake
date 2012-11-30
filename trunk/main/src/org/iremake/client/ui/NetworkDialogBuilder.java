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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.utils.GraphicsUtils;

/**
 *
 */
public class NetworkDialogBuilder {

    /**
     * No instantiation.
     */
    private NetworkDialogBuilder() {
    }

    /**
     *
     */
    public static void makeDialog() {
        JPanel content = new JPanel();

        // create menu bar and add to dialog
        JToolBar menuBar = NetworkDialogBuilder.menuBar();

        // client info panel
        JPanel clientInfoPanel = NetworkDialogBuilder.clientInfoPanel();

        // server member list
        JList<String> serverMemberList = NetworkDialogBuilder.serverInfoList();
        DefaultListModel<String> model = new DefaultListModel<>();
        model.add(0, "Hallo");
        serverMemberList.setModel(model);

        // server chat window
        JTextArea serverChatText = new JTextArea();
        JScrollPane serverChatPane = new JScrollPane();
        serverChatPane.setViewportView(serverChatText);
        serverChatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // chat input
        JTextField chatInputField = new JTextField();

        // define the layout
        // selectTree fixed width, infoPanel fixed height
        content.setLayout(new MigLayout("wrap 2, fill", "[][fill, grow]", "[][][fill, grow][]"));
        content.add(menuBar, "span, growx");
        content.add(clientInfoPanel, "height 100!, growx, span");
        content.add(serverMemberList, "wmin 100, hmin 100, growy, span 1 2");
        content.add(serverChatPane, "grow");
        content.add(chatInputField, "height pref!");

        FrameManager.getInstance().startDialog(content, "Network center");
    }

    private static JToolBar menuBar() {
        // toolbar
        JToolBar bar = GraphicsUtils.makeToolBar(false, false);

        // load button
        JButton serverStartButton = Button.ScenarioLoad.create();

        // add buttons to tool bar
        bar.add(serverStartButton);

        return bar;
    }

    private static JPanel clientInfoPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private static JList<String> serverInfoList() {
        JList<String> list = new JList<>();

        return list;
    }
}
