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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JToolBar;
import org.iremake.client.StartClient;
import org.iremake.client.resources.Loader;
import org.iremake.client.resources.Places;
import org.tools.ui.BrowserDialog;
import org.tools.ui.helper.GraphicsUtils;

/**
 *
 */
// TODO Dialogs and Frames are disposed now when they aren't needed, maybe for performance reason they should be re-used
// TODO Frames change between editor, start and main, should we work with rootpanes instead?
// TODO Special open source Font throughout the game (?)
// TODO background of dialogs is not white (panel in contentpane?)
public class StartScreenBuilder {

    private StartScreenBuilder() {
    }

    public static JFrame makeFrame() {
        JFrame frame = CommonElementsFactory.makeFrame();

        // create menu bar and add to frame
        JToolBar menuBar = StartScreenBuilder.menuBar(frame);
        frame.add(menuBar);

        // language drop down menu
        JComboBox<String> languageComboBox = new JComboBox<>();
        languageComboBox.setModel(new DefaultComboBoxModel<String>(new String[]{"English", "Spanish", "German", "Chinese"}));   // set model
        languageComboBox.setToolTipText("Select language");     // tooltip

        // background image
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(Loader.getAsIcon(Places.UI, "start.background.png"));    // set image

        // logo label
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(Loader.getAsIcon(Places.UI, "start.logo.png"));    // set image

        // version label
        JLabel versionLabel = new JLabel("version 0.1.0");
        versionLabel.setForeground(Color.WHITE);    // white color

        // get layered pane
        JLayeredPane pane = frame.getLayeredPane();

        // adding each component with correct depth and correct position, depending on frame size
        Dimension s = frame.getSize();
        Dimension d;

        // manual layout because it's layered one over another

        // add background image (centered)
        pane.add(backgroundLabel, new Integer(1));
        d = backgroundLabel.getPreferredSize();
        backgroundLabel.setBounds(s.width / 2 - d.width / 2, s.height / 2 - d.height / 2, d.width, d.height);

        // add logo (horizontally centerd, vertically at 40%)
        pane.add(logoLabel, new Integer(2));
        d = logoLabel.getPreferredSize();
        logoLabel.setBounds(s.width / 2 - d.width / 2, s.height * 4 / 10 - d.height / 2, d.width, d.height);

        // add version in the same layer (right, lower border)
        pane.add(versionLabel, new Integer(2));
        d = versionLabel.getPreferredSize();
        versionLabel.setBounds(s.width - d.width - 20, s.height - d.height - 20, d.width, d.height);

        // add menubar on top and position
        pane.add(menuBar, new Integer(3));
        d = menuBar.getPreferredSize();
        menuBar.setBounds(s.width / 2 - d.width / 2, s.height * 6 / 10 - d.height / 2, d.width, d.height);  // horizontally centered, vertically at 60%

        // add language combo box
        pane.add(languageComboBox, new Integer(4));
        d = languageComboBox.getPreferredSize();
        languageComboBox.setBounds(s.width * 7 / 10 - d.width / 2, s.height / 2 - d.height / 2, d.width, d.height);  // horizontally at 70%, vertically centered

        // set background of content pane (which is also included in layered pane)
        frame.getContentPane().setBackground(Color.BLACK);  // black background

        return frame;
    }

    private static JToolBar menuBar(final JFrame owner) {
        // tool bar
        JToolBar bar = CommonElementsFactory.makeToolBar();

        // scenario button
        JButton scenarioButton = CommonElementsFactory.makeButton(Places.UI, "start.button.scenario.png");
        scenarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension s = GraphicsUtils.getScreenSize();
                int w = 800;
                int h = 700;
                Rectangle bounds = new Rectangle(s.width / 2 - w / 2, s.height / 2 - h / 2, w, h);
                JDialog dialog = ScenarioDialogsBuilder.makeLoadDialog(owner, "Scenario - Start", bounds);
                dialog.setVisible(true);
            }
        });

        // network button
        JButton networkButton = CommonElementsFactory.makeButton(Places.UI, "start.button.network.png");
        networkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension s = GraphicsUtils.getScreenSize();
                int w = 800;
                int h = 700;
                Rectangle bounds = new Rectangle(s.width / 2 - w / 2, s.height / 2 - h / 2, w, h);
                JDialog dialog = CommonElementsFactory.makeDialog(owner, "Network center", bounds);
                dialog.setVisible(true);
            }
        });

        // options button
        JButton optionsButton = CommonElementsFactory.makeButton(Places.UI, "start.button.options.png");
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension s = GraphicsUtils.getScreenSize();
                int w = 800;
                int h = 700;
                Rectangle bounds = new Rectangle(s.width / 2 - w / 2, s.height / 2 - h / 2, w, h);
                JDialog dialog = OptionsDialogBuilder.makeDialog(owner, "Options", bounds);
                dialog.setVisible(true);
            }
        });

        // help button
        JButton helpButton = CommonElementsFactory.makeButton(Places.UI, "start.button.help.png");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URL index = Loader.getURL("/help/en_index.html");
                BrowserDialog dlg = new BrowserDialog(owner, "Help", false, index, index);
                dlg.setVisible(true);
                // TODO change decoration of dialog (via look and feel)
            }
        });

        // editor button
        JButton editorButton = CommonElementsFactory.makeButton(Places.UI, "start.button.editor.png");
        editorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension s = GraphicsUtils.getScreenSize();
                Rectangle bounds = new Rectangle(50, 50, s.width - 100, s.height - 100);
                JDialog dialog = EditorDialogBuilder.makeDialog(owner, "Editor", bounds);
                dialog.setVisible(true);
            }
        });

        // exit button
        JButton exitButton = CommonElementsFactory.makeButton(Places.UI, "start.button.exit.png");
        exitButton.addActionListener(new ActionListener() {    // add action listener
            @Override
            public void actionPerformed(ActionEvent e) {
                owner.dispose();
                StartClient.shutDown();
            }
        });

        // add buttons to toolbar
        bar.add(scenarioButton);
        bar.add(networkButton);
        bar.add(optionsButton);
        bar.add(helpButton);
        bar.add(editorButton);
        bar.add(exitButton);

        return bar;
    }
}
