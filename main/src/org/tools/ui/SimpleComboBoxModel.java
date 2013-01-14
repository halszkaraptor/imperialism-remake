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
 *
 */
public class SimpleComboBoxModel<E> implements ComboBoxModel<E> {

    private final List<E> content;
    private int selectedIndex = -1;

    public SimpleComboBoxModel(List<E> content) {
        this.content = content;
    }

    @Override
    public void setSelectedItem(Object item) {
        selectedIndex = content.indexOf(item);
    }

    @Override
    public Object getSelectedItem() {
        if (selectedIndex != -1) {
            return content.get(selectedIndex);
        } else {
            return null;
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public int getSize() {
        return content.size();
    }

    @Override
    public E getElementAt(int index) {
        if (index >= 0 && index < content.size()) {
            return content.get(index);
        } else {
            return null;
        }
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
}
