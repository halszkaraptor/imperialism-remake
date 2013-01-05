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

/**
 * In UIDialogs if the user wants the dialog closed, this listener is used to
 * check if closing should really happen. Also all actions before closing can be
 * executed here.
 */
public interface WindowClosingListener {

    /**
     * User wants to close, do all neccessary actions here.
     *
     * @return True if window should be disposed, False otherwise
     */
    public boolean closing();
}
