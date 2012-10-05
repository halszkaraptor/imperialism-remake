/*
 * Copyright (C) 2012 Trilarion 2012
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
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.iremake.client.utils.Resources;
import org.tools.ui.common.ClockLabel;
import org.tools.ui.helper.UITools;

/**
 *
 * @author Trilarion 2012
 */
public class MainScreenBuilder {

    private MainScreenBuilder() {
    }

    public static JDialog makeDialog(JFrame owner) {
        Dimension s = UITools.getScreenSize();
        Rectangle bounds = new Rectangle(0, 0, s.width, s.height);
        JDialog dialog = CommonElementsFactory.makeDialog(owner, null, bounds);
        dialog.setUndecorated(true);

        // get layered pane
        JLayeredPane layers = dialog.getLayeredPane();

        // add clock label to layered pane
        JLabel clockLabel = new ClockLabel();
        layers.add(clockLabel, new Integer(2));

        // position clock
        Dimension d = clockLabel.getPreferredSize();
        clockLabel.setBounds(s.width - d.width - 10, s.height-d.height-10, d.width, d.height);

        // add control panel to layered pane and position
        JPanel controlPanel = MainScreenBuilder.createControlPanel();
        layers.add(controlPanel, new Integer(3));
        d = controlPanel.getPreferredSize();
        controlPanel.setBounds(s.width - d.width - 20, 20, d.width, d.height);


        return dialog;
    }

    private static JPanel createControlPanel() {
        JPanel panel = new JPanel();

        panel.setPreferredSize(new Dimension(300, 800));

        // create menu bar and add to frame
        JToolBar menuBar = MainScreenBuilder.createControlMenuBar();
        panel.add(menuBar);

        return panel;
    }

    private static JToolBar createControlMenuBar() {
        // toolbar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // exit button
        JButton exitButton = CommonElementsFactory.makeButton(Resources.fromUI("main.button.exit.png"));

        // save button
        JButton saveButton = CommonElementsFactory.makeButton(Resources.fromUI("main.button.save.png"));

        // add buttons to toolbar
        bar.add(exitButton);
        bar.add(saveButton);

        return bar;
    }
}
