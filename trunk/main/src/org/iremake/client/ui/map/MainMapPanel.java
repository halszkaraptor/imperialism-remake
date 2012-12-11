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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.iremake.client.resources.TerrainLoader;
import org.iremake.client.ui.model.UIScenario;
import org.iremake.common.model.MapPosition;

/**
 * The Main map display, currently used in the editor.
 */
// TODO drawing into border? (make border thicker, then you see it)
public class MainMapPanel extends JPanel implements MiniMapFocusChangedListener {

    private static final long serialVersionUID = 1L;
    private Dimension size = new Dimension();
    private List<MapTileListener> tileListeners = new LinkedList<>();
    private UIScenario model;
    private Dimension tileSize;
    private MapPosition offset = new MapPosition();
    private MapPosition hoover = new MapPosition();

    /**
     *
     * @param model
     */
    public MainMapPanel(final UIScenario model) {

        this.model = model;

        tileSize = TerrainLoader.getTileSize();

        setOpaque(true);

        hoover.setOff();

        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.black, 1));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                MapPosition p = getPositionFromXY(e.getX(), e.getY());

                // TODO the white pieces at the edge are handled how?
                if (model.containsPosition(p)) {
                    // inside, check if over new tile
                    if (!p.equals(hoover)) {
                        hoover.setFrom(p);
                        // hoovered tile changed
                        repaint(); // TODO only the two tiles
                        notifyTileFocusChangedListeners();
                    }
                } else {
                    // outside of area, deselect
                    hoover.setOff();
                    repaint(); // TODO only the one tile
                    notifyTileFocusChangedListeners();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    MapPosition p = getPositionFromXY(e.getX(), e.getY());

                    if (model.containsPosition(p)) {
                        notifyTileClickedListeners(p);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoover.setOff();
                repaint();
                notifyTileFocusChangedListeners();
            }
        });

    }

    /**
     * From mouse to tile.
     *
     * @param x
     * @param y
     * @return
     */
    private MapPosition getPositionFromXY(int x, int y) {
        MapPosition p = new MapPosition();
        p.row = y / tileSize.height + offset.row;
        int shift = p.row % 2 != 0 ? tileSize.width / 2 : 0;
        // -9..9/10 == 0, avoid negative divisions, because rounding is different there
        p.column = (x - shift + tileSize.width) / tileSize.width - 1 + offset.column;
        return p;
    }

    /**
     * Paints tiles and focus rectangle.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // TODO insets/border? do we need to subtract?
        getSize(size);

        // first just fill the background
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, size.width, size.height);

        // draw the tiles
        int drawnRows = size.height / tileSize.height;
        int drawnColumns = size.width / tileSize.width;
        // we draw one more in each direction to also get show half tiles
        // r and c are row and column on the screen, we start at the left, upper corner of the panel
        for (int r = -1; r < drawnRows + 1; r++) {
            for (int c = -1; c < drawnColumns + 1; c++) {
                // real row and column
                int row = r + offset.row;
                int column = c + offset.column;
                MapPosition p = new MapPosition(row, column);
                // still on the map?
                if (row >= 0 && row < model.getNumberRows() && column >= 0 && column < model.getNumberColumns()) {
                    // compute left, upper corner (shift is every second, real row)
                    int x = c * tileSize.width + ((row % 2 != 0) ? tileSize.width / 2 : 0);
                    int y = r * tileSize.height;
                    g2d.drawImage(model.getTileAt(p), x, y, null);
                } else {
                    // just take a nearest tile image by projecting towards the nearest map tile in row and column direction

                    // compute left, upper corner (shift is every second, real row)
                    int x = c * tileSize.width + ((row % 2 != 0) ? tileSize.width / 2 : 0);
                    int y = r * tileSize.height;
                    row = Math.max(0, row);
                    row = Math.min(model.getNumberRows() - 1, row);
                    column = Math.max(0, column);
                    column = Math.min(model.getNumberColumns() - 1, column);
                    g2d.drawImage(model.getTileAt(new MapPosition(row, column)), x, y, null);
                }
            }
        }
        // TODO gray areas (outside of map) fill with nearest image, just paint something useful
        // TODO general transformation row, column to x, y

        // draw hoover rectangle
        if (!hoover.isOff()) {
            // calculate left upper position of that tile
            int r = hoover.row - offset.row;
            int c = hoover.column - offset.column;
            int x = c * tileSize.width + ((hoover.row % 2 != 0) ? tileSize.width / 2 : 0);
            int y = r * tileSize.height;
            g2d.setColor(Color.gray);
            // do not paint over tile borders (-1 in width and height)
            g2d.drawRect(x, y, tileSize.width - 1, tileSize.height - 1);
        }
    }

    /**
     *
     */
    private void notifyTileFocusChangedListeners() {
        for (MapTileListener l: tileListeners) {
            l.focusChanged(hoover);
        }
    }

    /**
     *
     * @param p
     */
    private void notifyTileClickedListeners(MapPosition p) {
        for (MapTileListener l: tileListeners) {
            l.tileClicked(p);
        }
    }

    /**
     *
     * @param l
     */
    public void addTileListener(MapTileListener l) {
        tileListeners.add(l);
    }

    /**
     *
     * @param x
     * @param y
     */
    @Override
    public void newMiniMapFocus(float x, float y) {
        // update offset row/column accordingly
        int row = (int) (y * model.getNumberRows() - size.height / 2 / tileSize.height);
        int column = (int) (x * model.getNumberColumns() - size.width / 2 / tileSize.width);
        row = Math.max(row, 0);
        column = Math.max(column, 0);
        MapPosition p = new MapPosition(row, column);
        if (!p.equals(offset)) {
            offset.setFrom(p);
            repaint();
        }
    }

    /**
     *
     * @param p
     */
    public void tileChanged(MapPosition p) {
        repaint();
    }

    /**
     *
     */
    public void mapChanged() {
        repaint();
    }
}
