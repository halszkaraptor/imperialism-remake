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
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 */
public class MapPanelBuilder {

    public static JPanel makeOverviewMapPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.black, 1));

        return panel;
    }

    static JPanel makeMiniMapPanel() {
        JPanel panel = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(Color.gray);
                g2d.draw3DRect(10, 10, 50, 50, true);
            }
        };

        return panel;
    }
}
