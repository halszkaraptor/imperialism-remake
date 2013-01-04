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

import java.awt.Dimension;
import java.awt.Rectangle;
import org.tools.ui.utils.WindowCorner;

/**
 * Constraint for relative layout. Defined by four floating point values ax, ay,
 * bx, by and two integer values cx, cy.
 *
 * Left upper corner is defined by x: ax * parent-width + bx *
 * component-preferred-width + cx y: ay * parent-height + by *
 * component-preferred-height + cy
 *
 * And width, height are preferred width/height.
 *
 * Special cases (centered or cornered or without any offset(cx, cy) are
 * accessible via special methods for convenience.
 */
public class RelativeLayoutConstraint {

    /**
     *
     */
    private float ax, bx, ay, by;
    private int cx, cy;

    /**
     * Check values.
     *
     * @param ax
     * @param bx
     * @param cx
     * @param ay
     * @param by
     * @param cy
     */
    public RelativeLayoutConstraint(float ax, float bx, int cx, float ay, float by, int cy) {
        if (ax < 0 || ax > 1 || ay < 0 || ay > 1) {
            throw new IllegalArgumentException("ax, ay must be in [0, 1]");
        }
        if (bx < -1 || bx > 1 || by < -1 || by > 1) {
            throw new IllegalArgumentException("bx, by must be in [-1, 1]");
        }
        this.ax = ax;
        this.bx = bx;
        this.cx = cx;

        this.ay = ay;
        this.by = by;
        this.cy = cy;
    }

    /**
     * Calculate the bounds of the component given its preferred size and the
     * parent's size.
     *
     * @param parentSize
     * @param componentPreferredSize
     * @return
     */
    public Rectangle calculateBounds(Dimension parentSize, Dimension componentPreferredSize) {
        Rectangle r = new Rectangle();
        r.x = (int) (ax * parentSize.width + bx * componentPreferredSize.width + cx + 0.5); // + 0.5 for rounding
        r.y = (int) (ay * parentSize.height + by * componentPreferredSize.height + cy + 0.5);
        r.width = componentPreferredSize.width;
        r.height = componentPreferredSize.height;
        return r;
    }

    /**
     * Centered at the screen.
     *
     * @return
     */
    public static RelativeLayoutConstraint centered() {
        return RelativeLayoutConstraint.relative(0.5f, 0.5f);
    }

    /**
     * Centered at the relative (ax, ay) screen position.
     *
     * @param ax
     * @param ay
     * @return
     */
    public static RelativeLayoutConstraint relative(float ax, float ay) {
        return new RelativeLayoutConstraint(ax, -0.5f, 0, ay, -0.5f, 0);
    }

    /**
     * Special case for all corners. gx, gy are the gap distances to the nearest
     * borders.
     *
     * @param corner
     * @param gx
     * @param gy
     * @return
     */
    public static RelativeLayoutConstraint corner(WindowCorner corner, int gx, int gy) {
        switch (corner) {
        case NorthWest:
            return new RelativeLayoutConstraint(1f, -1f, -gx, 0f, 0f, gy);
        case NorthEast:
            return new RelativeLayoutConstraint(0f, 0f, gx, 0f, 0f, gy);
        case SouthWest:
            return new RelativeLayoutConstraint(1f, -1f, -gx, 1f, -1f, -gy);
        case SouthEast:
            return new RelativeLayoutConstraint(0f, 0f, gx, 1f, -1f, -gy);
        }
        return null;
    }
}
