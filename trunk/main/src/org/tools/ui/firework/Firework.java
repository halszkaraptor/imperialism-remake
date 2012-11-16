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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Firework animation
 */
// TODO finish animation, use DynamicBackgroundPanel and JLayeredPane
public class Firework {

    private static final long serialVersionUID = 1L;
    private int refreshTime = 100;
    private ActionListener listener;
    private Timer timer;
    private Rocket[] rockets;
    private JPanel panel;
    private double probAwake;

    public Firework() {

        panel = new JPanel();
        panel.setOpaque(false);

        // if the compnent is resized we need to tell all the rockets
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            }
        });

        // what to do during each update
        listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = panel.getGraphics();
                for (int i = 0; i < rockets.length; i++) {
                    Rocket rocket = rockets[i];

                    // if sleeping with some change wake up
                    if (rocket.isSleeping() && Math.random() < probAwake) {
                        rocket.start();
                    }

                    // paint if not sleeping
                    if (!rocket.isSleeping()) {
                        rocket.paint(g);
                    }
                }
            }
        };
    }

    public JComponent exposeComponent() {
        return panel;
    }

    public void setup(int numberMaxRockets, int numberAvgRockets, int numberAvgCycles) {
        if (numberMaxRockets < 0 || numberAvgRockets < 0 || numberAvgRockets > numberMaxRockets) {
            throw new IllegalArgumentException();
        }

        // calculate probability to awake
        if (numberMaxRockets == numberAvgRockets) {
            probAwake = 1;
        } else {
            probAwake = (double) numberAvgRockets / (double) (numberMaxRockets - numberAvgRockets) / (double) numberAvgCycles;
        }

        Dimension size = panel.getSize();

        rockets = new Rocket[numberMaxRockets];
        for (int i = 0; i < rockets.length; i++) {
            Rocket rocket = new Rocket(size);
            rocket.setup(10, 10);
            rockets[i] = rocket;
        }
    }

    public void animate() {
        timer = new Timer(refreshTime, listener);
        timer.start();
    }

    public void pause() {
    }

    public void stop() {
        timer.stop();
        timer = null;
    }
}