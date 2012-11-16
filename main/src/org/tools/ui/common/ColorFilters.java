/*
 * Copyright (C) 2011 Trilarion
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
package org.tools.ui.common;

import java.awt.image.RGBImageFilter;

/**
 * Gray scale of inactive button images comes here.
 */
// TODO Implement a filter and use in ImageButton/ImageLabel
public class ColorFilters extends RGBImageFilter {

    @Override
    public int filterRGB(int x, int y, int rgb) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
