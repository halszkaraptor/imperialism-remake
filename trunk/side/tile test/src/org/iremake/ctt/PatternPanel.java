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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 */
public class PatternPanel extends JPanel {

    private final static int LEN = 20;
    private final static int NUM = 6;
    private boolean[][] pattern = new boolean[NUM][NUM];
    private Random random = new Random();

    /**
     *
     */
    public PatternPanel() {
        int size = 1 + LEN * NUM;
        setPreferredSize(new Dimension(size, size));
        setBackground(Color.gray);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int i = (e.getX() - 1) / LEN;
                int j = (e.getY() - 1) / LEN;
                if (pattern[i][j] == true) {
                    pattern[i][j] = false;
                } else {
                    pattern[i][j] = true;
                }
                repaint();
            }
        });
    }

    /**
     *
     */
    public void clearPattern() {
        for (int i = 0; i < NUM; i++) {
            for (int j = 0; j < NUM; j++) {
                pattern[i][j] = false;
            }
        }
        repaint();
    }

    /**
     *
     */
    public void newRandomPattern() {
        for (int i = 0; i < NUM; i++) {
            for (int j = 0; j < NUM; j++) {
                pattern[i][j] = random.nextBoolean();
            }
        }
        repaint();
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;


        for (int i = 0; i < NUM; i++) {
            for (int j = 0; j < NUM; j++) {
                if (pattern[i][j] == true) {
                    g2d.setColor(Color.black);
                } else {
                    g2d.setColor(Color.white);
                }
                g2d.fillRect(i * LEN + 1, j * LEN + 1, LEN - 1, LEN - 1);
            }
        }
    }

    /**
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isSet(int i, int j) {
        return pattern[i][j];
    }
}
