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
import java.awt.Frame;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;

/**
 * Some common elements needed.
 *
 */
public class CommonElementsFactory {

    /**
     * No instantiation.
     */
    private CommonElementsFactory() {
    }

    /**
     *
     * @param iconPath
     * @return
     */
    public static JButton makeButton(Places base, String iconPath) {
        JButton button = new JButton();
        button.setFocusable(false);
        button.setIcon(IOManager.getAsIcon(base, iconPath));
        return button;
    }

    /**
     *
     * @return
     */
    public static JToolBar makeToolBar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);  // non floatable
        bar.setOpaque(false);     // transparent
        return bar;
    }

    /**
     *
     * @param owner
     * @param title
     * @param modal
     * @param bounds
     * @return
     */
    public static JDialog makeDialog(Frame owner, String title, boolean modal, Rectangle bounds) {
        JDialog dialog = new JDialog(owner, title, modal);

        // turn of usual exiting mechanisms
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // not resizable
        dialog.setResizable(false);

        // set bounds
        dialog.setBounds(bounds);

        // set background
        dialog.getContentPane().setBackground(Color.WHITE);    // white color

        return dialog;
    }

    /**
     *
     * The frame is maximized via setting the maximized state instead of
     * something like Dimension s = GraphicsUtils.getScreenSize();
     * frame.setBounds(0, 0, s.width, s.height); The reason is that some OS like
     * Ubuntu react funny on these two lines.
     *
     * @return
     */
    public static JFrame makeFrame() {
        JFrame frame = new JFrame();

        // undecorated and maximized
        frame.setUndecorated(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        // turn of usual exiting mechanisms
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // not resizable
        frame.setResizable(false);

        // set title
        frame.setTitle("Imperialism Remake");

        // set icon
        frame.setIconImage(IOManager.getAsImage(Places.GraphicsIcons, "icon.app.png"));

        return frame;
    }
}