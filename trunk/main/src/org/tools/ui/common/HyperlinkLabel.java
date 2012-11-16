/*
 * Copyright (C) 2010-11 Trilarion
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
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 * Hyperlink functionality on Text Labels (derived from JLabel).
 */
public class HyperlinkLabel extends JLabel {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(HyperlinkLabel.class.getName());
    /**
     * Color of the Label text and the underline
     */
    private static final Color linkColor = Color.blue;
    /**
     * Only underline when mouse is over the label
     */
    private boolean underline;
    /**
     * Link URI as String
     */
    private String link;
    private final MouseListener mouseListener = new MouseAdapter() {
        /**
         * If the mouse is clicked we try to start a browser with this link
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (link == null) {
                link = HyperlinkLabel.this.getText();
            }
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(URI.create(link));
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            } else {
                LOG.log(Level.INFO, "Desktop not supported, cannot start Browser in JHyperlinkLabel");
            }
        }

        /**
         * The label shall be underlined as long as the mouse is over it
         */
        @Override
        public void mouseEntered(MouseEvent me) {
            underline = true;
            repaint();
        }

        /**
         * Mouse is outside again, do not underline
         */
        @Override
        public void mouseExited(MouseEvent me) {
            underline = false;
            repaint();
        }
    };

    /**
     * Create a label without any text.
     */
    public HyperlinkLabel() {
        this("");
    }

    /**
     * Constructs the label and sets text color and mouse listener.
     *
     * @param text The text of the label.
     */
    public HyperlinkLabel(String text) {
        super(text);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setForeground(linkColor);
        addMouseListener(mouseListener);
    }

    /**
     * Sets the content of the link, eg. http://www.google.com If this is not
     * set the link will be assumed to be the labels text.
     *
     * @param link The link as a String
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Paint the component, paint the line under the text depending on if we
     * want it or not.
     *
     * @param g Graphics context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (underline) {
            g.setColor(getForeground());
            Insets insets = getInsets();
            int left = insets.left;
            if (getIcon() != null) {
                left += getIcon().getIconWidth() + getIconTextGap();
            }
            g.drawLine(left, getHeight() - 1 - insets.bottom, (int) getPreferredSize().getWidth() - insets.right, getHeight() - 1 - insets.bottom);
        }
    }
}