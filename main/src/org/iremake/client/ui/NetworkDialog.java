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
 * Dialog for the network setup. Connecting to the server, seeing who else is
 * on. Chatting. Proposing a game.
 */
public class NetworkDialog extends UIDialog {

    /**
     * Setup of the dialog, layout of all components.
     */
    public NetworkDialog() {
        super("Network center");

        JPanel content = new JPanel();

        content.setLayout(new MigLayout("fill", "[][grow]", "[][grow][]"));
        content.add(createMenuBar(), "dock north");
        content.add(createInfoPanel(), "hmin 100, growx, span, wrap");
        content.add(createMemberList(), "wmin 100, hmin 100, growy, span 1 2");
        content.add(createChatPane(), "grow, wrap");
        content.add(createChatInput(), "growx");

        setContent(content);
    }

    /**
     * The menu bar.
     *
     * @return a component
     */
    private JComponent createMenuBar() {
        // load button
        JButton serverStartButton = Button.NetworkConnect.create();

        // add buttons to tool bar
        ButtonBar bar = new ButtonBar();
        bar.add(serverStartButton);

        return bar.get();
    }

    /**
     * The current connection info panel.
     *
     * @return a component
     */
    private JComponent createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Login"));

        JTextField networkAlias = new JTextField(Option.Client_Alias.get());

        JTextField serverAdress = new JTextField("127.0.0.1");

        panel.setLayout(new MigLayout("wrap 2"));
        panel.add(new JLabel("Network alias"));
        panel.add(networkAlias, "wmin 150");
        panel.add(new JLabel("Server address"));
        panel.add(serverAdress, "wmin 150");

        return panel;
    }

    /**
     * The member list on the server.
     *
     * @return a component
     */
    private JComponent createMemberList() {
        JList<String> list = new JList<>();
        DefaultListModel<String> model = new DefaultListModel<>();
        model.add(0, "Hallo");
        list.setModel(model);
        list.setBorder(BorderFactory.createTitledBorder("Lobby"));
        return list;
    }

    /**
     * The chat pane that contains all previous chat messages.
     *
     * @return
     */
    private JComponent createChatPane() {
        // server chat window
        JTextArea chatText = new JTextArea();
        JScrollPane chatPane = new JScrollPane();
        chatPane.setViewportView(chatText);
        chatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatPane.setBorder(BorderFactory.createTitledBorder("Messages"));
        return chatPane;
    }

    /**
     * Creates the chat input text line. Based on JTextField.
     *
     * @return a component
     */
    private JComponent createChatInput() {
        // chat input
        JTextField chatInput = new JTextField();
        chatInput.setBorder(BorderFactory.createTitledBorder("Send message"));
        return chatInput;
    }
}
