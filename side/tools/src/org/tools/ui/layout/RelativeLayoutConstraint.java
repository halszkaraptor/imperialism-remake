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
import java.util.logging.Logger;
import org.tools.ui.utils.WindowCorner;
import org.tools.ui.utils.WindowSide;

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
     * @param parentBounds
     * @param componentSize
     * @return
     */
    public Rectangle calculateBounds(Rectangle parentBounds, Dimension componentSize) {
        Rectangle r = new Rectangle();
        r.x = (int) (parentBounds.x + ax * parentBounds.width + bx * componentSize.width + cx + 0.5); // + 0.5 for rounding
        r.y = (int) (parentBounds.y + ay * parentBounds.height + by * componentSize.height + cy + 0.5);
        r.width = componentSize.width;
        r.height = componentSize.height;
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
     * Special case for all sides (centered along the side). gap is the gap
     * distance to the side.
     *
     * @param side
     * @param gap
     * @return
     */
    public static RelativeLayoutConstraint side(WindowSide side, int gap) {
        switch (side) {
            case North:
                return new RelativeLayoutConstraint(0.5f, -0.5f, 0, 0, 0, gap);
            case East:
                return new RelativeLayoutConstraint(1, -1, -gap, 0.5f, -0.5f, 0);
            case South:
                return new RelativeLayoutConstraint(0.5f, -0.5f, 0, 1, -1, -gap);
            case West:
                return new RelativeLayoutConstraint(0, 0, gap, 0.5f, -0.5f, 0);
        }
        return centered();
    }

    /**
     * Special case for all corners. gapx, gapy are the gap distances to the
     * nearest borders.
     *
     * @param corner
     * @param gapx
     * @param gapy
     * @return
     */
    public static RelativeLayoutConstraint corner(WindowCorner corner, int gapx, int gapy) {
        switch (corner) {
            case NorthEast:
                return new RelativeLayoutConstraint(1, -1, -gapx, 0f, 0f, gapy);
            case NorthWest:
                return new RelativeLayoutConstraint(0f, 0f, gapx, 0f, 0f, gapy);
            case SouthEast:
                return new RelativeLayoutConstraint(1, -1, -gapx, 1, -1, -gapy);
            case SouthWest:
                return new RelativeLayoutConstraint(0f, 0f, gapx, 1, -1, -gapy);
        }
        return centered();
    }
    private static final Logger LOG = Logger.getLogger(RelativeLayoutConstraint.class.getName());
}
