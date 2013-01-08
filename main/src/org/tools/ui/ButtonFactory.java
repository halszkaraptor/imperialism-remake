/*
 * Copyright (C) 2013 Trilarion
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

import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * A shortcut for the most typical configurations we are using buttons in.
 */
public class ButtonFactory {

    /**
     * Avoid instantiation.
     */
    private ButtonFactory() {
    }

    /**
     * Creates a borderless, textless button with image and tool tip and
     * adjustable but symmetrical margin.
     *
     * @param icon the icon of the button
     * @param tooltip tooltip text
     * @return a new button
     */
    public static JButton create(Icon icon, String tooltip) {

        JButton button = new JButton();
        // not focusable and no border by default
        button.setFocusable(false);
        button.setBorder(null);

        // set icon
        button.setIcon(icon);

        // set tooltip text
        button.setToolTipText(tooltip);

        return button;
    }
}
