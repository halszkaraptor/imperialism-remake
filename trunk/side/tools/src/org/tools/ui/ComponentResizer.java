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
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.tools.ui.utils.GraphicsUtils;
import org.tools.ui.utils.WindowSide;

/**
 * Allows you to resize a component by dragging a border of the component.
 *
 * It might be a good idea to set setAutoscrolls(false) on containing
 * components, otherwise movement might be non-smooth.
 *
 * Based on the wonderful work of Rob Camick, who made his code free. Thanks!
 * http://tips4java.wordpress.com/2009/09/13/resizing-components/
 */
public class ComponentResizer extends MouseAdapter {

    private WindowSide horizontal, vertical;
    private Insets dragInsets;
    private Dimension snapSize;
    /**
     * The cursor which is present before we change it.
     */
    private Cursor sourceCursor;
    private boolean resizing;
    private Rectangle bounds;
    private Point pressed;

    /**
     * Convenience constructor. All borders are resizable in increments of a
     * single pixel. Components must be registered separately.
     */
    public ComponentResizer() {
        this(new Insets(5, 5, 5, 5), new Dimension(1, 1));
    }

    /**
     * Convenience constructor. Eligible borders are resizable in increments of
     * a single pixel.
     *
     * @param dragInsets Insets specifying which borders are eligible to be
     * resized.
     */
    public ComponentResizer(Insets dragInsets) {
        this(dragInsets, new Dimension(1, 1));
    }

