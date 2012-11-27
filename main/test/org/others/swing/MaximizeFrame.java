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

import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 */
public class MaximizeFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create new frame (close with alt+f4)
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // set maximized
        frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);

        // frame.setResizable(false); // either comment out or leave in

        frame.setVisible(true);
    }
}
