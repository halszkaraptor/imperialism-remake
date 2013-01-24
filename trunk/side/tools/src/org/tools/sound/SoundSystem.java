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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 *
 */
public class SoundSystem {

    private static final Logger LOG = Logger.getLogger(SoundSystem.class.getName());
    private static final AudioFormat TargetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    private static final DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, TargetFormat);
    private static final int MinLines = 2;
    private static List<Mixer> availableMixers = new LinkedList<>();
    private static Mixer usedMixer;

    /**
     * No instantiation.
     */
    private SoundSystem() {
    }

    /**
     *
     * @return
     */
    public static List<String> getAvailableMixerNames() {
        List<String> names = new ArrayList<>(availableMixers.size());
        for (Mixer mixer : availableMixers) {
            names.add(mixer.getMixerInfo().getName());
        }
        return names;
    }

    /**
     * Should only be called once.
     */
    public static void initialize() {


        // get info about available mixers
        availableMixers.clear();
        // get infos about mixers
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        // ask how many lines there are, and plot vendor for each on with at least 2 lines
        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            int maxLines = mixer.getMaxLines(lineInfo);
            if (maxLines == AudioSystem.NOT_SPECIFIED || maxLines >= MinLines) {
                availableMixers.add(mixer);
            }
        }

        // if availableMixers is still empty, requestedFormat and line are not supported
        if (availableMixers.isEmpty()) {
            // TODO log and disable sound
        }
    }

    public void setMixer(String name) {
        for (Mixer mixer : availableMixers) {
            if (mixer.getMixerInfo().getName().equals(name)) {
                usedMixer = mixer;
                break;
            }
        }
        if (usedMixer != null) {
            try {
                usedMixer.open();
                SourceDataLine line = (SourceDataLine) usedMixer.getLine(lineInfo);
            } catch (LineUnavailableException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        /**
         * FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
         */
    }
}
