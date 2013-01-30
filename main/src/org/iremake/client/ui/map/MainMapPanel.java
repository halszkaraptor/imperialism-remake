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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.iremake.client.ui.model.UIScenario;
import org.iremake.common.model.map.MapPosition;
import org.iremake.common.model.map.TilesBorder;
import org.iremake.common.model.map.TilesTransition;

/**
 * The Main map display, currently used in the editor.
 */
// TODO drawing into border? (make border thicker, then you see it)
public class MainMapPanel extends JPanel implements MiniMapFocusChangedListener {

    private static final long serialVersionUID = 1L;
    /* we ask for the size quite often, so we store it */
    private Dimension size = new Dimension();
    /* all listeners */
    private List<MapTileListener> tileListeners = new LinkedList<>();
    /* client scenario */
    private UIScenario scenario;
    /* defines the part of the map that is displayed by defining the left, upper corner of the viewable part */
    private MapPosition offset = new MapPosition();
    /* the tile the mouse pointer is currently over */
    private MapPosition hoover = new MapPosition();

    private static class ScreenPosition {

        public final int x, y;
        public final MapPosition p;

        public ScreenPosition(int x, int y, MapPosition p) {
            this.x = x;
            this.y = y;
            this.p = p;
        }
    }

    /**
     * We feed the main map a scenario.
     *
     * @param scenario the scenario
     */
    public MainMapPanel(final UIScenario scenario) {

        this.scenario = scenario;

        setOpaque(true);
        hoover.setOff();

        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.black, 1));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                MapPosition p = getPositionFromXY(e.getX(), e.getY());

                // TODO the white pieces at the edge are handled how?
                if (scenario.containsPosition(p)) {
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

                    if (scenario.containsPosition(p)) {
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
     * @param x mouse x position
     * @param y mouse y position
     * @return map tile position
     */
    private MapPosition getPositionFromXY(int x, int y) {
        MapPosition p = new MapPosition();
        Dimension tileSize = scenario.getTileSize();
        p.row = y / tileSize.height + offset.row;
        int shift = p.row % 2 != 0 ? tileSize.width / 2 : 0;
        // -9..9/10 == 0, avoid negative divisions, because rounding is different there
        p.column = (x - shift + tileSize.width) / tileSize.width - 1 + offset.column;
        return p;
    }

    /**
     * Paints the whole main map. Tile terrain, resources, cities, units, ...
     * ..., borders (provincial, national), selection rectangles.
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // TODO insets/border? do we need to subtract?
        getSize(size);

        // first just fill the background
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, size.width, size.height);

        // List of ScreenPositions
        List<ScreenPosition> list = new ArrayList<>(100); // TODO meaningful capacity
        List<ScreenPosition> outside = new ArrayList<>(20);

        Dimension tileSize = scenario.getTileSize();
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
                // compute left, upper corner (shift is every second, real row)
                int x = c * tileSize.width + ((row % 2 != 0) ? tileSize.width / 2 : 0);
                int y = r * tileSize.height;
                // still on the map?
                if (row >= 0 && row < scenario.getNumberRows() && column >= 0 && column < scenario.getNumberColumns()) {
                    list.add(new ScreenPosition(x, y, p));
                } else {
                    row = Math.max(0, row);
                    row = Math.min(scenario.getNumberRows() - 1, row);
                    column = Math.max(0, column);
                    column = Math.min(scenario.getNumberColumns() - 1, column);
                    outside.add(new ScreenPosition(x, y, new MapPosition(row, column)));
                }
            }
        }

        // draw all terrain tiles
        for (ScreenPosition r : list) {
            g2d.drawImage(scenario.getTerrainTileAt(r.p), r.x, r.y, null);
        }

        // draw terrain tiles for outside
        for (ScreenPosition r : outside) {
            g2d.drawImage(scenario.getTerrainTileAt(r.p), r.x, r.y, null);
        }

        // draw resources
        for (ScreenPosition r : list) {
            if (scenario.isResourceVisibleAt(r.p)) {
                g2d.drawImage(scenario.getResourceOverlayAt(r.p), r.x, r.y, null);
            }
        }

        // draw tile borders
        for (ScreenPosition r : list) {
            // draw tile border
            g2d.setColor(Color.white);
            TilesBorder border = scenario.getBorder(r.p, TilesTransition.East);
            if (border == TilesBorder.Province) {
                // white stripe at the right side
                g2d.drawLine(r.x + tileSize.width - 1, r.y, r.x + tileSize.width - 1, r.y + tileSize.height);
            }
            border = scenario.getBorder(r.p, TilesTransition.SouthEast);
            if (border == TilesBorder.Province) {
                // white half strip on the lower, right side
                g2d.drawLine(r.x + tileSize.width / 2, r.y + tileSize.height - 1, r.x + tileSize.width - 1, r.y + tileSize.height - 1);
            }
            border = scenario.getBorder(r.p, TilesTransition.SouthWest);
            if (border == TilesBorder.Province) {
                // white half stripe on the lower, left side
                g2d.drawLine(r.x, r.y + tileSize.height - 1, r.x + tileSize.width / 2, r.y + tileSize.height - 1);
            }
            // TODO draw border nation
        }

        // draw railroad
        for (ScreenPosition r : list) {
            // TODO really have to draw after all other tiles are drawn, otherwise parts get overdrawn again
            g2d.setColor(Color.black);
            int xc = r.x + tileSize.width / 2;
            int yc = r.y + tileSize.height / 2;
            if (scenario.hasRailRoad(r.p, TilesTransition.East)) {
                g2d.drawLine(xc, yc, xc + tileSize.width, yc);
            }
            if (scenario.hasRailRoad(r.p, TilesTransition.SouthEast)) {
                g2d.drawLine(xc, yc, xc + tileSize.width / 2, yc + tileSize.height);
            }
            if (scenario.hasRailRoad(r.p, TilesTransition.SouthWest)) {
                g2d.drawLine(xc, yc, xc - tileSize.width / 2, yc + tileSize.height);
            }
        }

        // draw cities
        for (ScreenPosition r : list) {
            // draw city
            String name = scenario.getTownAt(r.p);
            name = "Test";
            if (name != null) {
                // TODO draw on half translucent rounded rectangle below city
                Font font = UIManager.getFont("Label.font");
                Rectangle2D bounds = font.getStringBounds(name, g2d.getFontRenderContext());
                int x = r.x + tileSize.width / 2 - (int)(bounds.getWidth() / 2);
                int y = r.y + tileSize.height;
                int gap = 5;
                g2d.setColor(new Color(128, 128, 128, 64));
                g2d.fillRect(x-gap, y-gap, (int)(bounds.getWidth() + 2 * gap), (int)(bounds.getHeight() + 2 * gap));
                g2d.setColor(Color.black);
                g2d.drawString(name, x, y + (int)(-bounds.getY())); // TODO antialiased
            }
        }

        // draw units
        // TODO draw units

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
     * the focus of the tile has changed
     */
    private void notifyTileFocusChangedListeners() {
        for (MapTileListener l : tileListeners) {
            l.focusChanged(hoover);
        }
    }

    /**
     * a tile has been clicked
     *
     * @param p the position of the tile
     */
    private void notifyTileClickedListeners(MapPosition p) {
        for (MapTileListener l : tileListeners) {
            l.tileClicked(p);
        }
    }

    /**
     * add another listener
     *
     * @param l the listener
     */
    public void addTileListener(MapTileListener l) {
        tileListeners.add(l);
    }

    /**
     * the mini map changed its focus (given in normalized coordinates)
     *
     * @param x normalized [0,1] x coordinates of the new focus of the mini map
     * @param y normalized [0,1] y coordinates of the new focus of the mini map
     */
    @Override
    public void miniMapFocusChanged(float x, float y) {
        // update offset row/column accordingly
        Dimension tileSize = scenario.getTileSize();
        int row = (int) (y * scenario.getNumberRows() - size.height / 2 / tileSize.height);
        int column = (int) (x * scenario.getNumberColumns() - size.width / 2 / tileSize.width);
        row = Math.max(row, 0);
        column = Math.max(column, 0);
        MapPosition p = new MapPosition(row, column);
        if (!p.equals(offset)) {
            offset.setFrom(p);
            repaint();
        }
    }

    /**
     * somebody tells us that a specific tile has been changed
     *
     * @param p position of the tile
     */
    public void tileChanged(MapPosition p) {
        // TODO for now we just repaint the whole map, this could be improved
        repaint();
    }

    /**
     * somebody tells us that the whole map has changed. typically this is the
     * case, when a new scenario is loaded. we repaint everything.
     */
    public void mapChanged() {
        repaint();
    }
}
