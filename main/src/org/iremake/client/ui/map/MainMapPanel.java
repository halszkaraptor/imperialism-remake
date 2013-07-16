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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.iremake.client.ui.model.UIScenario;
import org.iremake.common.model.map.MapItem;
import org.iremake.common.model.map.MapPosition;
import org.iremake.common.model.map.TilesBorder;
import org.iremake.common.model.map.TilesTransition;
import org.tools.utils.Pair;

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

    private void drawProvinceTownName(Graphics2D g2d, String name, int x, int y) {
        Font font = UIManager.getFont("Label.font");
        Rectangle bounds = font.getStringBounds(name, g2d.getFontRenderContext()).getBounds();

        Insets insets = new Insets(3, 6, 3, 6);

        // draw rectangle
        g2d.setColor(new Color(164, 164, 164, 240));
        // g2d.fillRoundRect(x - insets.left - bounds.width / 2, y, bounds.width + insets.left + insets.right, bounds.height + insets.top + insets.bottom, 5, 5);
        g2d.fill3DRect(x - insets.left - bounds.width / 2, y, bounds.width + insets.left + insets.right, bounds.height + insets.top + insets.bottom, true);

        // draw string
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.black);
        g2d.drawString(name, x - bounds.x - bounds.width / 2, y - bounds.y + insets.top); // TODO antialiased?
    }

    private void drawImageCentered(Graphics2D g2d, Image image, int x, int y) {
        g2d.drawImage(image, x - image.getWidth(null) / 2, y - image.getHeight(null) / 2, null);
    }

    private void drawBorder(Graphics2D g2d, TilesBorder border, int x1, int y1, int x2, int y2) {
        switch (border) {
            case Province:
                g2d.setColor(Color.white);
                g2d.setStroke(new BasicStroke(1));
                break;
            case Nation:
                g2d.setColor(Color.black);
                g2d.setStroke(new BasicStroke(2));
                break;
            default:
                throw new IllegalArgumentException("");
        }
        g2d.drawLine(x1, y1, x2, y2);
    }

    private static class ScreenPosition {

        public final int x, y;
        public final MapPosition p;

        ScreenPosition(int x, int y, MapPosition p) {
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
        List<ScreenPosition> fulldrawn = new ArrayList<>(100); // TODO meaningful capacity
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
                    fulldrawn.add(new ScreenPosition(x, y, p));
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
        for (ScreenPosition r : fulldrawn) {
            List<Pair<TilesTransition, Boolean>> list = new ArrayList<>(6);
            for (TilesTransition transition : TilesTransition.values()) {
                list.add(new Pair<TilesTransition, Boolean>(transition, scenario.isSameTerrain(r.p, transition)));
            }
            scenario.getTerrainTileAt(r.p).paint(g2d, r.x, r.y, list);
            // drawImageCentered(g2d, scenario.getTerrainTileAt(r.p), r.x + tileSize.width / 2, r.y + tileSize.height / 2);
        }

        // draw terrain tiles for outside areas
        for (ScreenPosition r : outside) {
            drawImageCentered(g2d, scenario.getTerrainTileAt(r.p).getOuter(), r.x + tileSize.width / 2, r.y + tileSize.height / 2);
        }

        // draw resources
        for (ScreenPosition r : fulldrawn) {
            if (scenario.isResourceVisibleAt(r.p)) {
                List<Pair<TilesTransition, Boolean>> list = new ArrayList<>(6);
                for (TilesTransition transition : TilesTransition.values()) {
                    list.add(new Pair<TilesTransition, Boolean>(transition, scenario.isSameResource(r.p, transition)));
                }
                scenario.getResourceOverlayAt(r.p).paint(g2d, r.x, r.y, list);
                // drawImageCentered(g2d, scenario.getResourceOverlayAt(r.p), r.x + tileSize.width / 2, r.y + tileSize.height / 2);
            }
        }

        // draw rivers
        for (ScreenPosition r : fulldrawn) {
            Image overlay = scenario.getRiverOverlayAt(r.p);
            if (overlay != null) {
                drawImageCentered(g2d, overlay, r.x + tileSize.width / 2, r.y + tileSize.height / 2);
            }
        }


        // draw tile borders, first province borders
        for (ScreenPosition r : fulldrawn) {
            // draw tile border
            g2d.setColor(Color.white);
            TilesBorder border = scenario.getBorder(r.p, TilesTransition.East);
            if (border == TilesBorder.Province) {
                // right border
                drawBorder(g2d, border, r.x + tileSize.width, r.y, r.x + tileSize.width, r.y + tileSize.height);
            }
            border = scenario.getBorder(r.p, TilesTransition.SouthEast);
            if (border == TilesBorder.Province) {
                // lower right side
                drawBorder(g2d, border, r.x + tileSize.width / 2, r.y + tileSize.height, r.x + tileSize.width, r.y + tileSize.height);
            }
            border = scenario.getBorder(r.p, TilesTransition.SouthWest);
            if (border == TilesBorder.Province) {
                // lower right side
                drawBorder(g2d, border, r.x, r.y + tileSize.height, r.x + tileSize.width / 2, r.y + tileSize.height);
            }
        }

        // draw tile borders, then nation borders
        for (ScreenPosition r : fulldrawn) {
            // draw tile border
            g2d.setColor(Color.white);
            TilesBorder border = scenario.getBorder(r.p, TilesTransition.East);
            if (border == TilesBorder.Nation) {
                // right border
                drawBorder(g2d, border, r.x + tileSize.width, r.y, r.x + tileSize.width, r.y + tileSize.height);
            }
            border = scenario.getBorder(r.p, TilesTransition.SouthEast);
            if (border == TilesBorder.Nation) {
                // lower right side
                drawBorder(g2d, border, r.x + tileSize.width / 2, r.y + tileSize.height, r.x + tileSize.width, r.y + tileSize.height);
            }
            border = scenario.getBorder(r.p, TilesTransition.SouthWest);
            if (border == TilesBorder.Nation) {
                // lower right side
                drawBorder(g2d, border, r.x, r.y + tileSize.height, r.x + tileSize.width / 2, r.y + tileSize.height);
            }
        }

        // draw railroad
        for (ScreenPosition r : fulldrawn) {
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
        for (ScreenPosition r : fulldrawn) {
            // draw city
            String name = scenario.getTownAt(r.p);
            // name = "Test";
            if (name != null) {
                drawImageCentered(g2d, scenario.getTileGraphicsRepository().getMiscOverlay("city"), r.x + tileSize.width / 2, r.y + tileSize.height / 2);
                drawProvinceTownName(g2d, name, r.x + tileSize.width / 2, r.y + tileSize.height - 10);
            }
        }

        // draw units
        for (MapItem unit : scenario.getAllUnits()) {
            MapPosition p = unit.getPosition();

            int r = p.row - offset.row;
            int c = p.column - offset.column;
            // compute left, upper corner (shift is every second, real row)
            int x = c * tileSize.width + ((p.row % 2 != 0) ? tileSize.width / 2 : 0);
            int y = r * tileSize.height;

            drawImageCentered(g2d, scenario.getTileGraphicsRepository().getUnitOverlay("infantry", "stand"), x + tileSize.width / 2, y + tileSize.height / 2);
        }

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
    private static final Logger LOG = Logger.getLogger(MainMapPanel.class.getName());
}
