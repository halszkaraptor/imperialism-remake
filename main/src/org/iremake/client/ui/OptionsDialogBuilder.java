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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.Options;
import org.iremake.client.StartClient;
import org.iremake.common.BigBag;
import org.iremake.server.network.ServerStatusListener;

/**
 * Options dialog wiring.
 */
public class OptionsDialogBuilder {

    /**
     * No instantiation.
     */
    private OptionsDialogBuilder() {
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

        // create JTabbedpane
        JTabbedPane pane = new JTabbedPane();
        pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // add panels for each tab
        pane.add(OptionsDialogBuilder.generalOptionsPanel(), "General");
        pane.add(OptionsDialogBuilder.serverOptionsPanel(), "Server");

        // layout dialog (use simpel borderlayout);
        Container c = dialog.getContentPane();
        c.setLayout(new MigLayout("fill"));
        dialog.add(pane, "grow");
        
        return dialog;
    }

    /**
     *
     * @return
     */
    private static JPanel generalOptionsPanel() {
        JPanel panel = new JPanel();
        
        JCheckBox fullscreen = new JCheckBox("Start in Full Screen");
        fullscreen.setSelected(Options.FullScreenMode.getBoolean());
        fullscreen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                Options.FullScreenMode.putBoolean(selected);
                // TODO notification that it will be active after next start
            }
        });
        
        // layout
        panel.setLayout(new MigLayout());        
        panel.add(fullscreen);
        
        return panel;
    }
    
    private static JPanel serverOptionsPanel() {
        JPanel panel = new JPanel();
        JToggleButton serverStart = new JToggleButton("Toggle Server");
        serverStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton abstractButton = (AbstractButton) e.getSource();
                boolean selected = abstractButton.getModel().isSelected();                
                if (selected) {
                    BigBag.serverManager.start();
                } else {
                    BigBag.serverManager.stop();
                }
            }
        });
        JToolBar menuBar = new JToolBar();
        menuBar.add(serverStart);
        
        JPanel serverInfoPanel = new JPanel();
        serverInfoPanel.setBorder(new LineBorder(Color.black, 1));
        
        final JLabel status = new JLabel();
        BigBag.serverManager.addStatusListener(new ServerStatusListener() {
            @Override
            public void statusUpdate(String message) {
                status.setText(message);
            }
        });
        serverInfoPanel.add(status);
        
        // layout
        panel.setLayout(new MigLayout("wrap 1, fillx"));
        panel.add(menuBar);
        panel.add(serverInfoPanel, "height 100!, growx");
        
        return panel;
        
    }
}
