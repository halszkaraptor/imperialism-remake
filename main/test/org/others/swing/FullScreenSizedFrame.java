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
package org.others.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/**
 * Tests different ways of maximizing a JFrame.
 *
 * Under Linux/Ubuntu the task bars are never fully covered. Maximizing though
 * shifts the window decoration to the top task bar and frees some vertical
 * space (is Undecorated is false), all other modes give the same result.
 *
 * Under Windows the behavior is different. Undecorated frames are always in the
 * foreground, decorated can be behind the task bar. With maximum bounds its
 * never covering the task bar, maximized is only covering the task bar when
 * undecorated and the other modes always cover the task bar but might behind
 * the task bar.
 *
 * So far I could test this only for single monitor system. It will be
 * interesting if there are differences for dual monitor systems.
 */
public class FullScreenSizedFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create a new frame
        final JFrame frame = new JFrame(FullScreenSizedFrame.class.getName() + " Test");
        frame.setSize(new Dimension(400, 400));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        // frame.setUndecorated(true); // or false

        // info label and timer which updates the label with the frames bounds and if it is maximized
        final JLabel infoLabel = new JLabel();
        final Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maximized = (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0 ? "maximized" : "not maximized";
                infoLabel.setText(frame.getBounds().toString() + " " + maximized);
            }
        });

        // have to stop the timer at the end
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
            }
        });

        // button for resetting
        JButton button0 = new JButton("reset");
        button0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setSize(400, 400);
            }
        });

        // toggle button for maximization
        JToggleButton button1 = new JToggleButton("maximized");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton abstractButton = (AbstractButton) e.getSource();
                boolean selected = abstractButton.getModel().isSelected();
                if (selected) {
                    frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
                } else {
                    frame.setExtendedState(frame.getExtendedState() & ~Frame.MAXIMIZED_BOTH);
                }

            }
        });

        // gets the screen size and tries to set to 0, 0, screensize
        JButton button2 = new JButton("screen size");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension s = tk.getScreenSize();
                frame.setBounds(0, 0, s.width, s.height);
            }
        });

        // gets maximum bounds
        JButton button3 = new JButton("maximum bounds");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                frame.setBounds(env.getMaximumWindowBounds());
            }
        });

        // gets bounds for the first monitor
        JButton button5 = new JButton("Single monitor bounds");
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] gs = env.getScreenDevices();
                GraphicsConfiguration gd = gs[0].getDefaultConfiguration();
                Rectangle bounds = gd.getBounds();
                frame.setBounds(bounds);
            }
        });

        // just add them in the most simplest layout and in a panel with a border
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 10));
        panel.setLayout(new FlowLayout());
        frame.add(panel);

        panel.add(button0);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button5);
        panel.add(infoLabel);

        // set frame visible and start the timer
        frame.setVisible(true);
        timer.start();
    }
}