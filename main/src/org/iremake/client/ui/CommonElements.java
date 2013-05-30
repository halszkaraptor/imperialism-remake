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
package org.iremake.client.ui;

import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 */
public class CommonElements {
    
    private static final Logger LOG = Logger.getLogger(CommonElements.class.getName());    
    
    private CommonElements() {
    }
    
    public static JLabel createLabel(String content) {
        JLabel label = new JLabel(content);
        return label;
    }
    
    private static final Border lineBorder = BorderFactory.createLineBorder(Color.darkGray, 1, true);
    
    public static Border createBorder(String title) {
        return BorderFactory.createTitledBorder(lineBorder, title);
    }
}
