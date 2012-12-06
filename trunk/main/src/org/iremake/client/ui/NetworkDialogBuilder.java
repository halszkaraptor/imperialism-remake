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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.Option;
import org.tools.ui.ButtonBar;

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
        JComponent menuBar = NetworkDialogBuilder.menuBar();

        // client info panel
        JPanel clientInfoPanel = NetworkDialogBuilder.clientInfoPanel();

        // server member list
        JList<String> serverMemberList = NetworkDialogBuilder.serverInfoList();
        DefaultListModel<String> model = new DefaultListModel<>();
        model.add(0, "Hallo");
        serverMemberList.setModel(model);
        serverMemberList.setBorder(BorderFactory.createTitledBorder("Lobby"));

        // server chat window
        JTextArea serverChatText = new JTextArea();
        JScrollPane serverChatPane = new JScrollPane();
        serverChatPane.setViewportView(serverChatText);
        serverChatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        serverChatPane.setBorder(BorderFactory.createTitledBorder("Messages"));

        // chat input
        JTextField chatInputField = new JTextField();
        chatInputField.setBorder(BorderFactory.createTitledBorder("Send message"));

        // define the layout
        // selectTree fixed width, infoPanel fixed height
        content.setLayout(new MigLayout("wrap 2, fill", "[][fill, grow]", "[][][fill, grow][]"));
        content.add(menuBar, "span");
        content.add(clientInfoPanel, "height 100!, growx, span");
        content.add(serverMemberList, "wmin 100, hmin 100, growy, span 1 2");
        content.add(serverChatPane, "grow");
        content.add(chatInputField, "height pref!");

        FrameManager.getInstance().startDialog(content, "Network center");
    }

    private static JComponent menuBar() {
        // load button
        JButton serverStartButton = Button.ScenarioLoad.create();

        // add buttons to tool bar
        ButtonBar bar = new ButtonBar();
        bar.add(serverStartButton);

        return bar.get();
    }

    private static JPanel clientInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Login"));

        JTextField networkAlias = new JTextField(Option.NetworkAlias.get());

        JTextField serverAdress = new JTextField("127.0.0.1");

        panel.setLayout(new MigLayout("wrap 2"));
        panel.add(new JLabel("Network alias"));
        panel.add(networkAlias, "wmin 150");
        panel.add(new JLabel("Server address"));
        panel.add(serverAdress, "wmin 150");

        return panel;
    }

    private static JList<String> serverInfoList() {
        JList<String> list = new JList<>();

        return list;
    }
}
