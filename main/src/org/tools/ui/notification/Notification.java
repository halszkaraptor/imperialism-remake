/*
 * Copyright (C) 2013 Trilarion
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.ButtonFactory;
import org.tools.ui.utils.IconLoader;

/**
 *
 */
public abstract class Notification {

    private String message;
    private List<NotificationListener> listeners = new ArrayList<>(3);

    public Notification(String message) {
        this.message = message;
    }

    public final String getMessage() {
        return message;
    }

    public final void addNotificationListener(NotificationListener l) {
        listeners.add(l);
    }

    public final void removeNotificationListener(NotificationListener l) {
        listeners.remove(l);
    }

    protected final void notifyListeners(boolean value) {
        for (NotificationListener l : listeners) {
            l.notificationResult(value);
        }
    }

    public abstract void setVisible();

    public abstract void dispose();

    /**
     *
     * @param notification
     * @param loader
     * @return
     */
    public static JComponent createUIContent(final Notification notification, IconLoader loader) {

        // panel holding the notification
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setOpaque(false);

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
}
