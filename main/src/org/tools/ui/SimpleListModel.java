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
import javax.swing.AbstractListModel;

/**
 * A simple, generic, read-only ListModel implementation for JList components
 * based (naturally) on a List.
 */
public class SimpleListModel<E> extends AbstractListModel<E> {

    private static final long serialVersionUID = 1L;
    private List<E> content;

    public SimpleListModel(List<E> list) {
        content = list; // TODO make copy?
    }

    @Override
    public int getSize() {
        return content.size();
    }

    @Override
    public E getElementAt(int index) {
        return content.get(index);
    }
}
