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
package org.tools.ui.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * A Label that contains the actual time and updates it regularly. It also has a
 * semi-transparent background.
 */
// TODO round corners
public class ClockLabel extends JLabel {

    private static final long serialVersionUID = 1L;
    private SimpleDateFormat format;

    /**
     * 
     */
    public ClockLabel() {

        // not opaque, ancestor must paint itself
        setOpaque(false);

        setPreferredSize(new Dimension(60, 30));
        // TODO non hard-coded size


        setHorizontalAlignment(SwingConstants.CENTER);

        // set background color
        setBackground(new Color(255, 255, 255, 80));

        // set time format
        format = new SimpleDateFormat("HH:mm");

        // update
        update();

        // set timer for repeated updates
        Timer timer = new Timer(20000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        timer.start();
    }

    /**
     *
     */
    private void update() {
        // new Date() implicitely calls System.currentTimeMillis()
        setText(format.format(new Date()));
    }

    /**
     * 
     * @param g 
     */
    // TODO move this to common elements
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
