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
package org.tools.ui;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JDialog;

/**
 * Support for custom decoration of dialogs/frames.
 */
// TODO is this used?
public class CustomDecoratedDlg extends JDialog {

    private static final long serialVersionUID = 1L;

    public CustomDecoratedDlg(Frame owner) {
        super(owner);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.yellow, 5));

        // ComponentMover cm = new ComponentMover();
        // cm.registerComponent(this);
        
        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(this);
        // cm.setDragInsets(cr.getDragInsets());
    }
}
