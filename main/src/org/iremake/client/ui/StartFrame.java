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
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import org.iremake.client.Main;

/**
 *
 * @author Trilarion 2012
 */
public class StartFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private static final String resources = "/data/game/art/graphics/ui/startup/";

    public StartFrame() {
        initComponents();
    }

    private void initComponents() {

        // frame specific
        setTitle("Title");  // set title
        setIconImage(new ImageIcon(getClass().getResource(resources + "icon.app.png")).getImage());  // set icon
        setUndecorated(true);   // undecorated
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);  // turn of usual exiting mechanisms
        setAlwaysOnTop(true);   // always on top (?)
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);   // start maximized
        setResizable(false);    // not resizable
        setMinimumSize(new Dimension(1024, 768));   // minimal size = 1024x768 (?)
        setBackground(new Color(0, 0, 0));  // black background (?)

        // toolbar
        JToolBar menuBar = new JToolBar();
        menuBar.setFloatable(false);    // non floatable
        menuBar.setBorder(null);    // no border
        menuBar.setRollover(true);  // set roll over

        // scenario button
        JButton scenarioButton = new JButton();

        // network button
        JButton networkButton = new JButton();

        // options button
        JButton optionsButton = new JButton();

        // help button
        JButton helpButton = new JButton();

        // editor button
        JButton editorButton = new JButton();

        // exit button
        JButton exitButton = new JButton();
        exitButton.setIcon(new ImageIcon(getClass().getResource(resources + "button.exit.png")));    // set icon
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

        // add toolbar to frame
        add(menuBar);

        // language drop down menu
        JComboBox languageComboBox = new JComboBox();
        languageComboBox.setModel(new DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));   // set model
        languageComboBox.setToolTipText("select language");     // tooltip
        
        // background image
        JLabel backgroundLabel = new JLabel();
        
        // logo label
        JLabel logoLabel = new JLabel();
    }
}
