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
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *
 */
public class CommonElements {

    private static final Border lineBorder = BorderFactory.createLineBorder(Color.darkGray, 1, true);

    private CommonElements() {
    }

    public static JLabel createLabel(String content) {
        JLabel label = new JLabel(content);
        label.setHorizontalAlignment(SwingConstants.LEADING);
        return label;
    }

    public static Border createBorder(String title) {
        return BorderFactory.createTitledBorder(lineBorder, title);
    }

    public static JPanel createPanel(String borderTitle) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(createBorder(borderTitle));
        return panel;
    }

    public static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setOpaque(false);
        return textField;
    }

    public static JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setOpaque(false);
        return textArea;
    }
}
