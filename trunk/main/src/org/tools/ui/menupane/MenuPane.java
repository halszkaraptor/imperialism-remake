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
package org.tools.ui.menupane;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * An overlay with some buttons that can invoke some actions. Used in graphical user interfaces, especially in applets.
 */
public class MenuPane {

    private JPanel panel;
    private ComponentListener listener;

    public MenuPane(JPanel outer) {
        this.panel = outer;
    }

    public void attach(final JLayeredPane pane) {

        // if we are already attached, just quit
        if (listener != null) {
            return;
        }
        panel.setSize(pane.getSize());
        pane.add(panel, JLayeredPane.PALETTE_LAYER);
        listener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.setSize(pane.getSize());
            }
        };
        pane.addComponentListener(listener);
        pane.repaint();
    }

    public void detach(JLayeredPane pane) {
        pane.removeComponentListener(listener);
        listener = null;
        pane.remove(panel);
    }
}
