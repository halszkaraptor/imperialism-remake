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
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.iremake.client.resources.TerrainLoader;

/**
 *
 */
public class MainMapPanel extends JPanel implements MiniMapFocusChangedListener {

    private static final long serialVersionUID = 1L;
    private Dimension size;
    private MainMapTileListener tileListener;
    private MapModel model;
    private Dimension tileSize;
    private int offsetRow = 0;
    private int offsetColumn = 0;
    private int hooveredRow = -1;
    private int hooveredColumn = -1;
    private int selectedRow = -1;
    private int selectedColumn = -1;

    public MainMapPanel(final MapModel model) {

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
                int row = y / tileSize.height;
                int shift = row % 2 != 0 ? tileSize.width / 2 : 0;
                int column = (x - shift) / tileSize.height;

                row = row + offsetRow;
                column = column + offsetColumn;
                
                // TODO mouse leaves the panel -> deselect

                if (row < 0 || row >= model.getNumberRows() || column < 0 || column >= model.getNumberColumns()) {
                    // outside of area, deselect                    
                    hooveredRow = -1;
                    hooveredColumn = -1;
                    notifyTileFocusChangedListeners();
                    repaint(); // TODO only the one tile
                } else {
                    // inside, check if over new tile
                    if (hooveredRow != row || hooveredColumn != column) {
                        hooveredRow = row;
                        hooveredColumn = column;
                        // hoovered tile changed
                        notifyTileFocusChangedListeners();
                        repaint(); // TODO only the two tiles
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = e.getX();
                    int y = e.getY();
                    int row = y / tileSize.height;
                    int shift = row % 2 != 0 ? tileSize.width / 2 : 0;
                    int column = (x - shift) / tileSize.height;

                    row = row + offsetRow;
                    column = column + offsetColumn;

                    if (row >= 0 && row < model.getNumberRows() && column >= 0 && column < model.getNumberColumns()) {
                        notifyTileClickedListeners(row, column);
                    }
                }
            }
        });

    }

    public void initWithSize(final MiniMapPanel miniMap) {

        size = getSize();

        // tell the minimap about our size
        float fractionRows = (float) size.height / tileSize.height / model.getNumberRows();
        float fractionColumns = (float) size.width / tileSize.width / model.getNumberColumns();
        miniMap.setMainMapViewSize(fractionRows, fractionColumns);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (size == null) {
            return;
        }

        // first just fill the background
        g2d.setColor(Color.gray);
        g2d.fillRect(0, 0, size.width, size.height);

        // draw the tiles
        int drawnRows = size.height / tileSize.height + 1;
        int drawnColumns = size.width / tileSize.width + 1;
        for (int row = 0; row < drawnRows; row++) {
            for (int column = 0; column < drawnColumns; column++) {
                int x = column * tileSize.width + ((row % 2 != 0) ? tileSize.width / 2 : 0);
                int y = row * tileSize.height;
                if (row + offsetRow < model.getNumberRows() && column + offsetColumn < model.getNumberColumns()) {
                    // lower right map border could want to print outside of map
                    g2d.drawImage(model.getTileAt(row + offsetRow, column + offsetColumn), x, y, null);
                }
            }
        }
        // TODO general transformation row, column to x, y

        // draw hoover rectangle
        if (hooveredRow != -1) {
            int row = hooveredRow - offsetRow;
            int column = hooveredColumn - offsetColumn;
            int x = column * tileSize.width + ((row % 2 != 0) ? tileSize.width / 2 : 0);
            int y = row * tileSize.height;
            g2d.setColor(Color.gray);
            g2d.drawRect(x, y, tileSize.width, tileSize.height);
        }
    }

    private void notifyTileFocusChangedListeners() {
        if (tileListener != null) {
            tileListener.focusChanged(hooveredRow, hooveredColumn);
        }
    }
    
    private void notifyTileClickedListeners(int row, int column) {
        if (tileListener != null) {
            tileListener.tileClicked(row, column);
        }
    }

    public void setTileListener(MainMapTileListener l) {
        tileListener = l;
    } // TODO at some point?

    @Override
    public void newMiniMapFocus(float x, float y) {
        // update offset row/column accordingly
        int row = (int) (y * model.getNumberRows() - size.height / 2 / tileSize.height);
        int column = (int) (x * model.getNumberColumns() - size.width / 2 / tileSize.width);
        row = Math.max(row, 0);
        column = Math.max(column, 0);
        if (offsetRow != row || offsetColumn != column) {
            offsetRow = row;
            offsetColumn = column;
            repaint();
        }
    }
}
