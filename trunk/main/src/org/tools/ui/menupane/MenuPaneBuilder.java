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

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.tools.ui.common.TranslucentPanel;

/**
 * A builder for the complex wiring.
 */
public class MenuPaneBuilder {

    private MenuPaneBuilder() {
    }

    static public MenuPane build(List<MenuPaneAction> actions) {
        JPanel outer = new JPanel(null, true);
        outer.setOpaque(false);

        TranslucentPanel content = new TranslucentPanel();

        // layout
        GroupLayout layout = new GroupLayout(outer);
        outer.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        SequentialGroup buttonsS = layout.createSequentialGroup();
        ParallelGroup buttonsP = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (MenuPaneAction action : actions) {
            JLabel button = MenuPaneBuilder.createButton(action, content);
            buttonsS.addComponent(button);
            buttonsP.addComponent(button);
        }

        layout.setHorizontalGroup(layout.createParallelGroup().addGroup(GroupLayout.Alignment.TRAILING, buttonsS).addComponent(content, GroupLayout.Alignment.CENTER));
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(buttonsP).addComponent(content));

        MenuPane pane = new MenuPane(outer);
        return pane;

    }

    static private JLabel createButton(final MenuPaneAction action, final TranslucentPanel content) {
        JLabel button = new JLabel();
        button.setIcon(action.getIcon());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                action.performAction(content);
            }
        });

        return button;
    }
}