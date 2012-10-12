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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 */
public class MiniMapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private Dimension focusSize;
    private Dimension mapSize;
    private Dimension tileSize;
    private Dimension size = new Dimension();
    private Point p0;
    private MiniMapFocusChangedListener focusChangedListener;

    public MiniMapPanel() {
        initComponents();
    }

    private void initComponents() {

        mapSize = new Dimension(100, 60);
        tileSize = new Dimension(2, 2);
        setPreferredSize(new Dimension(mapSize.width * tileSize.width, mapSize.height * tileSize.height));

        setBorder(new LineBorder(Color.black, 1));

        getSize(size);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                getSize(size);
            }
        });

        x = 10;
        y = 10;


        setOpaque(true); // by default is not

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Point p =  new Point(e.getX() / tileSize.width, e.getY() / tileSize.height);
                    if (!p.equals(p0)) {
                        p0 = p;
                        MiniMapPanel.this.repaint();
                        notifyFocusChangedListener();
                    }
                }
            }
        });

    }

    public void calcFocusSize(Dimension mapAreaInTiles) {
        focusSize = new Dimension(mapAreaInTiles.width * tileSize.width, mapAreaInTiles.height * tileSize.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // draw background
        g2d.setColor(Color.gray);
        g2d.fillRect(0, 0, size.width, size.height);

        //
        if (focusSize != null) {
            g2d.setColor(Color.gray);
            g2d.draw3DRect(x - focusSize.width / 2, y - focusSize.height / 2, focusSize.width, focusSize.height, true);
        }
    }

    private void notifyFocusChangedListener() {
        if (focusChangedListener != null) {
            focusChangedListener.newMiniMapFocus(x, y);
        }
    }

    public void setFocusChangedListener(MiniMapFocusChangedListener l) {
        focusChangedListener = l;
    }
}
