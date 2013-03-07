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
 * Interface for a option item in an option dialog. Such objects connect a UI
 * element with an Option.
 *
 * Idea is that in the end the item is queried whether is was modified or not
 * and if at least one item was modified and the user accepted to save the
 * changes then all modified items will be updated which mostly means that the
 * value of the corresponding Option is changed and possibly some other action
 * is done.
 */
public interface OptionsDialogItem {

    /**
     * Is this element been modified.
     *
     * @return True if modified.
     */
    public boolean isModified();

    /**
     * Writes the modified value back to the options.
     */
    public void updateOption();
}
