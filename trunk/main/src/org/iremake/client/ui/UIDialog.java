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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 *
 */
public class UIDialog {

    private Dimension minimumSize;
    private Point location;
    private WindowClosingListener closingListener;

    /**
     *
     */
    public UIDialog() {
        minimumSize = new Dimension(700, 600);
    }

    /**
     *
     * @param width
     * @param height
     */
    public void setMinimumSize(int width, int height) {
        minimumSize = new Dimension(width, height);
    }

    /**
     *
     * @param location
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     *
     * @param closingListener
     */
    public void setClosingListener(WindowClosingListener closingListener) {
        this.closingListener = closingListener;
    }

    /**
     *
     * @param content
     * @param title
     */
    public void start(JComponent content, String title) {

        // create dialog
        final JDialog dialog = new JDialog(FrameManager.getInstance().getFrame(), title, true);
        dialog.setResizable(true);

        // no default close, first ask the listener if there is any
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (closingListener == null || closingListener.closing() == true) {
                    dialog.dispose();
                }
            }
        });

        // set minimum size, add content and pack
        dialog.setMinimumSize(minimumSize);
        dialog.add(content);
        dialog.pack();

        // either set to predefined location or center on frame
        if (location != null) {
            dialog.setLocation(location);
        } else {
            Rectangle bounds = FrameManager.getInstance().getBounds();
            Dimension size = dialog.getSize();
            dialog.setLocation(bounds.x + bounds.width / 2 - size.width / 2, bounds.y + bounds.height / 2 - size.height / 2);
        }

        // start
        dialog.setVisible(true);
    }

}


/*
 *     public void startDialogCustom(JComponent content, String title, Dimension minimumSize) {

        dialog = new JDialog(frame, true);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setUndecorated(true);
        dialog.setResizable(true);

        dialog.getContentPane().setBackground(brown);

        JPanel top = new JPanel();
        top.setBackground(blue);

        JButton exitButton = Button.SmallExit.create();
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setBorder(null);
        exitButton.setBackground(blue);
        exitButton.addActionListener(exitDialogListener);

        JLabel titleLabel = new JLabel(title);

        top.setLayout(new MigLayout("fill, insets 2"));
        top.add(titleLabel, "alignx left");
        top.add(exitButton, "alignx right");

        dialog.setLayout(new MigLayout("wrap 1, fill, insets 0 5 5 5", "", "[][grow]"));
        dialog.add(top, "growx");
        dialog.add(content, String.format("grow, hmin %d, wmin %d", minimumSize.height, minimumSize.width));

        // instead of this we should set the bounds from options
        dialog.pack();

        ComponentMover mover = new ComponentMover();
        mover.setDestination(dialog);
        mover.registerComponent(top);

        ComponentResizer resizer = new ComponentResizer(new Insets(5, 5, 5, 5), new Dimension(10, 10));
        resizer.registerComponent(dialog);

        dialog.setVisible(true);
    }
 */