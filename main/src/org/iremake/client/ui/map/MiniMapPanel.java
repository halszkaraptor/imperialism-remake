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
package org.iremake.client.ui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.iremake.client.ui.model.UIScenario;
import org.iremake.common.model.MapPosition;

/**
 * Mini map panel.
 */
// TODO different views (political, geographical)
public class MiniMapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Dimension size = new Dimension();
    private Rectangle focus = new Rectangle();
    private MiniMapFocusChangedListener focusChangedListener;
    private UIScenario model;
    private BufferedImage miniMap;

    /**
     *
     * @param model
     */
    public MiniMapPanel(UIScenario model) {

        this.model = model;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && focus.width > 0) {
                    int x0 = e.getX();
                    int y0 = e.getY();
                    x0 = Math.min(Math.max(x0, focus.width / 2), size.width - focus.width / 2);
                    y0 = Math.min(Math.max(y0, focus.height / 2), size.height - focus.height / 2);
                    if (focus.x != x0 || focus.y != y0) {
                        focus.x = x0;
                        focus.y = y0;
                        MiniMapPanel.this.repaint();
                        notifyFocusChangedListener();
                    }
                }
            }
        });

        setBorder(BorderFactory.createLineBorder(Color.black, 1));
        setOpaque(true); // by default is not
    }

    /**
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // draw background
        if (miniMap != null) {
            g2d.drawImage(miniMap, 0, 0, this);
        }

        // the main map has told us their size, we can draw the focus rectangle
        if (focus.width > 0) {
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.black);
            g2d.drawRect(focus.x - focus.width / 2 + 1, focus.y - focus.height / 2 + 1, focus.width - 2, focus.height - 2);
            g2d.setStroke(oldStroke);
        }
    }

    /**
     *
     */
    private void notifyFocusChangedListener() {
        if (focusChangedListener != null && focus.width > 0) {
            focusChangedListener.newMiniMapFocus((float) focus.x / size.width, (float) focus.y / size.height);
        }
    }

    /**
     *
     * @param l
     */
    public void setFocusChangedListener(MiniMapFocusChangedListener l) {
        focusChangedListener = l;
    }

    /**
     *
     * @param fractionRows
     * @param fractionColumns
     */
    public void mapChanged(float fractionRows, float fractionColumns) {


        size = getSize();
        size.height = size.width * model.getNumberRows() / model.getNumberColumns();
        setPreferredSize(size);

        focus.x = size.width / 2;
        focus.y = size.height / 2;

        focus.width = (int) (size.width * fractionColumns);
        focus.height = (int) (size.height * fractionRows);

        notifyFocusChangedListener();

        redrawMap(); // TODO wait for the actual componenResized event
        revalidate(); // this will call repaint
    }

    /**
     *
     */
    public void tileChanged() {
        redrawMap();
        repaint();
    }

    /**
     *
     */
    private void redrawMap() {
        miniMap = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < size.width; x++) {
            for (int y = 0; y < size.height; y++) {
                int column = model.getNumberColumns() * x / size.width; // rounding down
                int row = model.getNumberRows() * y / size.height;
                Color color = model.getTileColorAt(new MapPosition(row, column));
                miniMap.setRGB(x, y, color.getRGB());
            }
        }
    }
}
