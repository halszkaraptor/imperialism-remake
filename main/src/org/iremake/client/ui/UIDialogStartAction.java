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
package org.iremake.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class UIDialogStartAction implements ActionListener {

    private static final Logger LOG = Logger.getLogger(UIDialogStartAction.class.getName());

    private Class<? extends UIDialog> clazz;

    public UIDialogStartAction(Class<? extends UIDialog> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UIDialog dialog;
        try {
            dialog = clazz.newInstance();
            dialog.start();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }


}
