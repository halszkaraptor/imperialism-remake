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
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import org.iremake.client.utils.Resources;
import org.tools.ui.UITools;

/**
 *
 * @author Trilarion 2012
 */
public class CommonElementsFactory {
    
    private CommonElementsFactory() {
    }

    /**
     *
     * @param iconPath
     * @return
     */
    public static JButton makeButton(String iconPath) {
        JButton button = new JButton();
        button.setFocusable(false);
        button.setIcon(Resources.getAsIcon(iconPath));
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
     * @param bounds
     * @return
     */
    public static JDialog makeDialog(JFrame owner, String title, Rectangle bounds) {
        JDialog dialog = new JDialog(owner, title, false);

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
     * @return
     */
    public static JFrame makeFrame() {
        JFrame frame = new JFrame();

        // undecorated
        frame.setUndecorated(true);

        // turn of usual exiting mechanisms
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // not resizable
        frame.setResizable(false);

        // set title
        frame.setTitle("App Title");

        // set icon
        frame.setIconImage(Resources.getAsImage(Resources.fromUI("icon.app.png")));

        // maximize (without setting the window maximized setting)
        Dimension s = UITools.getScreenSize();
        frame.setBounds(0, 0, s.width, s.height);        
        
        return frame;
    }
}
