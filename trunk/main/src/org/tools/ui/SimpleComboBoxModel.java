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

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 * A simple, generic, read-only implementation of the ComboBoxModel suitable for
 * JComboBox elements based on a List and a selection index. Since the list is not changed inside
 */
public class SimpleComboBoxModel<E> implements ComboBoxModel<E> {

    private final List<E> content;
    private int selectedIndex = -1;

    /**
     *
     * @param content
     */
    public SimpleComboBoxModel(List<E> list) {
        content = list; // TODO make copy?
    }

    /**
     *
     * @param item
     */
    @Override
    public void setSelectedItem(Object item) {
        selectedIndex = content.indexOf(item);
    }

    /**
     *
     * @return
     */
    @Override
    public Object getSelectedItem() {
        if (selectedIndex != -1) {
            return content.get(selectedIndex);
        } else {
            return null;
        }
    }

    /**
     *
     * @return
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     *
     * @return
     */
    @Override
    public int getSize() {
        return content.size();
    }

    /**
     *
     * @param index
     * @return
     */
    @Override
    public E getElementAt(int index) {
        if (index >= 0 && index < content.size()) {
            return content.get(index);
        } else {
            return null;
        }
    }

    /**
     *
     * @param l
     */
    @Override
    public void addListDataListener(ListDataListener l) {
    }

    /**
     *
     * @param l
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
    }
}
