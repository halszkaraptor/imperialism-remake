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
package org.iremake.client.ui.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.tools.ui.helper.Vector2D;

/**
 *
 * @author Trilarion 2012
 */
public class MainMapPanel extends JPanel implements MainMapTileFocusChangedListener, MiniMapFocusChangedListener {

    private static final long serialVersionUID = 1L;
    private Dimension size = new Dimension();
    private int w = 80;
    private int h = 80;
    private int r = 60;
    private int c = 100;
    private Vector2D pa = new Vector2D(-1, -1);
    private Vector2D p0 = new Vector2D(-1, -1);
    private List<MainMapTileFocusChangedListener> tileFocusListeners = new LinkedList<>();
    private MainMapResizedListener resizedListener;
    private MapModel map;

    public MainMapPanel() {

        map = new MapModel();
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
                int r0 = y0 / h;
                int t = r0 % 2 != 0 ? w / 2 : 0;
                int c0 = (x0 - t) / w;
                Vector2D r = new Vector2D(r0, c0);
                if (!pa.equals(r)) {
                    pa = r;
                    notifyTileFocusChangedListeners();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (p0.isNonNegative()) {

            // staggered drawing
            for (int col = 0; col < c; col++) {
                for (int row = 0; row < r; row++) {
                    int x = col * w + ((row % 2 != 0) ? w / 2 : 0);
                    int y = row * h;
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
            l.newTileFocus(pa);
        }
    }

    public void addTileFocusChangedListener(MainMapTileFocusChangedListener l) {
        tileFocusListeners.add(l);
    }

    public void removeTileFocusChangedListener(MainMapTileFocusChangedListener l) {
        tileFocusListeners.remove(l);
    }

    @Override
    public void newTileFocus(Vector2D tile) {
        // TODO
    }

    @Override
    public void newMiniMapFocus(Vector2D center) {
        Vector2D dim = new Vector2D(size);
        dim.divideby(map.getTileSize());
        p0.a = Math.max(center.a - dim.a / 2, 0);
        p0.b = Math.max(center.b - dim.b / 2, 0);
        repaint();
    }

    public void notifyResizedListener() {
        if (resizedListener != null) {
            Vector2D dim = new Vector2D(size);
            dim.divideby(map.getTileSize());
            resizedListener.newMainMapSizeInTiles(dim);
        }
    }

    public void setResizedListener(MainMapResizedListener l) {
        resizedListener = l;
    }
}
