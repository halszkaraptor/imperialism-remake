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
package org.tools.ui.notification;

import javax.swing.JLayeredPane;
import javax.swing.RootPaneContainer;
import org.tools.ui.helper.GraphicsUtils;
import org.tools.ui.helper.WindowCorner;

// TODO make it so that if there are other notification panes that the last one is always above all others
/**
 *
 * The parent could either be JFrame or JDialog (both offer the getLayeredPane()
 * method) but since they have no common ... Do we need to validate() after
 * adding and removal?
 */
public class NotificationPane extends Notification {

    private JLayeredPane pane;

    /**
     *
     * @param parent
     * @param message
     */
    public NotificationPane(RootPaneContainer parent, String message) {
        super(message);

        pane = parent.getLayeredPane();

    }

    /**
     *
     * @param corner
     */
    public void setLocation(WindowCorner corner) {
        GraphicsUtils.setLocationRelativeTo(panel, pane, corner);
    }

    @Override
    public void setVisible() {
        pane.add(panel, JLayeredPane.POPUP_LAYER);
        pane.repaint();
    }

    @Override
    void dispose() {
        pane.remove(panel);
        pane.repaint();
    }
}