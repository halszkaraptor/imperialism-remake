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
package org.iremake.client.ui.map;

import java.awt.Color;
import java.awt.Image;
import java.util.Objects;

/**
 *
 */
public class TerrainTile {

    private String type;
    private int variant;
    private Image image;
    private Color color;

    public TerrainTile(String type, int variant, Image image, Color color) {
        this.type = type;
        this.variant = variant;
        this.image = image;
        this.color = color;
    }

    public int getVariant() {
        return variant;
    }

    public Image getImage() {
        return image;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public int hashCode() {
        return type.hashCode() + variant;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TerrainTile other = (TerrainTile) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (this.variant != other.variant) {
            return false;
        }
        return true;
    }
}