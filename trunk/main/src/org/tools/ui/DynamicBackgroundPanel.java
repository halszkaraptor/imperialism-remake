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
package org.tools.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Support for dynamic change of background of a Panel with double buffering and timer.
 */
// TODO Is this used?
public abstract class DynamicBackgroundPanel {

    private static final int REPAINT_DELAY = 100;
    private JPanel panel;
    private Timer timer;
    private Dimension size;
    private Image image;

    /**
     * 
     */
    public DynamicBackgroundPanel() {
        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                if (image != null) {
                    g2d.drawImage(image, 0, 0, null);
                }
                super.paintComponent(g);
            }
        };
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.getSize(size);
                image = update();
            }
        };
        timer = new Timer(REPAINT_DELAY, listener);
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     *
     */
    public void startAnimating() {
        timer.start();
    }

    /**
     *
     */
    public void stopAnimating() {
        timer.stop();
    }

    /**
     *
     * @return
     */
    protected abstract Image update();
}
