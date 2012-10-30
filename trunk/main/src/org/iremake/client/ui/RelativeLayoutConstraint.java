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

import java.awt.Dimension;
import java.awt.Rectangle;
import org.tools.ui.helper.WindowCorner;

/**
 *
 */
public class RelativeLayoutConstraint {

    private float ax, bx, ay, by;
    private int cx, cy;

    /**
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
     *
     * @param parentSize
     * @param componentSize
     * @return
     */
    public Rectangle calculateBounds(Dimension parentSize, Dimension componentSize) {
        Rectangle r = new Rectangle();
        r.x = (int) (ax * parentSize.width + bx * componentSize.width + cx + 0.5);
        r.y = (int) (ay * parentSize.height + by * componentSize.height + cy + 0.5);
        r.width = componentSize.width;
        r.height = componentSize.height;
        return r;
    }

    /**
     *
     * @return
     */
    static RelativeLayoutConstraint centered() {
        return RelativeLayoutConstraint.relative(0.5f, 0.5f);
    }

    /**
     *
     * @param ax
     * @param ay
     * @return
     */
    static RelativeLayoutConstraint relative(float ax, float ay) {
        return new RelativeLayoutConstraint(ax, -0.5f, 0, ay, -0.5f, 0);
    }

    /**
     *
     * @param corner
     * @param gx
     * @param gy
     * @return
     */
    static RelativeLayoutConstraint corner(WindowCorner corner, int gx, int gy) {
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
