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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 * Base class for having a single modal dialog on the main frame. Allows
 * adjusting of the position, minimum size, closing behavior and content/title
 * without exposing anything beyond listeners and a content panel.
 *
 * Before this class is garbage collected, the dialog should be closed, most
 * probably by a call to close() with either no closing listener or a closing
 * listener returning true.
 */
public class UIDialog {

    private Dimension minimumSize;
    private Point location;
    private WindowClosingListener closingListener;
    private JDialog dialog;
    private JComponent content;
    private final String title;

    /**
     * A title must be given. Standard minimum size is 700x600.
     *
     * @param title the title
     */
    public UIDialog(String title) {
        this.title = title;
        minimumSize = new Dimension(700, 600);
    }

    /**
     * Stores the content component. Preferably call at the end of the
     * constructor of classes extending this class.
     *
     * @param content a component
     */
    protected void setContent(JComponent content) {
        this.content = content;
    }

    /**
     * Sets a new minimum size.
     *
     * @param width minimum width
     * @param height minimum height
     */
    protected void setMinimumSize(int width, int height) {
        minimumSize = new Dimension(width, height);
    }

    /**
     * Sets a specific location. The location is relative to the parent frame.
     * Otherwise the dialog will be centered.
     *
     * @param location the location
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Sets a new closing listener. This listener is invoked when the user
     * clicks on the cross of the window decoration or otherwise. A close action
     * might be invoked if the closing listener wants it.
     *
     * @param closingListener new closing listener
     */
    public void setClosingListener(WindowClosingListener closingListener) {
        this.closingListener = closingListener;
    }

    /**
     * Constructs the dialog and sets it visible. Content must be specified,
     * otherwise nothing is done.
     */
    public void start() {

        if (content == null) {
            // TODO log it
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
                close();
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

    /**
     * If there is no closing listener or it returns true, disposes the dialog.
     * Do not forget to dispose the dialog!
     */
    protected void close() {
        if (dialog != null && (closingListener == null || closingListener.closing() == true)) {
            dialog.dispose();
        }
    }

    /**
     * Convenience method, if we have simple content and do not want to extend
     * UIDialog.
     *
     * @param content a component
     * @param title the title
     */
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