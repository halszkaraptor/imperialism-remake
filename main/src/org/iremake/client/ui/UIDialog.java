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
    private JDialog dialog;
    private JComponent content;
    private final String title;

    /**
     *
     */
    public UIDialog(String title) {
        this.title = title;

        minimumSize = new Dimension(700, 600);
    }

    protected void setContent(JComponent content) {
        this.content = content;
    }

    /**
     *
     * @param width
     * @param height
     */
    protected void setMinimumSize(int width, int height) {
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
    protected void setClosingListener(WindowClosingListener closingListener) {
        this.closingListener = closingListener;
    }

    /**
     *
     */
    public void start() {

        if (content == null) {
            return;
        }

        // create dialog
        dialog = FrameManager.getInstance().getDialog(title);
        dialog.setResizable(true);

        // no default close, first ask the listener if there is any
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (closingListener == null || closingListener.closing() == true) {
                    dispose();
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
            FrameManager.getInstance().center(dialog);
        }

        // start
        dialog.setVisible(true);
    }

    protected void dispose() {
        dialog.dispose();
    }

    public static void make(JComponent content, String title) {
        UIDialog dialog = new UIDialog(title);
        dialog.setContent(content);
        dialog.start();
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