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
package org.iremake.tests;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.tools.ui.helper.LookAndFeel;

/**
 * Evaluates if a button is good for representing a tile. No.
 */
public class Tile {

    public static void main(String args[]) {
        // set look and feel
        LookAndFeel.setSystemLookAndFeel();

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = Tile.createFrame();
                frame.setVisible(true);
            }
        });
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Tile Button Test");
        frame.setSize(new Dimension(400, 400));

        JButton button = new JButton();
        button.setFocusable(false);
        button.setIcon(new ImageIcon(Tile.class.getResource("/icons/terrain.plains.png")));

        frame.setLayout(null);
        frame.add(button);
        Dimension d = button.getPreferredSize();
        button.setBounds(100, 100, d.width, d.height);

        return frame;
    }
}
