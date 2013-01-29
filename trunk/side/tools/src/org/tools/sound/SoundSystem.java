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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.tools.io.Resource;

/**
 *
 */
public class SoundSystem {

    private static final Logger LOG = Logger.getLogger(SoundSystem.class.getName());
    private static final AudioFormat TargetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    private static final DataLine.Info TargetLineInfo = new DataLine.Info(SourceDataLine.class, TargetFormat);
    private static final int NumberOfLines = 2;

    private static List<Mixer> availableMixers = new LinkedList<>();
    private static Mixer activeMixer;

    /**
     * No instantiation.
     */
    private SoundSystem() {
    }

    /**
     * Gets a new line from the active mixer if there is one.
     *
     * @return
     */
    public static SourceDataLine getLine() {
        if (activeMixer != null) {
            try {
                SourceDataLine line = (SourceDataLine) activeMixer.getLine(TargetLineInfo);
                line.open();
                return line;
            } catch (LineUnavailableException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static AudioInputStream getAudioInputStream(InputStream is) {

        AudioInputStream in;
        try {
            in = AudioSystem.getAudioInputStream(is);
        } catch (UnsupportedAudioFileException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
        return AudioSystem.getAudioInputStream(TargetFormat, in);
    }

    /**
     * Gets the names of the available mixers.
     *
     * @return
     */
    public static List<String> getAvailableMixerNames() {
        List<String> names = new ArrayList<>(availableMixers.size());
        for (Mixer mixer : availableMixers) {
            names.add(mixer.getMixerInfo().getName());
        }
        return Collections.unmodifiableList(names);
    }

    public static String getActiveMixerName() {
        if (activeMixer != null) {
            return activeMixer.getMixerInfo().getName();
        }
        return null;
    }

    public static boolean hasActiveMixer() {
        return activeMixer != null;
    }

    /**
     * Should only be called once.
     */
    public static void setup() {
        // get info about available mixers
        availableMixers.clear();
        // get infos about mixers
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        // ask how many lines there are, and plot vendor for each on with at least 2 lines
        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            int maxLines = mixer.getMaxLines(TargetLineInfo);
            if (maxLines == AudioSystem.NOT_SPECIFIED || maxLines >= NumberOfLines) {
                availableMixers.add(mixer);
            }
        }

        // if availableMixers is still empty, requestedFormat and line are not supported
        if (availableMixers.isEmpty()) {
            // TODO log and disable sound
        } else {
            activeMixer = availableMixers.get(0);
        }

    }

    public static void cleanup() {
        if (activeMixer != null) {
            if (activeMixer.isOpen()) {
                activeMixer.close();
            }
            activeMixer = null;
        }
    }

    public static void setMixer(String name) {
        cleanup();

        for (Mixer mixer : availableMixers) {
            if (mixer.getMixerInfo().getName().equals(name)) {
                activeMixer = mixer;
                break;
            }
        }
    }

    private static void activate() {
        try {
            activeMixer.open();
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
            activeMixer = null;
        }
    }

    /**
     * Helper
     *
     * @param resource
     * @return
     */
    public static AudioInputStream getAudioInputStream(Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be null.");
        }
        try {
            // first get, then convert to TargetFormat
            AudioInputStream in = AudioSystem.getAudioInputStream(resource.getInputStream());
            return AudioSystem.getAudioInputStream(TargetFormat, in);
        } catch (UnsupportedAudioFileException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
