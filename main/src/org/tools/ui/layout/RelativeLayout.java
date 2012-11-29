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
package org.tools.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import org.tools.utils.Pair;

/**
 * For positioning in layered panes, where elements can overlap. Position can be
 * given absolute or relative to total size of parent. The preferred size is the
 * actual parent's size, that means it must be set before to a constant
 * (maximized or resized).
 *
 * The position of each component is independently from others given by a
 * RelativeLayoutConstraint. These constraints have to be added with the method
 * addConstraint before.
 */
// TODO removing of components?
// TODO adding a second time a constraint (make the list a hastable instead)
// TODO LayoutManagerAdaper
// TODO Border of parent taken into account?
public class RelativeLayout implements LayoutManager {

    /**
     * Stores pairs of components and layout constraints.
     */
    private List<Pair<Component, RelativeLayoutConstraint>> list = new LinkedList<>();

    /**
     * @param name
     * @param comp
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * The preferred layout size is just the actual size of the parent.
     * Therefore this layout adapts to the size of the parent immediately and
     * may end up with zero space. (For example if the parent is fresh created
     * dialog and pack() is called).
     *
     * @param parent
     * @return
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getSize();
    }

    /**
     * No minimum size.
     *
     * @param parent
     * @return
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    /**
     * Add a new constraint. Only components with an added constraints will be
     * affected by this layout.
     *
     * @param component
     * @param constraint
     */
    public void addConstraint(Component component, RelativeLayoutConstraint constraint) {
        list.add(new Pair<>(component, constraint));
    }

    /**
     * Layouts the components by calculating the bounds for each component by
     * the constraint.
     *
     * @param parent
     */
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
