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
package org.iremake.client.ui.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import org.iremake.client.ui.Factory;
import org.iremake.client.ui.StartFrame;
import org.iremake.client.ui.common.ScreenFrame;
import org.iremake.client.utils.Resources;
import org.tools.ui.UITools;

/**
 *
 * @author Trilarion 2012
 */
public class EditorFrame extends ScreenFrame {

    private static final long serialVersionUID = 1L;

    public EditorFrame() {
        initComponents();
    }

    private void initComponents() {
        // toolbar
        JToolBar menuBar = new JToolBar();
        menuBar.setFloatable(false);  // non floatable
        menuBar.setOpaque(false);     // transparent

        // exit button
        JButton exitButton = Factory.makeButton(Resources.fromEditor("button.exit.png"));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                StartFrame frame = new StartFrame();
                frame.setVisible(true);
            }
        });

        // add buttons to toolbar
        menuBar.add(exitButton);

        // tabbed pane
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // panels in the tabbed pane
        JPanel mapPanel = new JPanel();
        JPanel nationPanel = new JPanel();

        // add panels to tabbed pane
        tabPane.addTab("Map", mapPanel);
        tabPane.addTab("Nation", nationPanel);

        // set background of content pane (which is also included in layered pane
        Container content = getContentPane();
        content.setBackground(Color.WHITE);    // white color

        // layout
        GroupLayout layout = new GroupLayout(content);
        content.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(menuBar).addComponent(tabPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(menuBar).addComponent(tabPane));
    }
}
