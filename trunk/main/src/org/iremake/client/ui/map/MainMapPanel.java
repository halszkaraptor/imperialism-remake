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
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.iremake.client.resources.TerrainLoader;

/**
 *
 */
public class MainMapPanel extends JPanel implements MiniMapFocusChangedListener {

    private static final long serialVersionUID = 1L;
    private Dimension size = new Dimension();
    private MainMapTileListener tileListener;
    private ScenarioModel model;
    private Dimension tileSize;
    private int offsetRow = 0;
    private int offsetColumn = 0;
    private int hooveredRow = -1;
    private int hooveredColumn = -1;
    private int selectedRow = -1;
    private int selectedColumn = -1;

    public MainMapPanel(final ScenarioModel model) {

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
                int shift = (row + offsetRow) % 2 != 0 ? tileSize.width / 2 : 0;
                int column = (x - shift) / tileSize.height;

                row += offsetRow;
                column += offsetColumn;

                // TODO the white pieces at the edge are handled how?
                if (row < 0 || row >= model.getNumberRows() || column < 0 || column >= model.getNumberColumns()) {
                    // outside of area, deselect
                    hooveredRow = -1;
                    hooveredColumn = -1;
                    repaint(); // TODO only the one tile
                    notifyTileFocusChangedListeners();
                } else {
                    // inside, check if over new tile
                    if (hooveredRow != row || hooveredColumn != column) {
                        hooveredRow = row;
                        hooveredColumn = column;
                        // hoovered tile changed
                        repaint(); // TODO only the two tiles
                        notifyTileFocusChangedListeners();
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

                    row += offsetRow;
                    column += offsetColumn;

                    if (row >= 0 && row < model.getNumberRows() && column >= 0 && column < model.getNumberColumns()) {
                        notifyTileClickedListeners(row, column);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hooveredRow = -1;
                hooveredColumn = -1;
                repaint();
                notifyTileFocusChangedListeners();
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        getSize(size);

        // first just fill the background
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, size.width, size.height);

        // draw the tiles
        int drawnRows = size.height / tileSize.height + 1;
        int drawnColumns = size.width / tileSize.width + 1;
        for (int r = 0; r < drawnRows; r++) {
            for (int c = 0; c < drawnColumns; c++) {
                int row = r + offsetRow;
                int column = c + offsetColumn;
                if (row < model.getNumberRows() && column < model.getNumberColumns()) {
                    int x = c * tileSize.width + ((row % 2 != 0) ? tileSize.width / 2 : 0);
                    int y = r * tileSize.height;                    
                    // lower right map border could want to print outside of map
                    g2d.drawImage(model.getTileAt(row, column), x, y, null);
                }
            }
        }
        // TODO general transformation row, column to x, y

        // draw hoover rectangle
        if (hooveredRow != -1 && hooveredColumn != -1) {
            int row = hooveredRow - offsetRow;
            int column = hooveredColumn - offsetColumn;
            int x = column * tileSize.width + ((row % 2 != 0) ? tileSize.width / 2 : 0);
            int y = row * tileSize.height;
            g2d.setColor(Color.gray);
            g2d.drawRect(x, y, tileSize.width - 1, tileSize.height - 1);
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
    }

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

    public void tileChanged(int row, int column) {
        repaint();
    }

    public void mapChanged() {
        repaint();
    }
}
