/*
 * Copyright (C) 2012 Trilarion 2012
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

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.tools.ui.helper.UITools;

/**
 *
 * @author Trilarion 2012
 */
public class MainScreenBuilder {

    private MainScreenBuilder() {
    }

    public static JDialog makeDialog(JFrame owner) {
        Dimension s = UITools.getScreenSize();
        Rectangle bounds = new Rectangle(0, 0, s.width, s.height);
        JDialog dialog = CommonElementsFactory.makeDialog(owner, null, bounds);
        dialog.setUndecorated(true);

        return dialog;
    }
}
