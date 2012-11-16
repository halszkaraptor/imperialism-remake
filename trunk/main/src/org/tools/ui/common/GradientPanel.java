/*
 * Copyright (C) 2011 Trilarion
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
package org.tools.ui.common;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import javax.swing.JPanel;

/**
 * Panel with gradient fill background.
 * 
 * If the user calls setOpague
 */
public class GradientPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // TODO move outside
    public enum Direction {

        Horizontal(1, 0), Vertical(0, 1), DiagonalRightDown(1, 1), DiagonalRightUp(1, -1);
        private int dx, dy;

        Direction(int dx, int dy) {
            if (dx == 0 && dy == 0) {
                throw new IllegalArgumentException();
            }
            this.dx = dx;
            this.dy = dy;
        }

        public int getdx() {
            return dx;
        }

        public int getdy() {
            return dy;
        }
    }
    private Direction direction = Direction.Horizontal;
    private Color c1 = Color.white, c2 = Color.black;

    /**
     * 
     */
    public GradientPanel() {
        // use a double buffer
        super(true);
        setOpaque(false);
    }

    /**
     * 
     * @param direction 
     */
    public void setGradientDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * 
     * @param c1
     * @param c2 
     */
    public void setGradientColors(Color c1, Color c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    /**
     * 
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (!isOpaque()) {

            Graphics2D g2d = (Graphics2D) g;

            Paint oldPaint = g2d.getPaint();

            Paint gradientPaint = calcGradientPaint();

            g2d.setPaint(gradientPaint);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setPaint(oldPaint);
        }

        super.paintComponent(g);
    }

    /**
     * 
     * @return 
     */
    private GradientPaint calcGradientPaint() {
        float dx = (float) direction.getdx();
        float dy = (float) direction.getdy();

        float scale = Math.max(Math.abs(dx), Math.abs(dy));

        float width2 = (float) getWidth() / 2.f;
        float height2 = (float) getHeight() / 2.f;

        dx = dx / scale * width2;
        dy = dy / scale * height2;

        return new GradientPaint(width2 - dx, height2 - dy, c1, width2 + dx, height2 + dy, c2, false);
    }
}