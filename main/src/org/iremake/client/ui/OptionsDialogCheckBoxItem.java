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

import javax.swing.JCheckBox;
import org.iremake.client.Option;

/**
 *
 */
public class OptionsDialogCheckBoxItem implements OptionsDialogItem {

    private JCheckBox item;
    private Option option;

    public OptionsDialogCheckBoxItem(JCheckBox item, Option option) {
        this.item = item;
        this.option = option;

        item.setSelected(option.getBoolean());
    }

    @Override
    public boolean isModified() {
        return !(option.getBoolean() == item.isSelected());
    }

    @Override
    public void updateOption() {
        option.putBoolean(item.isSelected());
    }
}
