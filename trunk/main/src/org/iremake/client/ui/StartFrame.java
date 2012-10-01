/*
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JToolBar;
import org.iremake.client.Main;
import org.iremake.client.ui.common.ScreenFrame;
import org.iremake.client.ui.editor.EditorFrame;
import org.iremake.client.utils.Resources;
import org.tools.ui.BrowserDlg;

/**
 *
 * We use the default null layout of JLayeredPane and set the elements near the
 * center in a simple way. For strange screen resolutions like 5000:1000 it
 * might look strange or elements might overlap, but not for 1650x1050 or
 * 1920x1280.
 *
 * @author Trilarion 2012
 */
public class StartFrame extends ScreenFrame {

    private static final long serialVersionUID = 1L;

    public StartFrame() {
        initComponents();
    }

    private void initComponents() {

        // toolbar
        JToolBar menuBar = new JToolBar();
        menuBar.setFloatable(false);    // non floatable
        menuBar.setOpaque(false);   // transparent

        // scenario button
        JButton scenarioButton = new JButton();
        scenarioButton.setIcon(Resources.getAsIcon(Resources.fromStartup("button.scenario.png")));    // set icon
        scenarioButton.setFocusable(false);     // not focusable (?)
        scenarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO implement
            }
        });

        // network button
        JButton networkButton = new JButton();
        networkButton.setIcon(Resources.getAsIcon(Resources.fromStartup("button.network.png")));    // set icon
        networkButton.setFocusable(false);     // not focusable (?)
        networkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO implement
            }
        });

        // options button
        JButton optionsButton = new JButton();
        optionsButton.setIcon(Resources.getAsIcon(Resources.fromStartup("button.options.png")));    // set icon
        optionsButton.setFocusable(false);     // not focusable (?)
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO implement
            }
        });

        // help button
        JButton helpButton = new JButton();
        helpButton.setIcon(Resources.getAsIcon(Resources.fromStartup("button.help.png")));    // set icon
        helpButton.setFocusable(false);     // not focusable (?)
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URL index = Resources.getAsURL("/help/en_index.html");
                BrowserDlg dlg = new BrowserDlg(StartFrame.this, "Help", false, index, index);
                dlg.setVisible(true);
                // TODO change decoration of dialog (via look and feel)
            }
        });

        // editor button
        JButton editorButton = new JButton();
        editorButton.setIcon(Resources.getAsIcon(Resources.fromStartup("button.editor.png")));    // set icon
        editorButton.setFocusable(false);     // not focusable (?)
        editorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartFrame.this.setVisible(false);
                EditorFrame frame = new EditorFrame();
                frame.setVisible(true);
            }
        });

        // exit button
        JButton exitButton = new JButton();
        exitButton.setIcon(Resources.getAsIcon(Resources.fromStartup("button.exit.png")));    // set icon
        exitButton.setFocusable(false);     // not focusable (?)
        exitButton.addActionListener(new ActionListener() {    // add action listener
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Main.shutDown();
            }
        });

        // add buttons to toolbar
        menuBar.add(scenarioButton);
        menuBar.add(networkButton);
        menuBar.add(optionsButton);
        menuBar.add(helpButton);
        menuBar.add(editorButton);
        menuBar.add(exitButton);

        // language drop down menu
        JComboBox<String> languageComboBox = new JComboBox<String>();
        languageComboBox.setModel(new DefaultComboBoxModel<String>(new String[]{"English", "Spanish", "German", "Chinese"}));   // set model
        languageComboBox.setToolTipText("Select language");     // tooltip

        // background image
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(Resources.getAsIcon(Resources.fromStartup("background.png")));    // set image

        // logo label
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(Resources.getAsIcon(Resources.fromStartup("logo.png")));    // set image

        // get layered pane
        JLayeredPane pane = getLayeredPane();

        // adding each component with correct depth and correct position, depending on frame size
        Dimension s = getSize();
        Dimension d;

        // add background image (centered)
        pane.add(backgroundLabel, new Integer(1));
        d = backgroundLabel.getPreferredSize();
        backgroundLabel.setBounds(s.width / 2 - d.width / 2, s.height / 2 - d.height / 2, d.width, d.height);

        // add logo (horizontally centerd, vertically at 40%)
        pane.add(logoLabel, new Integer(2));
        d = logoLabel.getPreferredSize();
        logoLabel.setBounds(s.width / 2 - d.width / 2, s.height * 4 / 10 - d.height / 2, d.width, d.height);

        // add menubar on top and position
        pane.add(menuBar, new Integer(3));
        d = menuBar.getPreferredSize();
        menuBar.setBounds(s.width / 2 - d.width / 2, s.height * 6 / 10 - d.height / 2, d.width, d.height);  // horizontally centered, vertically at 60%

        // add language combo box
        pane.add(languageComboBox, new Integer(4));
        d = languageComboBox.getPreferredSize();
        languageComboBox.setBounds(s.width * 7 / 10 - d.width / 2, s.height / 2 - d.height / 2, d.width, d.height);  // horizontally at 70%, vertically centered

        // set background of content pane (which is also included in layered pane
        getContentPane().setBackground(Color.BLACK);  // black background
    }
}
