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
package org.iremake.client.ui.map;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.iremake.common.model.map.TilesTransition;
import org.tools.ui.utils.GraphicsUtils;
import org.tools.utils.Pair;

/**
 *
 */
public class EnhancedTile {

    private static class Part {

        public int x, y;
        public Image inner, outer;

        public Part(int x, int y, Image inner, Image outer) {
            this.x = x;
            this.y = y;
            this.inner = inner;
            this.outer = outer;
        }
    }
    
    private Map<TilesTransition, Part> library = new HashMap<>();    
    private Dimension size;
    private Image outer;

    /**
     *
     * @param inner
     * @param outer
     */
    public EnhancedTile(Image inner, Image outer) {
        // check that have same size
        this.outer = outer;
        size = new Dimension(inner.getWidth(null), inner.getHeight(null));
        if (size.width != outer.getWidth(null) || size.height != outer.getHeight(null)) {
            throw new RuntimeException("Dimensions of inner and outer image must be equal!");
        }
        Dimension half = new Dimension(size.width / 2, size.height / 2);

        Rectangle area;
        BufferedImage in, out;
        int[] xi, yi;

        // NorthWest (upper, left quarter)
        area = new Rectangle(0, 0, half.width, half.height);
        xi = new int[]{0, 0, half.width - 1};
        yi = new int[]{1, half.height, half.height};
        in = cutout(inner, area, xi, yi);
        out = cutout(outer, area, xi, yi);
        library.put(TilesTransition.NorthWest, new Part(0, 0, in, out));

        // NorthEast (upper, right quarter)
        area = new Rectangle(half.width, 0, half.width, half.height);
        xi = new int[]{half.width, half.width, 1};
        yi = new int[]{1, half.height, half.height};
        in = cutout(inner, area, xi, yi);
        out = cutout(outer, area, xi, yi);
        library.put(TilesTransition.NorthEast, new Part(half.width, 0, in, out));

        // East (right half)
        area = new Rectangle(half.width, 0, half.width, size.height);
        xi = new int[]{0, half.width, 0, half.width, 0};
        yi = new int[]{0, 0, half.height, size.height, size.height};
        in = cutout(inner, area, xi, yi);
        out = cutout(outer, area, xi, yi);
        library.put(TilesTransition.East, new Part(half.width, 0, in, out));

        // SouthEast (lower, right quarter)
        area = new Rectangle(half.width, half.height, half.width, half.height);
        xi = new int[]{1, half.width, half.width};
        yi = new int[]{0, 0, half.height - 1};
        in = cutout(inner, area, xi, yi);
        out = cutout(outer, area, xi, yi);
        library.put(TilesTransition.SouthEast, new Part(half.width, half.height, in, out));

        // SouthWest (lower, left quarter)
        area = new Rectangle(0, half.height, half.width, half.height);
        xi = new int[]{0, half.width - 1, 0};
        yi = new int[]{0, 0, half.height - 1};
        in = cutout(inner, area, xi, yi);
        out = cutout(outer, area, xi, yi);
        library.put(TilesTransition.SouthWest, new Part(0, half.height, in, out));

        // West (left half)
        area = new Rectangle(0, 0, half.width, size.height);
        xi = new int[]{half.width, 0, half.width, 0, half.width};
        yi = new int[]{0, 0, half.height, size.height, size.height};
        in = cutout(inner, area, xi, yi);
        out = cutout(outer, area, xi, yi);
        library.put(TilesTransition.West, new Part(0, 0, in, out));
    }
    
    public Dimension getSize() {
        return size;
    }
    
    /**
     * 
     * @param original
     * @param area
     * @param xi
     * @param yi
     * @return 
     */
    private BufferedImage cutout(Image original, Rectangle area, int[] xi, int[] yi) {
        BufferedImage result = GraphicsUtils.subBufferedImage(original, area);
        GraphicsUtils.transparentFillPolygon(result, xi, yi);
        return result;
    }

    public void paint(Graphics2D g2d, int x0, int y0, List<Pair<TilesTransition, Boolean>> list) {
        for (Pair<TilesTransition, Boolean> item : list) {
            Part part = library.get(item.getA());
            Image image = item.getB() ? part.outer : part.inner;
            g2d.drawImage(image, x0 + part.x, y0 + part.y, null);
        }
    }
    
    public Icon getAsIcon() {
        return new ImageIcon(outer);
    }    
    
    public Image getOuter() {
        return outer;
    }    
}
