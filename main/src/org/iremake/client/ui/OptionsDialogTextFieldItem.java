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

import java.util.logging.Logger;
import javax.swing.JTextField;
import org.iremake.client.Option;

/**
 * Item with a text field. Internal state is String. String value is directly
 * stored and taken from the Option (Options have String as natural value).
 */
public class OptionsDialogTextFieldItem implements OptionsDialogItem {

   private static final Logger LOG = Logger.getLogger(OptionsDialogTextFieldItem.class.getName());    
    /* the ui element */
    private JTextField item;
    /* the option */
    private Option option;

    /**
     * Initializes the text field.
     *
     * @param item the ui element
     * @param option the option
     */
    public OptionsDialogTextFieldItem(JTextField item, Option option) {
        this.item = item;
        this.option = option;

        item.setText(option.get());
    }

    /**
     * @return True if the option's text not equals the text from the text
     * field.
     */
    @Override
    public boolean isModified() {
        return !(option.get().equals(item.getText()));
    }

    /**
     * Stores the text of the text field in the option.
     */
    @Override
    public void updateOption() {
        option.put(item.getText());
    }
 }