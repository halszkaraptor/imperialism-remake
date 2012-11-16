/*
 * Copyright (C) 2011 Trilarion
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
package org.tools.ui.notification;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.tools.ui.helper.GraphicsUtils;
import org.tools.ui.helper.WindowCorner;

/**
 *
 * Location: can be specified (corners of the window, corners of the parent
 * frame, relative to another component in the parent) Layout: Icon (left), Text
 * in the middle (intelligent wrapping), Close button right Balloon-layout,
 * other layouts possible ActionListener: when clicking the cancel/close button
 * a message is sent that it closes, otherwise when clicking somewhere else
 * Fade-in/out: Can fade in/out can be specified, fixed display time can be
 * specified (afterwards, automatically closed).
 *
 * modeless dialog
 *
 * At a time in the future translucent windows will be available in the official
 * Java Libraries and then we will support it:
 * http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
 */
public class NotificationDialog extends Notification {

    private static final long serialVersionUID = 1L;
    private JDialog dialog;

    /**
     *
     * @param parent
     * @param message
     */
    public NotificationDialog(JFrame parent, String message) {
        super(message);
        dialog = new JDialog(parent);

        dialog.add(panel);

        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // no alt+f4 or similar things
        dialog.setLocationRelativeTo(null);

        dialog.pack();
    }

    /**
     *
     * @param component
     * @param corner
     */
    public void setLocationRelativeTo(Component component, WindowCorner corner) {
        GraphicsUtils.setLocationRelativeTo(dialog, component, corner);
    }

    /**
     *
     * @param corner
     */
    public void setLocationRelativeToDesktop(WindowCorner corner) {
        GraphicsUtils.setLocationRelativeToDesktop(dialog, corner);
    }

    @Override
    public void setVisible() {
        dialog.setVisible(true);
    }

    @Override
    void dispose() {
        dialog.dispose();
    }
}