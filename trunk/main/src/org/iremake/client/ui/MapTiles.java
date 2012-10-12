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

import java.awt.Dimension;
import java.awt.Image;
import org.iremake.client.utils.Resources;

/**
 *
 */
public class MapTiles {

    private int rows, columns;
    private Image[][] map; // first columns, second rows
    private Dimension tileSize;

    public MapTiles() {
        Image image = Resources.getAsImage("/data/game/artwork/graphics/terrain/terrain.plains.png");
        // TODO use ImageIO.read and BufferedImage, ImageIcon waits automatically, no ImageObserver needed
        tileSize = new Dimension(image.getWidth(null), image.getHeight(null));

        columns = 100;
        rows = 60;

        map = new Image[columns][rows];
        for (int c = 0; c < columns; c++) {
            for (int r = 0; r < rows; r++) {
                map[c][r] = image;
            }
        }
    }

    public Dimension getSize() {
        return new Dimension(columns, rows);
    }

    public Image getTileAt(int column, int row) {
        return map[column][row];
    }

    public Dimension getTileSIze() {
        return tileSize;
    }
}
