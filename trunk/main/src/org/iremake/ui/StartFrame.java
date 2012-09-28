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
package org.iremake.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import org.iremake.Main;

/**
 *
 * @author Trilarion 2012
 */
public class StartFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public StartFrame() {
        initComponents();
    }

    private void initComponents() {

        // set title
        setTitle("Title");

        // set icon
        setIconImage(new ImageIcon(getClass().getResource("/data/images/gui/icons/app_icon.png")).getImage());

        // undecorated
        setUndecorated(true);

        // turn of usual exiting mechanisms
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // always on top (?)
        setAlwaysOnTop(true);

        // start maximized
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        // not resizable
        setResizable(false);

        // minimal size = 1024x768 (not needed because not resizable)
        setMinimumSize(new Dimension(1024, 768));

        // black background
        setBackground(new Color(0, 0, 0));
        
        // toolbar
        JToolBar menuBar = new JToolBar();
        
        // non floatable
        menuBar.setFloatable(false);
        
        // no border
        menuBar.setBorder(null);
        
        // set roll over
        menuBar.setRollover(true);
        
        // exit button
        JButton exitButton = new JButton();
        
        // set icon
        exitButton.setIcon(new ImageIcon(getClass().getResource("/data/images/gui/icons/start_new_world.png")));
        
        // add action listener
        exitButton.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Main.shutDown();
            }
        });
        
        // add buttons to toolbar
        menuBar.add(exitButton);
        
        // add toolbar to frame
        add(menuBar);
    }
}
