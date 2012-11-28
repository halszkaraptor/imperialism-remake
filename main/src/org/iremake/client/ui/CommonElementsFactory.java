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
import java.awt.Frame;
import java.awt.Rectangle;
import javax.swing.JDialog;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

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
     * @param title
     * @param size
     * @param modal
     * @return
     */
    public static JDialog makeDialog(String title, boolean modal, Dimension size) {

        // TODO make the dialog undecorated but resizable and moveable


        final Frame parent = FrameManager.getInstance().getFrame();

        JDialog dialog = new JDialog(parent, title, modal);

        // turn of usual exiting mechanisms
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // not resizable
        dialog.setResizable(false);

        // set bounds
        Rectangle pb = parent.getBounds();
        Rectangle bounds = new Rectangle(pb.x+pb.width/2-size.width/2,pb.y+pb.height/2-size.height/2,size.width,size.height);
        dialog.setBounds(bounds);

        // set background
        dialog.getContentPane().setBackground(Color.WHITE);    // white color

        return dialog;
    }
}