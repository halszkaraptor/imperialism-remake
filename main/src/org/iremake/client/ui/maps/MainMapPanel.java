/*
 * Copyright (C) 2012 Trilarion 2012
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
package org.iremake.client.ui.maps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.iremake.client.ui.MapTiles;
import org.tools.ui.helper.GraphicsUtils;

/**
 *
 * @author Trilarion 2012
 */
public class MainMapPanel extends JPanel implements MainMapTileFocusChangedListener, MiniMapFocusChangedListener {

    private static final long serialVersionUID = 1L;
    private static final int GAP = 10;
    private Dimension size = new Dimension();
    private int w = 80;
    private int h = 80;
    private int r = 60;
    private int c = 100;
    private int ra = -1;
    private int ca = -1;
    private int x0 = -1;
    private int y0 = -1;
    private List<MainMapTileFocusChangedListener> tileFocusListeners = new LinkedList<>();
    private MainMapResizedListener resizedListener;
    private MapTiles map;

    public MainMapPanel() {

        map = new MapTiles();
        initComponents();
    }

    private void initComponents() {

        getSize(size);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                getSize(size);
                notifyResizedListener();
            }
        });

        // we listen to ourselves
        addTileFocusChangedListener(this);

        setOpaque(true);

        setBackground(Color.white);
        setBorder(new LineBorder(Color.black, 1));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x0 = e.getX();
                int y0 = e.getY();
                int r0 = (y0 - GAP) / h;
                int t = r0 % 2 == 1 ? w / 2 : 0;
                int c0 = (x0 - GAP - t) / w;
                if (r0 != ra || c0 != ca) {
                    ra = r0;
                    ca = c0;
                    notifyTileFocusChangedListeners();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (x0 >= 0 && y0 >= 0) {

            // staggered drawing
            for (int col = 0; col < c; col++) {
                for (int row = 0; row < r; row++) {
                    int x = GAP + col * w + ((row % 2 == 1) ? w / 2 : 0);
                    int y = GAP + row * h;
                    g2d.drawImage(map.getTileAt(col, row), x, y, null);
                }
            }
        }
    }

    public Dimension getAreaInTiles() {
        Dimension tileSize = map.getTileSize();
        return new Dimension(size.width / tileSize.width, size.height / tileSize.height);
    }

    private void notifyTileFocusChangedListeners() {
        for (MainMapTileFocusChangedListener l : tileFocusListeners) {
            l.newTileFocus(ra, ca);
        }
    }

    public void addTileFocusChangedListener(MainMapTileFocusChangedListener l) {
        tileFocusListeners.add(l);
    }

    public void removeTileFocusChangedListener(MainMapTileFocusChangedListener l) {
        tileFocusListeners.remove(l);
    }

    @Override
    public void newTileFocus(int row, int column) {
        // TODO
    }

    @Override
    public void newMiniMapFocus(Point center) {
        Dimension dim = GraphicsUtils.divideDimensions(size, map.getTileSize());
        x0 = Math.max(center.x - dim.width / 2, 0);
        y0 = Math.max(center.y - dim.height / 2, 0);
        repaint();
    }

    public void notifyResizedListener() {
        if (resizedListener != null) {
            Dimension dim = GraphicsUtils.divideDimensions(size, map.getTileSize());
            resizedListener.newMainMapSizeInTiles(dim);
        }
    }

    public void setResizedListener(MainMapResizedListener l) {
        resizedListener = l;
    }
}
