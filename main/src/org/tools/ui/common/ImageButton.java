/*
 * Copyright (C) 2011 Trilarion
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
package org.tools.ui.common;

import java.awt.event.ActionListener;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * A Button with images for various states
 */
// TODO finish implementation
public class ImageButton {

    private static final long serialVersionUID = 1L;
    private final JLabel label;
    private ImageIcon disabledIcon;

    public ImageButton(ImageIcon icon, ActionListener action) {
        label = new JLabel();
        label.setIcon(icon);
        disabledIcon = new ImageIcon();
        disabledIcon.setImage(GrayFilter.createDisabledImage(icon.getImage()));
        label.setDisabledIcon(disabledIcon);
        // label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setFocusable(false);
        label.setBorder(null);
    }

    public JComponent getButton() {
        return label;
    }

    public void setToolTipText(String text) {
        label.setToolTipText(text);
    }

    public void setEnabled(boolean b) {
        label.setEnabled(b);
    }
}