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
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/**
 * Testing Java's Full Screen Exclusive Mode API.
 *
 * In Linux/Ubuntu the task bar is not covered and lower right part of the frame
 * is not displayed because the frame is shifted to the right and below. The
 * size is the size of the screen. Really bad!
 *
 * On Windows it works as expected.
 *
 * Both however say that FSEM is supported.
 *
 * System: OpenJDK/Sun JRE/JDK 7 (1.7.0_09) on Windows 7 or Ubuntu 12.10
 */
public class FullScreenExclusiveFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create new jframe without decoration and not resizable
        final JFrame frame = new JFrame(FullScreenExclusiveFrame.class.getName() + " Test");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);


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

        // get graphics device
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = env.getDefaultScreenDevice();
        System.out.println("FSEM is supported: " + gd.isFullScreenSupported());

        // just add them in the most simplest layout
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 10));
        panel.setLayout(new FlowLayout());
        panel.add(infoLabel);
        frame.add(panel);

        // set frame full screen and start the timer
        gd.setFullScreenWindow(frame);
        timer.start();
    }
}
