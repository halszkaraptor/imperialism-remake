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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.StartClient;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.ui.notification.NotificationFactory;
import org.tools.ui.utils.GraphicsUtils;

/**
 * There is one JFrame that holds various screens (start, editor and main game
 * screens) which need to be switched in between. This Manager here in the
 * singleton pattern does it.
 */
// TODO closing listener for clicking on the cross or alt-f4
public class FrameManager {

    private static final Logger LOG = Logger.getLogger(FrameManager.class.getName());

    /**
     * If the main frame closes we shut down.
     */
    private static class ShutDownOnCloseImpl implements FrameCloseListener {

        @Override
        public void close() {
            StartClient.shutDown();
        }
    }
    private static final FrameCloseListener shutDownCloseListener = new ShutDownOnCloseImpl();
    private static FrameManager singleton;
    private JFrame frame;
    private JPanel panel;
    private FrameCloseListener closingListener;

    /**
     * Creates the main frame. Because it is private we can control how often it
     * is called. Only once in this case.
     */
    private FrameManager() {
        frame = new JFrame();

        // undecorated ?
        frame.setUndecorated(false);

        // resizing ?
        frame.setResizable(true);

        // turn of usual exiting mechanisms
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (closingListener != null) {
                    closingListener.close();
                }
            }
        });

        // set title
        frame.setTitle("Imperialism Remake");

        // set minimum size (1024x768) according to specs
        frame.setMinimumSize(new Dimension(1024, 768));

        // set app icon
        frame.setIconImage(IOManager.getAsImage(Places.GraphicsIcons, "icon.app.png"));

        // set to bounds of the screen
        // GraphicsUtils.setFullScreen(frame);
        GraphicsUtils.setToMaximumBounds(frame);

        // create panel with border
        panel = new JPanel();
        // panel.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        panel.setOpaque(false);
        // set layoutmanager
        panel.setLayout(new MigLayout("fill, gap 0, insets 0"));

        // add panel to frame
        frame.add(panel);

        // set it visible
        frame.setVisible(true);
    }

    /**
     * Returns the instance (singleton) of this manager using lazy
     * initialization.
     *
     * @return The singleton instance.
     */
    public static synchronized FrameManager getInstance() {
        if (singleton == null) {
            singleton = new FrameManager();
        }
        return singleton;
    }

    /**
     * Disposes the singleton and the frame before.
     */
    public static void dispose() {
        singleton.frame.dispose();
        singleton = null;
    }

    /**
     * Removes old content and adds new one.
     *
     * @param content The component holding the new (full frame) content.
     */
    public void switchTo(JComponent content) {
        panel.removeAll();
        panel.add(content, "grow");
        panel.validate();
    }

    /**
     * Set a new closing listener handling pressing the x on the frame
     * decoration if not in fullscreen. Set to null if not needed.
     *
     * @param closingListener
     */
    public void setClosingListener(FrameCloseListener closingListener) {
        this.closingListener = closingListener;
    }

    /**
     *
     */
    public void setShutDownOnClose() {
        setClosingListener(shutDownCloseListener);
    }

    /**
     * Creates a new, modal dialog with the frame as parent.
     *
     * @param title The title;
     * @return
     */
    public JDialog getDialog(String title) {
        return new JDialog(frame, title, true);
    }

    /**
     * Shows the open dialog of a file chooser with the frame as parent.
     *
     * @param fileChooser A file chooser.
     * @return The return value of showOpenDialog()
     */
    public int showOpenDialog(JFileChooser fileChooser) {
        return fileChooser.showOpenDialog(frame);
    }

    /**
     * Shows the save dialog of a file chooser with the frame as parent.
     *
     * @param fileChooser A file chooser.
     * @return The return value of showSaveDialog()
     */
    public int showSaveDialog(JFileChooser fileChooser) {
        return fileChooser.showSaveDialog(frame);
    }

    /**
     * A simple input dialog with the frame as parent.
     *
     * @param text The dialogs text.
     * @return The return value of JOptionPane.showInputDialog()
     */
    public String showInputDialog(String text) {
        return JOptionPane.showInputDialog(frame, text);
    }

    /**
     * Centers a window relative to this frame.
     *
     * @param window The window to be centered.
     */
    public void center(Window window) {
        Rectangle bounds = frame.getBounds();
        Dimension size = window.getSize();
        window.setLocation(bounds.x + bounds.width / 2 - size.width / 2, bounds.y + bounds.height / 2 - size.height / 2);
    }

    /**
     *
     * @param message
     * @param black
     */
    // TODO real schedule, that is queue them instead of showing them instanteously
    public void scheduleInfoMessage(String message, boolean black) {
        Color color = black ? Color.black : Color.white;
        NotificationFactory.createInfoNotification(message, frame, color).setVisible();
    }
}