    /**
     * Create a ComponentResizer.
     *
     * @param dragInsets Insets specifying which borders are eligible to be
     * resized.
     * @param snapSize Specify the dimension to which the border will snap to
     * when being dragged. Snapping occurs at the halfway mark.
     */
    public ComponentResizer(Insets dragInsets, Dimension snapSize) {
        setDragInsets(dragInsets);
        setSnapSize(snapSize);
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
     * Set the drag dragInsets. The insets specify an area where mouseDragged
     * events are recognized from the edge of the border inwards. A value of 0
     * for any size will imply that the border is not resizable. Otherwise the
     * appropriate drag cursor will appear when the mouse is inside the
     * resizable border area.
     *
     * @param dragInsets Insets to control which borders are resizable.
     */
    public void setDragInsets(Insets dragInsets) {
        this.dragInsets = dragInsets;
    }

    /**
     * Remove listeners from the specified component
     *
     * @param components the components the listeners are removed from
     */
    public void deregisterComponent(Component... components) {
        for (Component component : components) {
            component.removeMouseListener(this);
            component.removeMouseMotionListener(this);
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
            component.addMouseMotionListener(this);
        }
    }

    /**
     * Get the snap size.
     *
     * @return the snap size.
     */
    public Dimension getSnapSize() {
        return snapSize;
    }

    /**
     * Control how many pixels a border must be dragged before the size of the
     * component is changed. The border will snap to the size once dragging has
     * passed the halfway mark.
     *
     * @param snapSize Dimension object allows you to separately specify a
     * horizontal and vertical snap size.
     */
    public void setSnapSize(Dimension snapSize) {
        this.snapSize = snapSize;
    }

    /**
     * When the components minimum size is less than the drag insets then we
     * can't determine which border should be resized so we need to prevent this
     * from happening.
     */
    private void validateMinimumAndInsets(Dimension minimum, Insets drag) {
        int minimumWidth = drag.left + drag.right;
        int minimumHeight = drag.top + drag.bottom;

        if (minimum.width < minimumWidth || minimum.height < minimumHeight) {
            String message = "Minimum size cannot be less than drag insets";
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        Component source = e.getComponent();
        Point location = e.getPoint();
        horizontal = WindowSide.None;
        vertical = WindowSide.None;

        if (location.x < dragInsets.left) {
            horizontal = WindowSide.West;
        }

        if (location.x > source.getWidth() - dragInsets.right - 1) {
            horizontal = WindowSide.East;
        }

        if (location.y < dragInsets.top) {
            vertical = WindowSide.North;
        }

        if (location.y > source.getHeight() - dragInsets.bottom - 1) {
            vertical = WindowSide.South;
        }

        //  Mouse is no longer over a resizable border

        if (horizontal == WindowSide.None && vertical == WindowSide.None) {
            source.setCursor(sourceCursor);
        } else {
            // use the appropriate resizable cursor
            int cursorType = GraphicsUtils.getResizeCursorForSide(vertical, horizontal);
            Cursor cursor = Cursor.getPredefinedCursor(cursorType);
            source.setCursor(cursor);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!resizing) {
            Component source = e.getComponent();
            sourceCursor = source.getCursor();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!resizing) {
            Component source = e.getComponent();
            source.setCursor(sourceCursor);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //	The mouseMoved event continually updates this variable

        if (horizontal == WindowSide.None && vertical == WindowSide.None) {
            return;
        }

        //  Setup for resizing. All future dragging calculations are done based
        //  on the original bounds of the component and mouse pressed location.

        resizing = true;

        Component source = e.getComponent();
        pressed = e.getPoint();
        SwingUtilities.convertPointToScreen(pressed, source);
        bounds = source.getBounds();
    }

    /**
     * Restore the original state of the Component
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        resizing = false;

        Component source = e.getComponent();
        source.setCursor(sourceCursor);
    }

    /**
     * Resize the component ensuring location and size is within the bounds of
     * the parent container and that the size is within the minimum and maximum
     * constraints.
     *
     * All calculations are done using the bounds of the component when the
     * resizing started.
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (resizing == false) {
            return;
        }

        Component source = e.getComponent();
        Point dragged = e.getPoint();
        SwingUtilities.convertPointToScreen(dragged, source);

        changeBounds(source, bounds, pressed, dragged);
    }

    void changeBounds(Component source, Rectangle bounds, Point pressed, Point current) {
        //  Start with original locaton and size

        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;

        //  Resizing the West or North border affects the size and location

        Dimension maximumSize = source.getMaximumSize();
        Dimension minimumSize = source.getMinimumSize();
        validateMinimumAndInsets(minimumSize, dragInsets);

        if (horizontal == WindowSide.West) {
            int drag = getDragDistance(pressed.x, current.x, snapSize.width);
            int maximum = Math.min(width + x, maximumSize.width);
            drag = getDragBounded(drag, snapSize.width, width, minimumSize.width, maximum);

            x -= drag;
            width += drag;
        }

        if (vertical == WindowSide.North) {
            int drag = getDragDistance(pressed.y, current.y, snapSize.height);
            int maximum = Math.min(height + y, maximumSize.height);
            drag = getDragBounded(drag, snapSize.height, height, minimumSize.height, maximum);

            y -= drag;
            height += drag;
        }

        //  Resizing the East or South border only affects the size

        if (horizontal == WindowSide.East) {
            int drag = getDragDistance(current.x, pressed.x, snapSize.width);
            Dimension boundingSize = GraphicsUtils.getBoundingSize(source);
            int maximum = Math.min(boundingSize.width - x, maximumSize.width);
            drag = getDragBounded(drag, snapSize.width, width, minimumSize.width, maximum);
            width += drag;
        }

        if (vertical == WindowSide.South) {
            int drag = getDragDistance(current.y, pressed.y, snapSize.height);
            Dimension boundingSize = GraphicsUtils.getBoundingSize(source);
            int maximum = Math.min(boundingSize.height - y, maximumSize.height);
            drag = getDragBounded(drag, snapSize.height, height, minimumSize.height, maximum);
            height += drag;
        }

        source.setBounds(x, y, width, height);
        source.validate();
    }

    /*
     * Determine how far the mouse has moved from where dragging started
     */
    private int getDragDistance(int larger, int smaller, int snapSize) {
        int halfway = snapSize / 2;
        int drag = larger - smaller;
        drag += (drag < 0) ? -halfway : halfway;
        drag = (drag / snapSize) * snapSize;

        return drag;
    }

    /*
     * Adjust the drag value to be within the minimum and maximum range.
     */
    private int getDragBounded(int drag, int snapSize, int dimension, int minimum, int maximum) {
        while (dimension + drag < minimum) {
            drag += snapSize;
        }

        while (dimension + drag > maximum) {
            drag -= snapSize;
        }

        return drag;
    }
    private static final Logger LOG = Logger.getLogger(ComponentResizer.class.getName());
}
