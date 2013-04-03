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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 */
public class ImageMapLabel extends JLabel {

    /**
     *
     */
    public static class MapItem {

        public Rectangle area;
        public Point pos;
        public Image image;
        public ActionListener action;
    }
    /* */
    private MapItem active;
    /* */
    private List<MapItem> items = new LinkedList<>();

    /**
     *
     */
    public ImageMapLabel() {
        super();

        addMouseListener(new MouseAdapter() {
            /**
             *
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if (active != null) {
                    active.action.actionPerformed(null);
                }
            }

            /**
             * Just to make sure nothing remains when outside of the panel.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                if (active != null) {
                    updateActiveItem(null);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            /**
             *
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                // test if contained in any item
                int x = e.getX();
                int y = e.getY();
                for (MapItem item : items) {
                    // inside this one
                    if (x >= item.area.x && x <= item.area.x + item.area.width && y >= item.area.y && y <= item.area.y + item.area.height) {
                        if (item != active) {
                            updateActiveItem(item);
                        }
                        return;
                    }
                }
                // not inside anything
                if (active != null) {
                    updateActiveItem(null);
                }
            }
        });
    }

    /**
     * Adds another item.
     *
     * @param item
     */
    public void addMapItem(MapItem item) {
        // TODO checks, inside...
        items.add(item);
    }

    /**
     *
     * @param item
     */
    private void updateActiveItem(MapItem item) {
        active = item;
        repaint();
    }

    /**
     * Paints the overlay of the active region if needed.
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (active != null) {
            g2d.drawImage(active.image, active.pos.x, active.pos.y, this);
        }
    }
}
