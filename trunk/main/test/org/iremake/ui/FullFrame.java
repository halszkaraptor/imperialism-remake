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
package org.iremake.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import org.iremake.common.ui.ComponentMover;
import org.iremake.common.ui.ComponentResizer;

/**
 *
 */
public class FullFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean fullscreen = false;

        final JFrame frame = new JFrame();
        
        GraphicsConfiguration gc = frame.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        if (!fullscreen) {
            Insets insets = frame.getToolkit().getScreenInsets(gc);
            bounds.x += insets.left;
            bounds.y += insets.right;
            bounds.width -= insets.left + insets.right;
            bounds.height -= insets.top + insets.bottom;
            
            frame.setResizable(true);
        } else {
            frame.setResizable(false);
        }
        
        frame.setBounds(bounds);

        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);        
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        frame.add(panel);
        
        if (!fullscreen) {
            
            Insets insets = new Insets(0, 10, 10, 10);
            Dimension snap = new Dimension(10, 10);
            ComponentResizer resizer = new ComponentResizer(insets, snap);
            resizer.registerComponent(frame);
            
            ComponentMover cm = new ComponentMover();
            cm.setDragInsets(insets);
            insets = new Insets(-100, -100, -100, -100);
            cm.setEdgeInsets(insets);
            cm.registerComponent(frame);
        }
        
        final Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(frame.getBounds());
            }
        });        
        
        
        frame.setVisible(true);
        timer.start();
    }
}
