/*
 * Copyright (C) 2000 ymnk, JCraft,Inc.
 *               2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vorbis.jcraft.jorbis;

class Residue2 extends Residue0 {

    @Override
    int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
        int i;
        for (i = 0; i < ch; i++) {
            if (nonzero[i] != 0) {
                break;
            }
        }
        if (i == ch) {
            return 0; /* no nonzero vectors */
        }

        return _2inverse(vb, vl, in, ch);
    }
}
