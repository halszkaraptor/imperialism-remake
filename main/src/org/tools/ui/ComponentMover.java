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
package org.tools.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.tools.ui.helper.GraphicsUtils;

/**
 * This class allows you to move a Component by using a mouse. The Component
 * moved can be a high level Window (ie. Window, Frame, Dialog) in which case
 * the Window is moved within the desktop. Or the Component can belong to a
 * Container in which case the Component is moved within the Container.
 *
 * When moving a Window, the listener can be added to a child Component of the
 * Window. In this case attempting to move the child will result in the Window
 * moving. For example, you might create a custom "Title Bar" for an undecorated
 * Window and moving of the Window is accomplished by moving the title bar only.
 * Multiple components can be registered as "window movers".
 *
 * It might be a good idea to set setAutoscrolls(false) on containing
 * components, otherwise movement might be non-smooth.
 *
 * Based on the wonderful work of Rob Camick, who made his code free. Thanks!
 * http://tips4java.wordpress.com/2009/06/14/moving-windows/
 */
public final class ComponentMover extends MouseAdapter {

    private Insets dragInsets = new Insets(0, 0, 0, 0);
    private Dimension snapSize = new Dimension(1, 1);
    private Insets edgeInsets = new Insets(0, 0, 0, 0);
    private Component destination;
    private Component target;
    private Component source;
    private Point pressed;
    private Point location;
    private Cursor originalCursor;
    private boolean isDragging = false;
    private boolean customCursor = true;

    /**
     * Constructor for moving individual components. The components must be
     * registered using the registerComponent() method.
     */
    public ComponentMover() {
    }

    /**
     *
     * @param destination
     */
    public void setDestination(Component target) {
        this.target = target;
    }

    /**
     * Get the change cursor property
     *
     * @return the change cursor property
     */
    public boolean isChangeCursor() {
        return customCursor;
    }

    /**
     * Set the change cursor property
     *
     * @param changeCursor when true the cursor will be changed to the
     * Cursor.MOVE_CURSOR while the mouse is pressed
     */
    public void setChangeCursor(boolean changeCursor) {
        this.customCursor = changeCursor;
    }

    /**
     * Get the drag insets
     *
     * @return the drag insets
     */
    public Insets getDragInsets() {
        return dragInsets;
    }

    /**
     * Set the drag insets. The insets specify an area where mouseDragged events
     * should be ignored and therefore the component will not be moved. This
     * will prevent these events from being confused with a MouseMotionListener
     * that supports component resizing.
     *
     * @param dragInsets
     */
    public void setDragInsets(Insets dragInsets) {
        this.dragInsets = dragInsets;
    }

    /**
     * Get the bounds insets
     *
     * @return the bounds insets
     */
    public Insets getEdgeInsets() {
        return edgeInsets;
    }

    /**
     * Set the edge insets. The insets specify how close to each edge of the
     * parent component that the child component can be moved. Positive values
     * means the component must be contained within the parent. Negative values
     * means the component can be moved outside the parent.
     *
     * @param edgeInsets
     */
    public void setEdgeInsets(Insets edgeInsets) {
        this.edgeInsets = edgeInsets;
    }

    /**
     * Remove listeners from the specified component
     *
     * @param components the components the listeners are removed from
     */
    public void deregisterComponent(Component... components) {
        for (Component component : components) {
            component.removeMouseListener(this);
        }
    }

    /**
     * Add the required listeners to the specified component
     *
     * @param components the components the listeners are added to
     */
    public void registerComponent(Component... components) {
        for (Component component : components) {
            component.addMouseListener(this);
        }
    }

    /**
     * Get the snap size
     *
     * @return the snap size
     */
    public Dimension getSnapSize() {
        return snapSize;
    }

    /**
     * Set the snap size. Forces the component to be snapped to the closest grid
     * position. Snapping will occur when the mouse is dragged half way.
     *
     * @param snapSize
     */
    public void setSnapSize(Dimension snapSize) {
        if (snapSize.width < 1 || snapSize.height < 1) {
            throw new IllegalArgumentException("Snap sizes must be greater than 0");
        }

        this.snapSize = snapSize;
    }

    /**
     * Setup the variables used to control the moving of the component:
     *
     * source - the source component of the mouse event destination - the
     * component that will ultimately be moved pressed - the Point where the
     * mouse was pressed in the destination component coordinates.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        source = e.getComponent();

        int width = source.getSize().width - dragInsets.left - dragInsets.right;
        int height = source.getSize().height - dragInsets.top - dragInsets.bottom;
        Rectangle r = new Rectangle(dragInsets.left, dragInsets.top, width, height);

        if (r.contains(e.getPoint())) {
            source.addMouseMotionListener(this);
            isDragging = true;

            // determine which component to move
            if (target != null) {
                destination = target;
            } else {
                destination = source;
            }

            pressed = e.getLocationOnScreen();
            location = destination.getLocation();

            if (customCursor) {
                originalCursor = source.getCursor();
                source.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }
    }

    /**
     * Move the component to its new location. The dragged Point must be in the
     * destination coordinates.
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point dragged = e.getLocationOnScreen();
        int dragX = getDragDistance(dragged.x, pressed.x, snapSize.width);
        int dragY = getDragDistance(dragged.y, pressed.y, snapSize.height);

        int locationX = location.x + dragX;
        int locationY = location.y + dragY;

        //  Mouse dragged events are not generated for every pixel the mouse
        //  is moved. Adjust the location to make sure we are still on a
        //  snap value.

        while (locationX < edgeInsets.left) {
            locationX += snapSize.width;
        }

        while (locationY < edgeInsets.top) {
            locationY += snapSize.height;
        }

        Dimension d = GraphicsUtils.getBoundingSize(destination);

        while (locationX + destination.getSize().width + edgeInsets.right > d.width) {
            locationX -= snapSize.width;
        }

        while (locationY + destination.getSize().height + edgeInsets.bottom > d.height) {
            locationY -= snapSize.height;
        }

        //  Adjustments are finished, move the component

        destination.setLocation(locationX, locationY);
    }

    /*
     * Determine how far the mouse has moved from where dragging started (Assume
     * drag direction is down and right for positive drag distance)
     */
    private int getDragDistance(int larger, int smaller, int snapSize) {
        int halfway = snapSize / 2;
        int drag = larger - smaller;
        drag += (drag < 0) ? -halfway : halfway;
        drag = (drag / snapSize) * snapSize;

        return drag;
    }

    /**
     * Restore the original state of the Component
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging == true) {
            isDragging = false;

            source.removeMouseMotionListener(this);

            if (customCursor) {
                source.setCursor(originalCursor);
            }
        }
    }
}