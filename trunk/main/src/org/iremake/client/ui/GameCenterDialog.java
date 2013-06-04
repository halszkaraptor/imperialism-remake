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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.DefaultListCellRenderer;
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
import org.iremake.common.network.messages.LoginData;
import org.iremake.common.network.messages.game.setup.TitleListEntry;
import org.iremake.common.network.messages.lobby.LobbyChatMessage;
import org.iremake.common.network.messages.lobby.LobbyListEntry;
import org.iremake.server.network.RemoteServer;
import org.tools.ui.ButtonBar;
import org.tools.ui.PanelWithBackground;
import org.tools.ui.SimpleListModel;

/**
 *
 */
public class GameCenterDialog extends UIDialog {

    private JTextArea chatHistory;
    private SimpleListModel<LobbyListEntry> lobbyListModel;

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
        localScenarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // close this dialog
                GameCenterDialog.this.close();
                // open another one
                UIDialog dialog = new LocalScenarioSetupDialog();
                dialog.start();
            }
        });

        JButton connectRemoteServerButton = Button.NetworkConnect.create();
        connectRemoteServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if already connect, do nothing
                if (RemoteClient.CONTEXT.isConnected()) {
                    FrameManager.getInstance().scheduleInfoMessage("Client already connect, disconnect first!");
                    return;
                }

                final JDialog dialog = GameCenterDialog.this.createSubDialog("Connect to remote Server", true);

                // text fields and buttons
                final JTextField ipField = new JTextField();
                if (RemoteServer.CONTEXT.isRunning()) {
                    ipField.setText("localhost");
                    ipField.setEditable(false);
                }
                final JTextField aliasField = new JTextField(Option.Client_Alias.get());
                JButton connectButton = new JButton("Connect");
                connectButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (RemoteClient.CONTEXT.start(ipField.getText())) {
                            // we connected
                            FrameManager.getInstance().scheduleInfoMessage("Connection successful");
                            RemoteClient.CONTEXT.addHandler(new LobbyHandler(chatHistory.getDocument(), lobbyListModel));
                            RemoteClient.CONTEXT.send(new LoginData(Option.General_Version.get(), aliasField.getText()));
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
                if (RemoteClient.CONTEXT.isConnected()) {
                    RemoteClient.CONTEXT.disconnect("User request disconnection.");
                    FrameManager.getInstance().scheduleInfoMessage("Disconnection successful");
                }
            }
        });

        // add buttons to tool bar
        ButtonBar bar = new ButtonBar();
        bar.add(localScenarioButton, connectRemoteServerButton, disconnectRemoteServerButton);
        return bar.get();
    }

    private Component createMemberList() {
        JList<LobbyListEntry> list = new JList<>();
        list.setOpaque(false);
        list.setBorder(CommonElements.createBorder("Lobby"));

        // model
        lobbyListModel = new SimpleListModel<>();
        list.setModel(lobbyListModel);

        // cell renderer (use defaultlistcellrenderer based on jlabel)
        list.setCellRenderer(new OurListCellRenderer(new OurListCellRenderer.ListCellInfoProvider() {
            @Override
            public String getToolTip(Object value) {
                // cast to TitleListEntry, we know it's one
                LobbyListEntry entry = (LobbyListEntry) value;
                return String.format("origin: %s, joined: %s", entry.ip, entry.joined);
            }
        }));

        return list;
    }

    private Component createChatPane() {
        // text area inside the chat pan
        chatHistory = CommonElements.createTextArea();
        chatHistory.setEditable(false);

        // chat pane
        JScrollPane chatPane = new JScrollPane();
        chatPane.setOpaque(false);
        chatPane.getViewport().setOpaque(false);
        chatPane.setBorder(CommonElements.createBorder("Chat history"));

        chatPane.setViewportView(chatHistory);
        chatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        chatPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });

        return chatPane;
    }

    private Component createChatInput() {
        // chat input field
        final JTextField chatInput = CommonElements.createTextField();
        chatInput.setBorder(CommonElements.createBorder("Send message"));

        chatInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if we are connected and have some text send it
                if (RemoteClient.CONTEXT.isConnected() && chatInput.getText().length() > 0) {
                    RemoteClient.CONTEXT.send(new LobbyChatMessage(chatInput.getText()));
                }
                // in any case clear the text again
                chatInput.setText("");
            }
        });

        return chatInput;
    }
}
