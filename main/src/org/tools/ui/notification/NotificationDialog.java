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

import java.awt.Component;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.layout.RelativeLayoutConstraint;
import org.tools.ui.utils.GraphicsUtils;
import org.tools.ui.utils.IconLoader;
import org.tools.ui.utils.WindowCorner;

/**
 *
 */
public class NotificationDialog extends Notification {

    private static final int TIMER_STEP = 100;
    private JDialog dialog;
    private float translucency;
    private Timer timer;
    private int inTime;
    private int outTime;
    private int onTime;

    @SuppressWarnings("LeakingThisInConstructor")
    public NotificationDialog(String message, IconLoader loader, Frame parent) {
        super(message);

        dialog = new JDialog(parent);

        // standard properties
        dialog.setUndecorated(true);
        dialog.setResizable(false);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // miglayout for getting the gaps right and pack
        dialog.setLayout(new MigLayout("ins 0"));
        dialog.add(Notification.createUIContent(this, loader));
        dialog.pack();

        // start in any case invisible
        updateTranslucency();
    }

    @Override
    public void setVisible() {
        dialog.setVisible(true);
        animateIn();
    }

    private void animateIn() {
        // fade in animation
        if (inTime > 0) {
            // create new timer with right timing and start
            final float increment = (float) TIMER_STEP / inTime;
            timer = new Timer(TIMER_STEP, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    translucency += increment;
                    if (translucency < 1) {
                        updateTranslucency();
                    } else {
                        // stop timer
                        timer.stop();

                        // set opaque
                        translucency = 1;
                        updateTranslucency();

                        // continue to hold
                        hold();
                    }
                }
            });
            timer.start();
        } else {
            // immediately set opaque
            translucency = 1;
            updateTranslucency();

            // continue to hold
            hold();
        }
    }

    private void hold() {
        if (onTime > 0) {
            timer = new Timer(onTime, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timer.stop();
                    animateOut();
                }
            });
            timer.start();
        }
    }

    private void animateOut() {
        if (outTime > 0) {
            final float decrement = (float) TIMER_STEP / outTime;
            timer = new Timer(TIMER_STEP, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    translucency -= decrement;
                    if (translucency > 0) {
                        updateTranslucency();
                    } else {
                        // stop timer
                        timer.stop();

                        // and dispose of dialog
                        stop();
                    }
                }
            });
            timer.start();
        } else {
            // stop immediately
            stop();
        }
    }

    private void updateTranslucency() {
        dialog.setOpacity(translucency);
    }

    private void stop() {
        // dispose of the dialog and notify the listeners
        dispose();
        notifyListeners(false);
    }

    @Override
    public void dispose() {
        dialog.dispose();
    }

    public void setLocationRelativeTo(Component component, RelativeLayoutConstraint constraint) {
        setLocationRelativeTo(component.getBounds(), constraint);
    }

    public void setLocationRelativeToDesktop(WindowCorner corner, int gapx, int gapy) {
        setLocationRelativeTo(GraphicsUtils.MaximumBounds, RelativeLayoutConstraint.corner(corner, gapx, gapy));
    }

    private void setLocationRelativeTo(Rectangle bounds, RelativeLayoutConstraint constraint) {
        dialog.setBounds(constraint.calculateBounds(bounds, dialog.getSize()));
    }

    public void setFadeInTime(int time) {
        checkPositivity(time);
        inTime = time;
    }

    public void setFadeOutTime(int time) {
        checkPositivity(time);
        outTime = time;
    }

    public void setOnTime(int time) {
        checkPositivity(time);
        onTime = time;
    }

    private void checkPositivity(int time) {
        if (time < 0) {
            throw new IllegalArgumentException("time value cannot be negative!");
        }
    }
}
