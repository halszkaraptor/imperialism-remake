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
package org.iremake.ctt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.tools.ui.utils.GraphicsUtils;
import org.tools.ui.utils.LookAndFeel;

/**
 *
 */
public class EnhancedTileTest {

    private static final int TILE_SIZE = 80;

    public EnhancedTileTest() throws IOException {

        String in = "C:\\Users\\Jan\\Dropbox\\remake\\graphics\\Veneteaou\\Forest.bmp";
        final Image inner = GraphicsUtils.scaleBufferedImage(ImageIO.read(new File(in)), new Dimension(TILE_SIZE, TILE_SIZE));

        String out = "C:\\Users\\Jan\\Dropbox\\remake\\graphics\\Veneteaou\\Hills.bmp";
        final Image outer = GraphicsUtils.scaleBufferedImage(ImageIO.read(new File(out)), new Dimension(TILE_SIZE, TILE_SIZE));

        final EnhancedTile tile = new EnhancedTile(inner, outer);

        // construction of JFrame
        JFrame frame = new JFrame(EnhancedTileTest.class.getName());
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(Color.red);

                // NorthWest
                g2d.fillRect(10, 10, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 10, 10, TilesTransition.NorthWest, false);
                g2d.fillRect(10, 120, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 10, 120, TilesTransition.NorthWest, true);

                // NorthEast
                g2d.fillRect(100, 10, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 100, 10, TilesTransition.NorthEast, false);
                g2d.fillRect(100, 120, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 100, 120, TilesTransition.NorthEast, true);

                // East
                g2d.fillRect(190, 10, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 190, 10, TilesTransition.East, false);
                g2d.fillRect(190, 120, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 190, 120, TilesTransition.East, true);

                // SouthEast
                g2d.fillRect(280, 10, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 280, 10, TilesTransition.SouthEast, false);
                g2d.fillRect(280, 120, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 280, 120, TilesTransition.SouthEast, true);

                // SouthWest
                g2d.fillRect(370, 10, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 370, 10, TilesTransition.SouthWest, false);
                g2d.fillRect(370, 120, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 370, 120, TilesTransition.SouthWest, true);

                // West
                g2d.fillRect(460, 10, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 460, 10, TilesTransition.West, false);
                g2d.fillRect(460, 120, TILE_SIZE, TILE_SIZE);
                tile.paint(g2d, 460, 120, TilesTransition.West, true);

                // names
                g2d.setColor(Color.black);
                g2d.drawString(TilesTransition.NorthWest.toString(), 10, 110);
                g2d.drawString(TilesTransition.NorthEast.toString(), 100, 110);
                g2d.drawString(TilesTransition.East.toString(), 190, 110);
                g2d.drawString(TilesTransition.SouthEast.toString(), 280, 110);
                g2d.drawString(TilesTransition.SouthWest.toString(), 370, 110);
                g2d.drawString(TilesTransition.West.toString(), 460, 110);

                g2d.setColor(Color.red);

                // just the inner tile
                g2d.fillRect(10, 210, TILE_SIZE, TILE_SIZE);
                g2d.drawImage(inner, 10, 210, null);
                g2d.drawString("original inner", 10, 310);

                // assemble inner from parts
                g2d.fillRect(100, 210, TILE_SIZE, TILE_SIZE);
                for (TilesTransition transition : TilesTransition.values()) {
                    tile.paint(g2d, 100, 210, transition, false);
                }
                g2d.drawString("assembled", 100, 310);

                // just the inner tile
                g2d.fillRect(280, 210, TILE_SIZE, TILE_SIZE);
                g2d.drawImage(outer, 280, 210, null);
                g2d.drawString("original outer", 280, 310);

                // assemble inner from parts
                g2d.fillRect(370, 210, TILE_SIZE, TILE_SIZE);
                for (TilesTransition transition : TilesTransition.values()) {
                    tile.paint(g2d, 370, 210, transition, true);
                }
                g2d.drawString("assembled", 370, 310);

                // two times random
                Random random = new Random();
                g2d.fillRect(190, 210, TILE_SIZE, TILE_SIZE);
                for (TilesTransition transition : TilesTransition.values()) {
                    tile.paint(g2d, 190, 210, transition, random.nextBoolean());
                }
                g2d.drawString("random", 190, 310);

                g2d.fillRect(460, 210, TILE_SIZE, TILE_SIZE);
                for (TilesTransition transition : TilesTransition.values()) {
                    tile.paint(g2d, 460, 210, transition, random.nextBoolean());
                }
                g2d.drawString("random", 460, 310);

            }
        };
        panel.setPreferredSize(new Dimension(600, 400));
        frame.add(panel);


        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LookAndFeel.setSystemLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new EnhancedTileTest();
                } catch (IOException ex) {
                    Logger.getLogger(EnhancedTileTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
