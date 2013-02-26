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
import java.awt.Frame;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Tests the behavior of initially maximized frames depending on decoration or
 * resizability.
 *
 * * Linux: Frames are only seen if they resizable. Decoration or not doesn't
 * make much of a difference. They are resizable under both conditions.
 *
 * Windows: Frames that are set to state maximal or indeed maximized (and in the
 * foreground) if they aren't decorated, not dependent of whether they are
 * resizable or not. If they are decorated, however, they are only maximized if
 * they are also resizable and they might end up behind the task bar.
 */
public class MaximizeFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create new frame (close with alt+f4)
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);

        // options (set to true or false)
        frame.setUndecorated(true);
        frame.setResizable(true);

        // add a panel with border
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black, 20));
        frame.add(panel);

        // set visible
        frame.setVisible(true);
    }
    private static final Logger LOG = Logger.getLogger(MaximizeFrame.class.getName());
}
