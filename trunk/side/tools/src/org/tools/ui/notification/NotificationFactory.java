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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.ButtonFactory;
import org.tools.ui.layout.RelativeLayoutConstraint;
import org.tools.ui.utils.IconLoader;
import org.tools.ui.utils.WindowCorner;
import org.tools.ui.utils.WindowSide;

/**
 * Static helper class for creating finished, commonly used notification
 * dialogs. There are two content styles: one with icons and border and one with
 * just a message and a translucent background. The style with icons
 * automatically delegates to the other if no icon loader is specified before.
 * Display time of the notifications is about 5 seconds with fade in and out. No
 * NotificationListener is added.
 *
 * To show the notification call the setVisible() method.
 */
// TODO if several notifications are displayed, the last ones should be placed on top, is this so?
public class NotificationFactory {

    /* For the style with icons we need a loader, better specify it only once */
    private static IconLoader defaultLoader;

    /**
     * To avoid instantiation because this is an utility class.
     */
    private NotificationFactory() {
    }

    /**
     * Sets the loader that is used for getting icons.
     *
     * @param loader a loader
     */
    public static void setIconLoader(IconLoader loader) {
        defaultLoader = loader;
    }

    /**
     * Creates a new notification dialog in the style with icons (unless there
     * is no loader specified before in that case we return the other style
     * instead), the location is set to the right, lower border of the parent
     * and the display time is about 5 seconds including fade in and out.
     *
     * @param message the message
     * @param parent the parent frame
     * @return the new notification dialog
     */
    public static NotificationDialog createStandardNotification(String message, Frame parent) {
        // if no loader is given, delgate to the other style with black text
        if (defaultLoader == null) {
            return createInfoNotification(message, parent, Color.black);
        }

        // create a new notification dialog
        NotificationDialog dlg = new NotificationDialog(message, parent);
        JComponent content = createStandardContent(dlg, defaultLoader);
        dlg.setContent(content);

        // set location to right, lower corner of parent
        dlg.setLocationRelativeTo(parent, RelativeLayoutConstraint.corner(WindowCorner.SouthEast, 5, 5));

        // set fading times, 1.5s in, 2s stay, 1.5s out
        dlg.setFadeInTime(1500);
        dlg.setFadeOutTime(1500);
        dlg.setOnTime(2000);

        return dlg;
    }

    /**
     * Creates a new notification dialog in the style without icons. The color
     * of the text can be chosen according to the parent's background (white of
     * dark background and vice versa). The location is at the lower center of
     * the parent's area and the display duration is about 5 seconds including
     * fade in and out times.
     *
     * @param message the message
     * @param parent the parent frame
     * @param textColor the text color of the message
     * @return the new notification dialog
     */
    public static NotificationDialog createInfoNotification(String message, Frame parent, Color textColor) {

        // create new dialog
        NotificationDialog dlg = new NotificationDialog(message, parent);
        Color bgColor = new Color(128, 128, 128, 32); // just a touch of translucent gray
        JComponent content = createInfoContent(dlg, bgColor, textColor);
        dlg.setContent(content);

        // set location to lower side (inside) and horizontally centered
        dlg.setLocationRelativeTo(parent, RelativeLayoutConstraint.side(WindowSide.South, 20));

        // set fading times, 1.5s in, 2s stay, 1.5s out
        dlg.setFadeInTime(1500);
        dlg.setFadeOutTime(1500);
        dlg.setOnTime(2000);

        return dlg;
    }

    /**
     * Creates the component of the standard style with icons. That is an info
     * icon to the left and a cancel button at the right side. Clicking on the
     * message text accepts the notification while clicking on the cancel button
     * dismisses the notifcation.
     *
     * @param notification the notification (NotificationDialog) to get
     * messages...
     * @param loader the icon loader
     * @return the new content component in icon style
     */
    private static JComponent createStandardContent(final Notification notification, IconLoader loader) {

        // panel holding the notification
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setOpaque(true);

        // info icon on the left size
        JLabel infoIcon = new JLabel();
        infoIcon.setIcon(loader.getAsIcon("notification.information.png")); // NOI18N

        // message label in the center
        JLabel msgLabel = new JLabel();
        msgLabel.setText(notification.getMessage());
        msgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // if clicked will dismiss the notification and notify listeners accepted
        msgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                notification.dispose();
                notification.notifyListeners(true);
            }
        });

        // close button to the right
        JButton closeButton = ButtonFactory.create(loader.getAsIcon("notification.cross.png"), "Dismiss");
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // if clicked will dismiss the notification and notify listeners dismissed
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notification.dispose();
                notification.notifyListeners(false);
            }
        });

        // layout (related gaps everywhere except for close button which is on the right, upper corner)
        panel.setLayout(new MigLayout("fill, ins 0", "[][grow][]"));
        panel.add(infoIcon, "gap r r r r");
        panel.add(msgLabel, "grow, gap 0 0 r r");
        panel.add(closeButton, "gap r 0 0 r, aligny top");

        return panel;
    }

    /**
     * Creates a component for the alternative style without icons. It's just a
     * label with some empty border, a background and a foreground color.
     * Clicking on the label accepts the notification.
     *
     * @param notification the notification object
     * @param bgColor the background color
     * @param textColor the text color
     * @return the new component in this style
     */
    private static JComponent createInfoContent(final Notification notification, Color bgColor, Color textColor) {
        // create new label
        JLabel message = new JLabel(notification.getMessage());

        // set property (opaque, empty border, background and foreground colors)
        message.setOpaque(true);
        message.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        message.setBackground(bgColor);
        message.setForeground(textColor);
        message.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // if clicked will dismiss the notification and notify listeners accepted
        message.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                notification.dispose();
                notification.notifyListeners(true);
            }
        });

        return message;
    }
}