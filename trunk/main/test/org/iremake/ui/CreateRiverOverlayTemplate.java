/*
 * Copyright (C) 2013 Trilarion
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
package org.iremake.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Creates an template overlay image for creating river overlays.
 */
public class CreateRiverOverlayTemplate {

    private static final int TileSize = 80;
    private static final int MarkerSize = 5;
    private static final int Columns = 8;
    private static final int Rows = 5;
    private static final Color BgColor = new Color(0, 0, 0, 0);
    private static final Color BorderColor = Color.gray;
    private static final Color MarkerColor = Color.red;
    private static final Color ConnectionColor = Color.blue;
    private static final String OutFileName = "C:\\Users\\jkeller1\\Dropbox\\remake\\graphics\\Trilarion\\rivers\\template.png";

    private static final int[][] Connections =
        {{0, 0, 2, 0, 3, 2}, {0, 1, 2, 0, 2, 3}, {0, 2, 2, 0, 1, 3}, {0, 3, 2, 0, 0, 2}, {0, 4, 2, 0, 0, 1}, {0, 5, 3, 1, 2, 3},
         {0, 6, 3, 1, 1, 3}, {0, 7, 3, 1, 0, 2}, {1, 0, 3, 1, 0, 1}, {1, 1, 3, 1, 1, 0}, {1, 2, 3, 2, 1, 3}, {1, 3, 3, 2, 0, 2},
         {1, 4, 3, 2, 0, 1}, {1, 5, 3, 2, 1, 0}, {1, 6, 2, 3, 0, 2}, {1, 7, 2, 3, 0, 1}, {2, 0, 2, 3, 1, 0}, {2, 1, 1, 3, 0, 1},
         {2, 2, 1, 3, 1, 0}, {2, 3, 0, 2, 1, 0}, {2, 4, 2, 0, 2, 1}, {2, 5, 3, 1, 2, 1}, {2, 6, 3, 2, 2, 2}, {2, 7, 2, 3, 2, 2},
         {3, 0, 1, 3, 1, 2}, {3, 1, 0, 2, 1, 2}, {3, 2, 0, 1, 1, 1}, {3, 3, 1, 0, 1, 1}};
    private static final int[][] Mounds =
        {{3, 4, 2, 0}, {3, 5, 3, 1}, {3, 6, 3, 2}, {3, 7, 2, 3}, {4, 0, 1, 3}, {4, 1, 0, 2}, {4, 2, 0, 1}, {4, 3, 1, 0}};
    private static final int[][] Unused = {{4, 4}, {4, 5}, {4, 6}, {4, 7}};
    private static final int[] Location = {0, TileSize / 4, TileSize * 3 / 4, TileSize};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // creae image
        int width = Columns * TileSize;
        int height = Rows * TileSize;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // fill with transparent background
        Graphics2D g2 = image.createGraphics();
        g2.setComposite(AlphaComposite.SrcOver);
        // g2.setComposite(AlphaComposite.Src);

        g2.setColor(BgColor);
        g2.fillRect(0, 0, width, height);

        // horizontal and vertical gray lines
        g2.setColor(BorderColor);
        for (int i = 1; i < Columns; i++) {
            int x = i * TileSize;
            g2.drawLine(x, 0, x, height);
        }
        for (int i = 1; i < Rows; i++) {
            int y = i * TileSize;
            g2.drawLine(0, y, width, y);
        }

        // small red lines
        g2.setColor(MarkerColor);
        for (int c = 0; c < Columns; c++) {
            int x = c * TileSize;
            for (int r = 0; r < Rows; r++) {
                int y = r * TileSize;
                // horizontal
                int dy = TileSize * 1 / 4;
                g2.drawLine(x - MarkerSize, y + dy, x + MarkerSize, y + dy);
                dy = TileSize * 3 / 4;
                g2.drawLine(x - MarkerSize, y + dy, x + MarkerSize, y + dy);
                // vertical
                int dx = TileSize * 1 / 4;
                g2.drawLine(x + dx, y - MarkerSize, x + dx, y + MarkerSize);
                dx = TileSize * 3 / 4;
                g2.drawLine(x + dx, y - MarkerSize, x + dx, y + MarkerSize);
            }
        }

        // draw dotted blue lines
        g2.setColor(ConnectionColor);
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[]{3}, 0));
        for (int i = 0; i < Connections.length; i++) {
            int[] connection = Connections[i];
            int y1 = connection[0] * TileSize + Location[connection[3]];
            int x1 = connection[1] * TileSize + Location[connection[2]];
            int y2 = connection[0] * TileSize + Location[connection[5]];
            int x2 = connection[1] * TileSize + Location[connection[4]];
            g2.drawLine(x1, y1, x2, y2);
        }

        // draw mounds
        g2.setStroke(new BasicStroke(3));
        for (int i = 0; i < Mounds.length; i++) {
            int[] mound = Mounds[i];
            int y = mound[0] * TileSize + Location[mound[3]];
            int x = mound[1] * TileSize + Location[mound[2]];
            g2.drawLine(x, y, x, y);
        }

        // draw unused
        g2.setColor(BorderColor);
        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i < Unused.length; i++) {
            int x = Unused[i][1] * TileSize;
            int y = Unused[i][0] * TileSize;
            g2.drawLine(x, y, x + TileSize, y + TileSize);
            g2.drawLine(x + TileSize, y, x, y + TileSize);
        }

        g2.dispose();

        // write to file
        File outputfile = new File(OutFileName);
        ImageIO.write(image, "png", outputfile);
    }
}
