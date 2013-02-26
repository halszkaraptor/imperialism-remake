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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Logger;
import javax.swing.border.Border;

/**
 * Opaque rounded border of given color.
 */
public class RoundedBorder implements Border {

    private final Insets insets;
    private final int arc, dr;
    private Color color;

    /**
     *
     * @param top
     * @param left
     * @param bottom
     * @param right
     * @param arc
     * @param color
     */
    public RoundedBorder(int top, int left, int bottom, int right, int arc, Color color) {
        if (top < 0 || left < 0 || bottom < 0 || right < 0 || arc < 0) {
            throw new IllegalArgumentException();
        }
        insets = new Insets(top, left, bottom, right);
        this.arc = arc;
        dr = (int) Math.ceil((1 - 1 / Math.sqrt(2)) * arc);
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (c.isOpaque()) {
            Graphics2D g2d = (Graphics2D) g;

            Color oldColor = g2d.getColor();

            g2d.setColor(color);

            Shape outer, inner;

            outer = new RoundRectangle2D.Float(insets.left, insets.top, width - insets.left - insets.right, height - insets.top - insets.bottom, arc, arc);
            inner = new Rectangle2D.Float(insets.left + dr, insets.top + dr, width - insets.left - insets.right - 2 * dr, height - insets.top - insets.bottom - 2 * dr);

            Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);

            path.append(outer, false);
            path.append(inner, false);

            g2d.fill(path);

            g2d.setColor(oldColor);
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(insets.top + dr, insets.left + dr, insets.right + dr, insets.bottom + dr);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
    private static final Logger LOG = Logger.getLogger(RoundedBorder.class.getName());
}
