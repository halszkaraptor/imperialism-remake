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

import java.awt.Frame;
import org.tools.ui.layout.RelativeLayoutConstraint;
import org.tools.ui.utils.IconLoader;
import org.tools.ui.utils.WindowCorner;

/**
 *
 */
    // TODO if a second is created it should come on top, by default it is under??
public class NotificationFactory {

    private static IconLoader defaultLoader;

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
    public static NotificationDialog createDlg(String message, IconLoader loader, Frame parent, NotificationListener listener) {

        NotificationDialog dlg = new NotificationDialog(message, loader, parent);
        dlg.addNotificationListener(listener);

        return dlg;
    }

    public static void setIconLoader(IconLoader loader) {
        defaultLoader = loader;
    }

    public static void showInfo(String message, Frame parent) {
        if (defaultLoader == null) {
            // TODO log
            return;
        }

        NotificationDialog dlg = new NotificationDialog(message, defaultLoader, parent);
        // set fading times, 1.5s in, 3s stay, 1.5s out
        dlg.setLocationRelativeTo(parent, RelativeLayoutConstraint.corner(WindowCorner.SouthEast, 5, 5));
        dlg.setVisible();
    }
}