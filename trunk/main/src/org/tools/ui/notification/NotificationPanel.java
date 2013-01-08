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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.ButtonFactory;
import org.tools.ui.utils.IconLoader;

/**
 *
 */
// TODO maybe make static method of Notificiaton
public abstract class NotificationPanel extends Notification {

    private JPanel panel;

    public NotificationPanel(String message, IconLoader loader) {
        super(message);

        // panel holding the notification
        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setOpaque(false);

        // info icon on the left size
        JLabel infoIcon = new JLabel();
        infoIcon.setIcon(loader.getAsIcon("notification.information.png")); // NOI18N

        // message label in the center
        JLabel msgLabel = new JLabel();
        msgLabel.setText(getMessage());
        msgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        msgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                notifyListeners(true);
            }
        });

        // close button to the right
        JButton closeButton =  ButtonFactory.create(loader.getAsIcon("notification.cross.png"), "Dismiss");
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                notifyListeners(false);
            }
        });

        // layout
        panel.setLayout(new MigLayout("fill, ins 0", "[][grow][]"));
        panel.add(infoIcon, "gap r r r r");
        panel.add(msgLabel, "grow, gap 0 0 r r");
        panel.add(closeButton, "gap r 0 0 r, aligny top");
    }

    protected final JPanel getPanel() {
        return panel;
    }

}
