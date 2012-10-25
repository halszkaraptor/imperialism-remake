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
package org.iremake.client.ui.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 */
public class MiniMapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dimension size = new Dimension();
    private Rectangle focus = new Rectangle();
    private MiniMapFocusChangedListener focusChangedListener;
    private MapModel model;
    private BufferedImage miniMap;

    public MiniMapPanel(MapModel model) {

        this.model = model;

        size.width = 200;
        size.height = 200 * model.getNumberRows() / model.getNumberColumns();
        this.setPreferredSize(size);

        miniMap = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < size.width; x++) {
            for (int y = 0; y < size.height; y++) {
                int column = model.getNumberColumns() * x / size.width; // rounding down
                int row = model.getNumberRows() * y / size.height;
                Color color = model.getTileColorAt(row, column);
                miniMap.setRGB(x, y, color.getRGB());
            }
        }

        focus.x = size.width / 2;
        focus.y = size.height / 2;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && focus.width > 0) {
                    int x0 = e.getX();
                    int y0 = e.getY();
                    x0 = Math.min(Math.max(x0, focus.width / 2), size.width - focus.width / 2);
                    y0 = Math.min(Math.max(y0, focus.height / 2), size.height - focus.height / 2);
                    if (focus.x != x0 || focus.y != y0) {
                        focus.x = x0;
                        focus.y = y0;
                        MiniMapPanel.this.repaint();
                        notifyFocusChangedListener();
                    }
                }
            }
        });

        setBorder(new LineBorder(Color.black, 1));
        setOpaque(true); // by default is not
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // draw background
        g2d.drawImage(miniMap, 0, 0, this);

        // the main map has told us their size, we can draw the focus rectangle
        if (focus.width > 0) {
            g2d.setColor(Color.gray);
            g2d.draw3DRect(focus.x - focus.width / 2, focus.y - focus.height / 2, focus.width, focus.height, true);
        }
    }

    private void notifyFocusChangedListener() {
        if (focusChangedListener != null && focus.width > 0) {
            focusChangedListener.newMiniMapFocus((float) focus.x / size.width, (float) focus.y / size.height);
        }
    }

    public void setFocusChangedListener(MiniMapFocusChangedListener l) {
        focusChangedListener = l;
    }

    public void setMainMapViewSize(float fractionRows, float fractionColumns) {
        focus.width = (int) (size.width * fractionColumns);
        focus.height = (int) (size.height * fractionRows);
        repaint();
        notifyFocusChangedListener();
    }
}
