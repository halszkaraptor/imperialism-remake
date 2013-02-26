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
package org.tools.ui.notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for a notification. Just the message and notifying framework
 * without any UI stuff.
 *
 * You can add listeners to listen if the user takes knowledges about the
 * notification and you can store the notification message. Also you should be
 * able to set the notification visible or dispose of it, but this is
 * implementation dependent.
 */
public abstract class Notification {

    /* message of the notification */
    private String message;
    /* list of listeners to this notification */
    private List<NotificationListener> listeners = new ArrayList<>(3);

    /**
     * Sets the message text.
     *
     * @param message message text
     */
    public Notification(String message) {
        this.message = message;
    }

    /**
     * @return the message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Adds another listener.
     *
     * @param l a listener
     */
    public void addNotificationListener(NotificationListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener.
     *
     * @param l a listener
     */
    public void removeNotificationListener(NotificationListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all listeners by a boolean value if the user
     * accepts/acknowledges the notification or not. Not can also be invoked by
     * a time out of showing the notification, that means then the user did not
     * accept the notification within a certain time period.
     *
     * @param value user reaction
     */
    protected void notifyListeners(boolean value) {
        for (NotificationListener l : listeners) {
            l.notificationResult(value);
        }
    }

    /**
     * Shows the notification to the user for the first time.
     */
    public abstract void setVisible();

    /**
     * Stop showing the notification to the user.
     */
    public abstract void dispose();
}
