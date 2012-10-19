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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.tools.ui.helper.Vector2D;

/**
 *
 */
public class MiniMapPanel extends JPanel implements MainMapResizedListener {

    private static final long serialVersionUID = 1L;
    private Vector2D focusCenter;
    private Vector2D focusSize;
    private Vector2D mapSize;
    private Vector2D tileSize;
    private Dimension size = new Dimension();
    private MiniMapFocusChangedListener focusChangedListener;
    private MapModel map;

    public MiniMapPanel() {

        map = new MapModel();
        initComponents();
    }

    private void initComponents() {

        mapSize = new Vector2D(100, 60);
        tileSize = new Vector2D(2, 2);
        setPreferredSize(Vector2D.multiply(mapSize, tileSize).asDimension());

        setBorder(new LineBorder(Color.black, 1));

        getSize(size);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                getSize(size);
                // TODO adjust more?
            }
        });

        // set focus center to center
        focusCenter = new Vector2D(size);
        focusCenter.divideby(2);


        setOpaque(true); // by default is not

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && focusSize != null) {
                    int x0 = e.getX();
                    int y0 = e.getY();
                    x0 = Math.min(Math.max(x0, focusSize.a / 2), size.width - focusSize.a / 2);
                    y0 = Math.min(Math.max(y0, focusSize.b / 2), size.height - focusSize.b / 2);
                    Vector2D newCenter = new Vector2D(x0, y0);
                    if (!newCenter.equals(focusCenter)) {
                        focusCenter = newCenter;
                        MiniMapPanel.this.repaint();
                        notifyFocusChangedListener();
                    }
                }
            }
        });

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
            g2d.draw3DRect(focusCenter.a - focusSize.a / 2, focusCenter.b - focusSize.b / 2, focusSize.a, focusSize.b, true);
        }
    }

    private void notifyFocusChangedListener() {
        if (focusChangedListener != null) {
            focusChangedListener.newMiniMapFocus(Vector2D.divide(focusCenter, tileSize));
        }
    }

    public void setFocusChangedListener(MiniMapFocusChangedListener l) {
        focusChangedListener = l;
        // do it once
        notifyFocusChangedListener();
    }

    @Override
    public void newMainMapSizeInTiles(Vector2D size) {
        focusSize = Vector2D.multiply(size, tileSize);
        repaint();
    }
}
