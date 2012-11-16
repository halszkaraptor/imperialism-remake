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
package org.tools.ui.firework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Single element of a Firework.
 */
public class Rocket {

    private boolean sleeping;
    private int currentStep, maxSteps = 20;
    private Color color;
    private double[] vx, vy;
    private int ox, oy;
    private Dimension space;

    public Rocket(Dimension space) {
        this.space = space;

        goSleeping();
    }

    public void setup(int numberFragments, double speed) {

        // origin position
        ox = (int) ((Math.random() * 0.7 + 0.2) * space.width);
        oy = (int) ((Math.random() * 0.7 + 0.2) * space.height);


        // arbitrary bright color
        double r = (Math.random() + 1) / 2;
        double g = (Math.random() + 1) / 2;
        double b = (Math.random() + 1) / 2;
        color = new Color((float) r, (float) g, (float) b);

        // directions of all fragments
        vx = new double[numberFragments];
        vy = new double[numberFragments];
        for (int i = 0; i < numberFragments; i++) {
            vx[i] = (Math.random() - 0.5) * speed;
            vy[i] = (Math.random() - 0.8) * speed;
        }
    }

    public boolean isSleeping() {
        return sleeping;
    }

    private void goSleeping() {
        currentStep = 0;
        sleeping = true;
    }

    public void start() {
        if (sleeping == true) {
            sleeping = false;
        }
    }

    public void paint(Graphics g) {
        if (!sleeping) {
            if (currentStep < maxSteps) {
                g.setColor(color);
                double t = currentStep / 20;

                int x, y;
                for (int i = 0; i < vx.length; i++) {
                    x = (int) (vx[i] * t);
                    y = (int) (vy[i] * t - 1 * t * t);
                    g.fillRect(ox + x, oy + y, 1, 1);
                }


                currentStep += 1;
            } else {
                // go and sleep
                goSleeping();
            }
        }

    }
}
