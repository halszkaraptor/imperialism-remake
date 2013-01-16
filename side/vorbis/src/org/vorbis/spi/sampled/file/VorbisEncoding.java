/*
 * Copyright (C) 2008 JavaZOOM
 *               2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
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
package org.vorbis.spi.sampled.file;

import javax.sound.sampled.AudioFormat;

/**
 * Encodings used by the VORBIS audio decoder.
 */
public class VorbisEncoding extends AudioFormat.Encoding {

    public static final AudioFormat.Encoding VORBISENC = new VorbisEncoding("VORBISENC");

    /**
     * Constructors.
     */
    public VorbisEncoding(String name) {
        super(name);
    }
}
