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

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.iremake.client.utils.Resources;
import org.tools.ui.UITools;

/**
 *
 * @author Trilarion 2012
 */
public class ScreenFrame extends JFrame {

    public ScreenFrame() {
        // undecorated
        setUndecorated(true);

        // turn of usual exiting mechanisms
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // not resizable
        setResizable(false);

        // set title
        setTitle("Title");

        // set icon
        setIconImage(Resources.getAsImage(Resources.fromUI("icon.app.png")));

        // maximize (without setting the window maximized setting)
        Dimension s = UITools.getScreenSize();
        setBounds(0, 0, s.width, s.height);
    }
}
