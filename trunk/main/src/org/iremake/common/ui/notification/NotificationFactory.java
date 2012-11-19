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
package org.iremake.common.ui.notification;

import javax.swing.JFrame;
import org.iremake.common.ui.utils.WindowCorner;

/**
 *
 */
public class NotificationFactory {

    /**
     * To avoid instantiation because this is an utility class.
     */
    private NotificationFactory() {
    }

    /**
     * Creates a new notification dialog given a message and a listener. Returns
     * it.
     *
     * @param parent
     * @param message
     * @param listener
     * @return
     */
    public static NotificationDialog createDlg(JFrame parent, String message, NotificationListener listener) {

        NotificationDialog dlg = new NotificationDialog(parent, message);
        dlg.addNotificationListener(listener);

        return dlg;
    }

    /**
     * Creates a new notification pane given a message and a listener. Returns
     * it.
     *
     * @param parent
     * @param message
     * @param listener
     * @return
     */
    public static NotificationPane createPane(JFrame parent, String message, NotificationListener listener) {

        NotificationPane pane = new NotificationPane(parent, message);
        pane.addNotificationListener(listener);

        return pane;
    }

    // TODO if a second is created it should come on top, by default it is under??
    /**
     * Displays a notification in the South-East corner of a given window
     * displaying a given message. The message is faded in 2 seconds and stays
     * at least for x seconds or until the user clicks either on the message or
     * the close button. No action is taken then.
     *
     * @param parent The parent window
     * @param message
     */
    public static void createInfoPane(JFrame parent, String message) {
        NotificationPane pane = new NotificationPane(parent, message);

        pane.setFadeInTime(1500);
        pane.setOnTime(3000);
        pane.setFadeOutTime(1500);

        pane.setLocation(WindowCorner.SouthEast);

        pane.activate();
    }
}