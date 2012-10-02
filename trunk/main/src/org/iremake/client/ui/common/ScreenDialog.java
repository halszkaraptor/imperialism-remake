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
package org.iremake.client.ui.common;

import java.awt.Frame;
import java.awt.Rectangle;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Trilarion 2012
 */
public class ScreenDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public ScreenDialog(JFrame owner, String title, Rectangle bounds) {
        super(owner, title, false);
        initComponents(bounds);
    }

    private void initComponents(Rectangle bounds) {
        
        // turn of usual exiting mechanisms
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        // not resizable
        setResizable(false);
        
        // set bounds
        setBounds(bounds);
    }
}
