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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 *
 */
public class ImageMapLabel extends JLabel {

    private static final int DRAWING_ARC_SIZE = 10;

    /**
     *
     */
    private static class MapItem {

        public Rectangle area;
        public Point pos;
        public String tooltiptext;
        public Image image;
        public ActionListener action;

        public MapItem(Rectangle area, Point pos, String tooltiptext, Image image, ActionListener action) {
            this.area = area;
            this.pos = pos;
            this.tooltiptext = tooltiptext;
            this.image = image;
            this.action = action;
        }
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
                if (active != null && active.action != null) {
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
     *
     * @param area
     * @param pos
     * @param image
     * @param action
     */
    public void addMapItem(Rectangle area, Point pos, String tooltiptext, Image image, ActionListener action) {
        items.add(new MapItem(area, pos, tooltiptext, image, action));
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
        // paint original content (backgrond image)
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // paint borders
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(new Color(196, 196, 196, 128));
        for (MapItem item : items) {
            Rectangle r = item.area;
            g2d.drawRoundRect(r.x, r.y, r.width, r.height, DRAWING_ARC_SIZE, DRAWING_ARC_SIZE);
        }

        // paint active overlay and border
        if (active != null) {
            // draw image
            g2d.drawImage(active.image, active.pos.x, active.pos.y, this);

            // draw border
            g2d.setStroke(new BasicStroke(4));
            g2d.setColor(new Color(255, 255, 255, 164));
            Rectangle r = active.area;
            g2d.drawRoundRect(r.x, r.y, r.width, r.height, DRAWING_ARC_SIZE, DRAWING_ARC_SIZE);

            // draw tooltiptext
            Dimension size = getSize();
            Font font = UIManager.getFont("Label.font");
            // font = font.deriveFont(Font.BOLD, 16);
            font = font.deriveFont(14f);
            Rectangle bounds = font.getStringBounds(active.tooltiptext, g2d.getFontRenderContext()).getBounds();
            Insets insets = new Insets(3, 6, 3, 6);

            // draw rectangle
            g2d.setColor(new Color(0, 0, 0, 200));
            // g2d.fill3DRect(size.width / 2 - insets.left - bounds.width / 2, size.height - insets.top - insets.bottom - bounds.height - 10, bounds.width + insets.left + insets.right, bounds.height + insets.top + insets.bottom, true);
            g2d.fillRoundRect(size.width / 2 - insets.left - bounds.width / 2, size.height - insets.top - insets.bottom - bounds.height - 10, bounds.width + insets.left + insets.right, bounds.height + insets.top + insets.bottom, 8, 8);

            // draw string
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setColor(Color.white);
            g2d.setFont(font);
            g2d.drawString(active.tooltiptext, size.width / 2 - bounds.x - bounds.width / 2, size.height - bounds.y - insets.bottom - bounds.height - 10); // TODO antialiased
        }
    }
}
