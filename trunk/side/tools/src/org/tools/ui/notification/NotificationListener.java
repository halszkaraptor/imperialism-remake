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

/**
 * Interface for listening to a notification and finding out if the cancel
 * button has been clicked (not accepted) or if the label has been clicked
 * (accepted).
 *
 * If the notification is fading or moving out, it also invokes this method with
 * argument: false.
 */
public interface NotificationListener {

    /**
     * Invoked when Notification is clicked.
     *
     * @param value true is the used clicked on the label.
     */
    void notificationResult(boolean value);
}