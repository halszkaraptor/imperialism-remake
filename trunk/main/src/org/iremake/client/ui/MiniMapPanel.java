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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 */
public class MiniMapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private int x0;
    private int y0;
    private final int w = 50;
    private final int h = 30;
    private Dimension size = new Dimension();

    public MiniMapPanel() {
        initComponents();
    }

    private void initComponents() {

        setBorder(new LineBorder(Color.black, 1));

        getSize(size);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                getSize(size);
            }
        });

        x0 = 10;
        y0 = 10;

        setPreferredSize(new Dimension(200,120));
        setOpaque(true); // by default is not

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = e.getX() - w / 2;
                    int y = e.getY() - h / 2;
                    x = Math.min(Math.max(x, 0), size.width - w);
                    y = Math.min(Math.max(y, 0), size.height - h);
                    if (x != x0 || y != y0) {
                        x0 = x;
                        y0 = y;
                        MiniMapPanel.this.repaint();
                    }
                }
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // draw background
        g2d.setColor(Color.gray);
        g2d.fillRect(0, 0, size.width, size.height);

        g2d.setColor(Color.gray);
        g2d.draw3DRect(x0, y0, w, h, true);
    }
}
