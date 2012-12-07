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
package org.iremake.client.ui.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.UIDialog;

/**
 *
 */
public class GameDialogBuilder {

    public static void build(GamePanel panel) {

        JPanel content = new JPanel();

        JToolBar bar = new JToolBar();

        for (final GamePanel p: GamePanel.values()) {
            JButton button = new JButton(p.toString());
            // TODO standard button
            button.setFocusable(false);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO the action
                }
            });
            bar.add(button);
        }

        JPanel inner = panel.create();

        content.setLayout(new MigLayout("wrap 1, fill", "", "[][grow]"));
        content.add(bar);
        content.add(inner, "grow");

        UIDialog.make(content, panel.toString());
    }

}
