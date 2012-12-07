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
package org.iremake.client.ui;

import javax.swing.JTextField;
import org.iremake.client.Option;

/**
 *
 */
public class OptionsDialogTextFieldItem implements OptionsDialogItem {

    private JTextField item;
    private Option option;

    public OptionsDialogTextFieldItem(JTextField item, Option option) {
        this.item = item;
        this.option = option;

        item.setText(option.get());
    }

    @Override
    public boolean isModified() {
        return !(option.get().equals(item.getText()));
    }

    @Override
    public void updateOption() {
        option.put(item.getText());
    }
}