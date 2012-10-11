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
package org.iremake.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.iremake.client.utils.Resources;

/**
 *
 * @author Trilarion 2012
 */
public class MainMapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int GAP = 10;
    private Image image = Resources.getAsImage("/data/game/artwork/graphics/terrain/terrain.plains.png");

    private Dimension size = new Dimension();

    private int w = 80;
    private int h = 80;
    private int r = 60;
    private int c = 100;
    private int ra = -1;
    private int ca = -1;

    List<TileFocusChangedListener> tileListeners = new LinkedList<>();

    public MainMapPanel() {

        initComponents();
    }

    private void initComponents() {

        getSize(size);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                getSize(size);
            }
        });

        setOpaque(true);

        setBackground(Color.white);
        setBorder(new LineBorder(Color.black, 1));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x0 = e.getX();
                int y0 = e.getY();
                int r0 = (y0 - GAP)/ h;
                int t = r0 % 2 == 1 ? w / 2 : 0;
                int c0 = (x0 - GAP - t) / w;
                if (r0 != ra || c0 != ca) {
                    ra = r0;
                    ca = c0;
                    notifyTileFocusChangedListeners();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // staggered drawing
        for (int col = 0; col < c; col++) {
            for (int row = 0; row < r; row++) {
                int x = GAP + col * w + ((row % 2 == 1) ? w / 2 : 0);
                int y = GAP + row * h;
                g2d.drawImage(image, x, y, null);
            }
        }
    }

    private void notifyTileFocusChangedListeners() {
        for (TileFocusChangedListener l: tileListeners) {
            l.newTileFocus(0, 0);
        }
    }

    public void addTileFocusChangedListener(TileFocusChangedListener l) {
        tileListeners.add(l);
    }

    public void removeTileFocusChangedListener(TileFocusChangedListener l) {
        tileListeners.remove(l);
    }
}
