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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import org.tools.ui.layout.RelativeLayoutConstraint;
import org.tools.ui.utils.GraphicsUtils;
import org.tools.ui.utils.WindowCorner;

/**
 * Implementation of the Notification class. We use a non-modal JDialog because
 * its location can be set most flexible and since Java 7 one can set the
 * translucency of Windows. The actual content component must be set from
 * outside, but it may be that it must call methods of this class like
 * notifyListeners() or dispose().
 *
 * Example of a full working notification:
 *
 * NotificationDialog dialog = new NotificationDialog(message, parent);
 * dialog.setContent(myContentGenerator(dialog));
 * dialog.addNotifyListener(listener);
 * dialog.setFadeInTime(xxx);
 * ...
 * dialog.setVisible();
 */
public class NotificationDialog extends Notification {

    /* one refresh every XX ms */
    private static final int TIMER_STEP = 80;
    /* the central UI element */
    private JDialog dialog;
    /* current translucency of the dialog */
    private float translucency;
    /* timer used in animations */
    private Timer timer;
    /* fade in time in ms */
    private int inTime;
    /* fade out time in ms */
    private int outTime;
    /* staying on time in ms (0 means forever) */
    private int onTime;

    /**
     * Constructs a new instance and sets the notification message and the
     * parent frame of the dialog.
     *
     * @param message the notification message
     * @param parent parent frame
     */
    public NotificationDialog(String message, Frame parent) {
        super(message);

        // new non-modal dialog
        dialog = new JDialog(parent, false);

        // standard properties (no decoration, transparent background, not resizable, on top, ...)
        dialog.setUndecorated(true);
        dialog.setBackground(GraphicsUtils.TRANSPARENT);
        dialog.setResizable(false);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // start in any case invisible
        updateTranslucency();
    }

    /**
     * Sets the content, it can be any component but it might want to call
     * methods from this object. We don't want to set it already in the
     * constructor because of "leaking this in constructor" and it might be
     * anything. The dialog is packed afterwards. This function should ideally
     * be called exactly once after the creation.
     *
     * @param content the content component
     */
    public void setContent(JComponent content) {
        dialog.add(content);
        dialog.pack();
    }

    /**
     * Sets the dialog visible and starts a possible fading in animation.
     */
    @Override
    public void setVisible() {
        dialog.setVisible(true);
        animateIn();
    }

    /**
     * If fading in is wished (fade in time > 0) then start the timer and
     * perform the animation. In either way check at the end if we have a finite
     * on time.
     */
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

    /**
     * Check for finite one time, if so wait for the specified time and then
     * check for fade out animation.
     */
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

    /**
     * If there is a fade out time > 0, do the fade out animation. In either
     * case stop at the end.
     */
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

    /**
     * Used in every animation run. Adjusts the translucency of the whole
     * dialog.
     */
    private void updateTranslucency() {
        dialog.setOpacity(translucency);
    }

    /**
     * Stops the whole dialog by disposing it and notifying the listeners with
     * result false (i.e. no user interaction within the life time of the
     * notification dialog).
     */
    private void stop() {
        // dispose of the dialog and notify the listeners
        dispose();
        notifyListeners(false);
    }

    /**
     * Disposes the dialog.
     */
    @Override
    public void dispose() {
        dialog.dispose();
    }

    /**
     * Sets the location of the notification relative to another component with
     * the constraint given by a RelativeLayoutConstraint, which allows any
     * combination of parent size and notification size and constant offsets.
     *
     * @param component the parent component
     * @param constraint a relative constraint indicating the position
     */
    public void setLocationRelativeTo(Component component, RelativeLayoutConstraint constraint) {
        setLocationRelativeTo(component.getBounds(), constraint);
    }

    /**
     * Sets the location of the notification relative to the desktop to any
     * corner and additionally specifying the gaps to the nearest borders.
     *
     * @param corner the desktop corner
     * @param gapx gap to the left or right border (depending on the corner)
     * @param gapy gap to the upper or lower border (depending on the corner)
     */
    public void setLocationRelativeToDesktop(WindowCorner corner, int gapx, int gapy) {
        setLocationRelativeTo(GraphicsUtils.MaximumBounds, RelativeLayoutConstraint.corner(corner, gapx, gapy));
    }

    /**
     * Internal function for setting the location working with a bound of
     * another component and a relative constraint.
     *
     * @param bounds rectangle
     * @param constraint relative constraint
     */
    private void setLocationRelativeTo(Rectangle bounds, RelativeLayoutConstraint constraint) {
        dialog.setBounds(constraint.calculateBounds(bounds, dialog.getSize()));
    }

    /**
     * Sets the fade in time in ms. Must be non-negative. Default is 0.
     *
     * @param time time in ms
     */
    public void setFadeInTime(int time) {
        checkPositivity(time);
        inTime = time;
    }

    /**
     * Sets the fade out time in ms. Must be non-negative. Default is 0.
     *
     * @param time time in ms
     */
    public void setFadeOutTime(int time) {
        checkPositivity(time);
        outTime = time;
    }

    /**
     * Sets the on time. Must be non-negative. A time of 0 means that the
     * notification should stay until the user cancels it (this is the default).
     *
     * @param time time in ms
     */
    public void setOnTime(int time) {
        checkPositivity(time);
        onTime = time;
    }

    /**
     * Checks the positivity of the times.
     *
     * @param time time in ms
     */
    private void checkPositivity(int time) {
        if (time < 0) {
            throw new IllegalArgumentException("time value cannot be negative!");
        }
    }
}
