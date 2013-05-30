/*
 * Copyright (C) 2013 Trilarion
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.Option;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.client.network.RemoteClient;
import org.iremake.client.network.handler.LobbyHandler;
import org.iremake.common.network.messages.LoginMessage;
import org.iremake.common.network.messages.lobby.LobbyChatMessage;
import org.tools.ui.ButtonBar;
import org.tools.ui.PanelWithBackground;

/**
 *
 */
public class GameCenterDialog extends UIDialog {
    
    private JTextArea chatHistory;

    public GameCenterDialog() {
        super("Game Center");

        JPanel content = new PanelWithBackground(IOManager.getAsImage(Places.GraphicsIcons, "misc/dialog.background.png"));

        content.setLayout(new MigLayout("wrap 2", "[][grow]", "[][grow][]"));
        content.add(createMenuBar(), "span 2");
        content.add(createMemberList(), "wmin 150, growy, spany 2");
        content.add(createChatPane(), "grow");
        content.add(createChatInput(), "growx");

        setContent(content);
    }

    private Component createMenuBar() {
        JButton localScenarioButton = Button.ScenarioStart.create();

        JButton connectRemoteServerButton = Button.NetworkConnect.create();
        connectRemoteServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if already connect, do nothing
                if (RemoteClient.INSTANCE.isConnected()) {
                    FrameManager.getInstance().scheduleInfoMessage("Client already connect, disconnect first!");
                    return;
                }
                
                final JDialog dialog = GameCenterDialog.this.createSubDialog("Connect to remote Server", true);

                // text fields and buttons
                final JTextField ipField = new JTextField();
                final JTextField aliasField = new JTextField(Option.Client_Alias.get());
                JButton connectButton = new JButton("Connect");
                connectButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (RemoteClient.INSTANCE.start(ipField.getText())) {
                            // we connected
                            FrameManager.getInstance().scheduleInfoMessage("Connection successful");
                            RemoteClient.INSTANCE.addHandler(new LobbyHandler(chatHistory));                            
                            RemoteClient.INSTANCE.send(new LoginMessage(Option.General_Version.get(), aliasField.getText()));                            
                        } else {
                            // we couldn't connect
                            FrameManager.getInstance().scheduleInfoMessage("Could not connect!");
                        }
                        // close dialog in any case
                        dialog.dispose();
                    }
                });


                // set layout and add elements
                dialog.setLayout(new MigLayout("wrap 2", "[sizegroup a][sizegroup b, fill]"));
                dialog.add(CommonElements.createLabel("Remote server IP address"));
                dialog.add(ipField, "wmin 100");
                dialog.add(CommonElements.createLabel("Network name"));
                dialog.add(aliasField);
                dialog.add(connectButton, "span 2, align center");

                // pack and display
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        JButton disconnectRemoteServerButton = Button.NetworkDisconnect.create();
        disconnectRemoteServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RemoteClient.INSTANCE.isConnected()) {
                    RemoteClient.INSTANCE.disconnect("User request disconnection.");
                }
            }
        });

        // add buttons to tool bar
        ButtonBar bar = new ButtonBar();
        bar.add(localScenarioButton, connectRemoteServerButton, disconnectRemoteServerButton);
        return bar.get();
    }

    private Component createMemberList() {
        JList<String> list = new JList<>();
        list.setBorder(BorderFactory.createTitledBorder("Lobby"));
        list.setOpaque(false);

        // model
        DefaultListModel<String> model = new DefaultListModel<>();
        model.add(0, "Hallo");
        list.setModel(model);

        return list;
    }

    private Component createChatPane() {
        // text area inside the chat pan
        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setOpaque(false);

        // chat pane
        JScrollPane chatPane = new JScrollPane();
        chatPane.setOpaque(false);
        chatPane.getViewport().setOpaque(false);
        chatPane.setBorder(BorderFactory.createTitledBorder("Chat history"));

        chatPane.setViewportView(chatHistory);
        chatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return chatPane;
    }

    private Component createChatInput() {
        // chat input field
        final JTextField chatInput = new JTextField();
        chatInput.setOpaque(false);
        chatInput.setBorder(BorderFactory.createTitledBorder("Send message"));
        
        chatInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if we are connected and have some text send it
                if (RemoteClient.INSTANCE.isConnected() && chatInput.getText().length() > 0) {
                    RemoteClient.INSTANCE.send(new LobbyChatMessage(chatInput.getText()));
                }
                // in any case clear the text again
                chatInput.setText("");
            }
        });

        return chatInput;
    }
}