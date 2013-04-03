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
package org.tools.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * A Label that contains the actual time and updates it regularly. It also has a
 * semi-transparent background.
 */
// TODO round corners
// TODO opaque(true) does not paint below, typical artifacts when using transparency the wrong way
public class ClockLabel extends JLabel {

    private static final Logger LOG = Logger.getLogger(ClockLabel.class.getName());
    private static final long serialVersionUID = 1L;
    private SimpleDateFormat format;

    /**
     *
     */
    public ClockLabel() {

        // partly opaque
        setOpaque(true);

        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // set background color
        setBackground(new Color(0.8f, 0.8f, 0.8f, 0.2f));
        setHorizontalAlignment(SwingConstants.CENTER);

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
}
