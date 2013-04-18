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
import javax.swing.JPanel;

/**
 *
 */
public class ViewPanel extends JPanel {
    
    private final static int NUM = 6;    
    
    public ViewPanel() {
        setBackground(Color.gray);
        setPreferredSize(new Dimension(NUM * 80 + 40, NUM * 80));
    }
}
