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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.utils.IconLoader;

/**
 * Small notification at some position within some window.
 */
// TODO revisit and improve design, maybe unify both variants with the onset of jdk 7
public abstract class Notification {

    private static final int TIMER_DELAY = 100;
    protected JPanel panel;
    private ArrayList<NotificationListener> notificationListeners = new ArrayList<>(3);
    private float alpha = 1f;
    private Timer timer;
    private int fadeInTime, fadeOutTime, onTime;
    private IconLoader loader;

    /**
     *
     * @param message
     */
    public Notification(String message, IconLoader loader) {
        this.loader = loader;
        ActionListener cancelAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTimer();
                fastDismiss();
                for (NotificationListener l : notificationListeners) {
                    l.notificationClicked(false);
                }
            }
        };
        MouseListener defaultAction = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetTimer();
                fastDismiss();
                for (NotificationListener l : notificationListeners) {
                    l.notificationClicked(true);
                }
            }
        };

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Composite oldComposite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                super.paint(g);
                g2d.setComposite(oldComposite);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(this.getBackground());
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
        };

        // special icon to the left
        JLabel infoIcon = new JLabel();
        infoIcon.setIcon(loader.getAsIcon("notification.information.png")); // NOI18N

        // Message
        JLabel msgLabel = new JLabel();
        msgLabel.setText(message);
        msgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        msgLabel.addMouseListener(defaultAction);

        // close button to the right
        JButton closeButton = new JButton();
        closeButton.setIcon(loader.getAsIcon("notification.cross.png")); // NOI18N
        closeButton.addActionListener(cancelAction);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setFocusable(false);
        closeButton.setBorder(null);

        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        // layout
        panel.setLayout(new MigLayout("wrap 3, fill", "[][grow][]"));
        panel.add(infoIcon);
        panel.add(msgLabel, "grow");
        panel.add(closeButton, "aligny top");

        panel.setSize(panel.getPreferredSize()); // TODO needed?
        panel.setOpaque(false);
    }

    /**
     *
     */
    abstract void dispose();

    /**
     *
     * @param l
     */
    public void addNotificationListener(NotificationListener l) {
        notificationListeners.add(l);
    }

    /**
     *
     * @param l
     */
    public void removeNotificationListener(NotificationListener l) {
        notificationListeners.remove(l);
    }

    /**
     *
     * @param onTime
     */
    public void setOnTime(int onTime) {
        if (onTime < 0) {
            throw new IllegalArgumentException();
        }
        this.onTime = onTime;
    }

    /**
     *
     * @param fadeInTime
     */
    public void setFadeInTime(int fadeInTime) {
        if (fadeInTime < 0) {
            throw new IllegalArgumentException();
        }
        this.fadeInTime = fadeInTime;
    }

    /**
     *
     * @param fadeOutTime
     */
    public void setFadeOutTime(int fadeOutTime) {
        if (fadeOutTime < 0) {
            throw new IllegalArgumentException();
        }
        this.fadeOutTime = fadeOutTime;
    }

    /**
     *
     */
    private void fastDismiss() {
        alpha = 1;
        panel.repaint();
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTimer();
                dispose();
            }
        });
        timer.start();
    }

    /**
     *
     */
    private void slowDismiss() {
        if (fadeOutTime > 0) {
            alpha = 1;
            final float alphaIncrement = (float) TIMER_DELAY / fadeOutTime;
            timer = new Timer(TIMER_DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    alpha -= alphaIncrement;
                    if (alpha > 0) {
                        panel.repaint();
                    } else {
                        alpha = 0;
                        panel.repaint();
                        resetTimer();
                        cancelAndDispose();
                    }
                }
            });
            timer.start();
        } else {
            cancelAndDispose();
        }
    }

    /**
     *
     */
    private void resetTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    /**
     *
     */
    public abstract void setVisible();

    private void cancelAndDispose() {
        for (NotificationListener l : notificationListeners) {
            l.notificationClicked(false);
        }
        dispose();
    }

    /**
     *
     */
    public void scheduleDismiss() {
        if (onTime > 0) {
            timer = new Timer(onTime, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetTimer();
                    slowDismiss();
                }
            });
            timer.start();
        }
    }

    /**
     *
     */
    public void activate() {
        setVisible();
        if (fadeInTime > 0) {
            final float alphaIncrement = (float) TIMER_DELAY / fadeInTime;
            alpha = 0;
            timer = new Timer(TIMER_DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    alpha += alphaIncrement;
                    if (alpha < 1) {
                        panel.repaint();
                    } else {
                        alpha = 1;
                        panel.repaint();
                        resetTimer();
                        scheduleDismiss();
                    }
                }
            });
            timer.start();
        } else {
            scheduleDismiss();
        }
    }
}