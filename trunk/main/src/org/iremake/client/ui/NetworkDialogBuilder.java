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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import org.iremake.client.ui.main.MainScreenBuilder;
import org.iremake.common.resources.Places;

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
     * @param owner
     * @param title
     * @param bounds
     * @return
     */
    public static JDialog makeDialog(JFrame owner, String title, Rectangle bounds) {
        // create general dialog
        JDialog dialog = CommonElementsFactory.makeDialog(owner, title, false, bounds);
        
        // create menu bar and add to dialog
        JToolBar menuBar = NetworkDialogBuilder.menuBar(owner);
        dialog.add(menuBar);        
        
        // client info panel
        JPanel clientInfoPanel = NetworkDialogBuilder.clientInfoPanel();
        dialog.add(clientInfoPanel);
        
        // server member list
        JList serverMemberList = NetworkDialogBuilder.serverInfoList();
        serverMemberList.setModel(new DefaultListModel());
        dialog.add(serverMemberList);
        
        // server chat window
        JTextArea serverChatText = new JTextArea();
        JScrollPane serverChatPane = new JScrollPane();
        dialog.add(serverChatPane);
        serverChatPane.setViewportView(serverChatText);
        serverChatPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        // chat input
        JTextField chatInputField = new JTextField();
        dialog.add(chatInputField);

        // define the layout
        // selectTree fixed width, infoPanel fixed height
        Container c = dialog.getContentPane();
        GroupLayout layout = new GroupLayout(c);
        c.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(menuBar).addComponent(clientInfoPanel).addGroup(layout.createSequentialGroup().addComponent(serverMemberList).addGroup(layout.createParallelGroup().addComponent(serverChatPane).addComponent(chatInputField))));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(menuBar).addComponent(clientInfoPanel).addGroup(layout.createParallelGroup().addComponent(serverMemberList).addGroup(layout.createSequentialGroup().addComponent(serverChatPane).addComponent(chatInputField))));

        return dialog;
    }
    
    private static JToolBar menuBar(JFrame owner) {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // load button
        JButton serverStartButton = CommonElementsFactory.makeButton(Places.GraphicsIcons, "scenario.button.load.png");

        // add buttons to tool bar
        bar.add(serverStartButton);

        return bar;        
    }    
    
    private static JPanel clientInfoPanel() {
        JPanel panel = new JPanel();
        return panel;
    }
    
    private static JList serverInfoList() {
        JList list = new JList();
        
        return list;
    }
}
