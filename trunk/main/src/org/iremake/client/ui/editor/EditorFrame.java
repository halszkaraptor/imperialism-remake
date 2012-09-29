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
package org.iremake.client.ui.editor;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.tools.ui.UITools;

/**
 *
 * @author Trilarion 2012
 */
public class EditorFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String resources = "/data/game/art/graphics/ui/";

    public EditorFrame() {
        initComponents();
    }

    private void initComponents() {
        // frame specific
        setTitle("Title");  // set title
        setIconImage(new ImageIcon(getClass().getResource(resources + "icon.app.png")).getImage());  // set icon
        setUndecorated(true);   // undecorated
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);  // turn of usual exiting mechanisms
        setResizable(false);    // not resizable

        Dimension s = UITools.getScreenSize();
        setBounds(0, 0, s.width, s.height);

        // set background of content pane (which is also included in layered pane
        getContentPane().setBackground(Color.WHITE);    // white color
    }

}
