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

import javax.swing.JComponent;

/**
 * Base class for all frame filling screens. Keeps a component and if needed replaces it in the frame.
 */
public class UIFrame {

    /* the ui element containing all the content */
    private JComponent content;

    /**
     * Sets the content.
     *
     * @param content a component
     */
    public void setContent(JComponent content) {
        this.content = content;
    }

    /**
     * Displays the content in the programm frame.
     */
    public void switchTo() {
        FrameManager.getInstance().switchTo(content);
    }
}
