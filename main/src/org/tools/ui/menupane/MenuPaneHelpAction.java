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
package org.tools.ui.menupane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.tools.common.Loader;
import org.tools.ui.Interruptible;
import org.tools.ui.common.TranslucentPanel;

/**
 * Basic implementation for a simple help action.
 */
public class MenuPaneHelpAction implements MenuPaneAction {

    private JLabel label;
    private Interruptible applet;

    public MenuPaneHelpAction(String message, Interruptible applet) {
        label = new JLabel(message);
        this.applet = applet;
    }

    @Override
    public Icon getIcon() {
        return Loader.getAsIcon(Loader.IconPath + "help.png");
    }

    @Override
    public void performAction(final TranslucentPanel content) {
        applet.pause();
        content.setTranslucence(0.3f);
        content.add(label);

        final MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO fix this
                // content.removeMouseListener(listener);
                content.remove(label);
                content.reset();
                applet.resume();
            }
        };
        content.addMouseListener(listener);
        content.revalidate();

    }
}