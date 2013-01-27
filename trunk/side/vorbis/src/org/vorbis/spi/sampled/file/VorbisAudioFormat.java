/*
 * Copyright (C) 2008 JavaZOOM
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
package org.vorbis.spi.sampled.file;

import java.util.Map;
import javax.sound.sampled.AudioFormat;
import org.sound.sampled.TAudioFormat;

/**
 * @author JavaZOOM
 */
public class VorbisAudioFormat extends TAudioFormat {

    /**
     * Constructor.
     *
     * @param encoding
     * @param nFrequency
     * @param SampleSizeInBits
     * @param nChannels
     * @param FrameSize
     * @param FrameRate
     * @param isBigEndian
     * @param properties
     */
    public VorbisAudioFormat(AudioFormat.Encoding encoding, float nFrequency, int SampleSizeInBits, int nChannels, int FrameSize, float FrameRate, boolean isBigEndian, Map properties) {
        super(encoding, nFrequency, SampleSizeInBits, nChannels, FrameSize, FrameRate, isBigEndian, properties);
    }

    /**
     * Ogg Vorbis audio format parameters. Some parameters might be unavailable.
     * So availability test is required before reading any parameter.
     *
     * <br>AudioFormat parameters. <ul> <li><b>bitrate</b> [Integer], bitrate in
     * bits per seconds, average bitrate for VBR enabled stream. <li><b>vbr</b>
     * [Boolean], VBR flag. </ul>
     */
    @Override
    public Map properties() {
        return super.properties();
    }
}
