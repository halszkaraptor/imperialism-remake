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

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.tools.ui.utils.GraphicsUtils;

/**
 *
 */
public class OurListCellRenderer extends DefaultListCellRenderer {
    
    private static final Color BG_NORMAL = new Color(196, 196, 196, 64);

    public static interface ListCellInfoProvider {

        public String getToolTip(Object value);
    }
    private ListCellInfoProvider infoProvider;

    /**
     * 
     * @param infoProvider The info provider or null if not wanted.
     */
    public OurListCellRenderer(ListCellInfoProvider infoProvider) {
        this.infoProvider = infoProvider;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // not transparent, paint background
        setOpaque(true);
        
        // textcolor is black
        setForeground(Color.black);
        
        // set background
        if (isSelected) {
            setBackground(BG_NORMAL);
        } else {
            setBackground(GraphicsUtils.TRANSPARENT);
        }

        // set tooltiptext by provider if given
        if (infoProvider != null) {
            setToolTipText(infoProvider.getToolTip(value));
        }
        
        return this;
    }
}
