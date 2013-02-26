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
package org.tools.sound;

import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 * Some utility functions
 */
public class SoundUtils {

    /**
     * No instantiation.
     */
    private SoundUtils() {
    }

    /**
     * Prints some info.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws LineUnavailableException {
        printAvailableMixers();
    }

    /**
     * Get all mixers that can handle at least two lines of signed PCM, 44kHz,
     * stereo, 16bit, little endian output.
     */
    public static void printAvailableMixers() throws LineUnavailableException {
        // audio format, standard high quality, stereo output
        AudioFormat requestedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        // an output data line
        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, requestedFormat);

        // get infos about all mixers in the system
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        // ask how many lines there are, and plot vendor for each on with at least 2 lines
        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            // how many lines of the type we need are available
            int maxLines = mixer.getMaxLines(lineInfo);
            // at least two or not specified, print info
            if (maxLines == AudioSystem.NOT_SPECIFIED || maxLines >= 2) {
                System.out.println("Description: " + mixerInfo.getDescription() + " Vendor: " + mixerInfo.getVendor() + " Name: " + mixerInfo.getName());
                try (SourceDataLine line = (SourceDataLine) mixer.getLine(lineInfo)) {
                    line.open();
                    for (Control control : line.getControls()) {
                        System.out.println("Line has control: " + control);
                    }
                }
            }
        }
    }
    private static final Logger LOG = Logger.getLogger(SoundUtils.class.getName());
}
