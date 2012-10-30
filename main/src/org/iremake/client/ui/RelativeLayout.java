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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import org.tools.common.Pair;

/**
 *
 */
public class RelativeLayout implements LayoutManager {

    private List<Pair<Component, RelativeLayoutConstraint>> list = new LinkedList<>();

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    public void addConstraint(Component component, RelativeLayoutConstraint constraint) {
        list.add(new Pair<>(component, constraint));
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension size = parent.getSize();
        if (size.width == 0 || size.height == 0) {
            return;
        }
        for (Pair<Component, RelativeLayoutConstraint> entry : list) {
            Component c = entry.getA();
            if (parent.equals(c.getParent())) {
                Rectangle bounds = entry.getB().calculateBounds(size, c.getPreferredSize());
                c.setBounds(bounds);
            } else {
                // list.remove(entry); TODO -> leads to java.util.ConcurrentModificationException
            }
        }
    }
}
