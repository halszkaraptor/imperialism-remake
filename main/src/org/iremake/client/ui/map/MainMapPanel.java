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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.iremake.client.resources.TerrainLoader;

/**
 *
 */
public class MainMapPanel extends JPanel implements MiniMapFocusChangedListener {

    private static final long serialVersionUID = 1L;
    private Dimension size;
    private List<MainMapTileFocusChangedListener> tileFocusListeners = new LinkedList<>();
    private MapModel model;
    private Dimension tileSize;

    private int offsetRow = 0;
    private int offsetColumn = 0;

    private int hooveredRow = -1;
    private int hooveredColumn = -1;

    private int selectedRow = -1;
    private int selectedColumn = -1;

    public MainMapPanel(MapModel model) {

        this.model = model;

        tileSize = TerrainLoader.getTileSize();

        setOpaque(true);

        setBackground(Color.white);
        setBorder(new LineBorder(Color.black, 1));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int r = y / tileSize.height;
                int t = r % 2 != 0 ? tileSize.width / 2 : 0;
                int c = (x - t) / tileSize.height;
                /*
                 Vector2D r = new Vector2D(r0, c0);
                 if (!pa.equals(r)) {
                 pa = r;
                 notifyTileFocusChangedListeners();
                 }*/
            }
        });

    }

    public void setSizeAndPrepare(MiniMapPanel miniMap) {

        size = getSize();

        // tell the minimap
        // miniMap.newMainMapSizeInTiles(TOP_ALIGNMENT, TOP_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (size == null) {
            return;
        }

        /*
         if (p0.isNonNegative()) {

         // staggered drawing
         for (int column = 0; column < model.getNumberColumns(); column++) {
         for (int row = 0; row < model.getNumberRows(); row++) {
         int x = column * tileSize.width + ((row % 2 != 0) ? tileSize.width / 2 : 0);
         int y = row * tileSize.height;
         g2d.drawImage(model.getTileAt(row, column), x, y, null);
         }
         }
         }
         */
    }

    private void notifyTileFocusChangedListeners() {
        for (MainMapTileFocusChangedListener l : tileFocusListeners) {
            // l.newTileFocus(pa);
        }
    }

    public void addTileFocusChangedListener(MainMapTileFocusChangedListener l) {
        tileFocusListeners.add(l);
    }

    public void removeTileFocusChangedListener(MainMapTileFocusChangedListener l) {
        tileFocusListeners.remove(l);
    }

    @Override
    public void newMiniMapFocus(float x, float y) {
        /*
         Vector2D dim = Vector2D.divide(new Vector2D(size), model.getTileSize());
         p0.a = Math.max(center.a - dim.a / 2, 0);
         p0.b = Math.max(center.b - dim.b / 2, 0); */
        repaint();
    }
}
