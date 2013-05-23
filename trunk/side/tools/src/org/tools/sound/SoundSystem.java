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
 * General information about the sound system. Obtaining of suitable lines.
 * Choosing of the right mixer.
 */
public class SoundSystem {

    /* Logger */
    private static final Logger LOG = Logger.getLogger(SoundSystem.class.getName());
    /* Target format for feeding to the audio system */
    private static final AudioFormat TargetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    /* SourceDataLine Info for the target format */
    private static final DataLine.Info TargetLineInfo = new DataLine.Info(SourceDataLine.class, TargetFormat);
    /* Usually we want two lines (one background, one effects) */
    private static final int NumberOfLines = 2;
    /* Several mixers could be able to deliver our lines, we store them here */
    private static List<Mixer> availableMixers = new LinkedList<>();
    /* There will be only one active mixer, which is used for obtaining lines */
    private static Mixer activeMixer;

    /**
     * No instantiation.
     */
    private SoundSystem() {
    }

    /**
     * Gets a new line from the active mixer if there is one. It is returned
     * open.
     *
     * @return A new SourceDataLine or null if none is available
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

    /**
     * Gets an AudioInputStream in the TargetFormat from an InputStream by using
     * the internal AudioSystem capabilities.
     *
     * @param is The InputStream
     * @return The AudioInputStream
     */
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
     * Convenience method. Instead of a InputStream, obtains the
     * AudioInputStream from a Resource.
     *
     * @param resource The Resource
     * @return The AudioInputStream
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

    /**
     * Gets all names of the available mixers.
     *
     * @return A list of names
     */
    public static List<String> getAvailableMixerNames() {
        List<String> names = new ArrayList<>(availableMixers.size());
        for (Mixer mixer : availableMixers) {
            names.add(mixer.getMixerInfo().getName());
        }
        return Collections.unmodifiableList(names);
    }

    /**
     * Gets the name of the active mixer or null if there is none.
     *
     * @return The name
     */
    public static String getActiveMixerName() {
        if (activeMixer != null) {
            return activeMixer.getMixerInfo().getName();
        }
        return null;
    }

    /**
     * @return True if active mixer is set.
     */
    public static boolean hasActiveMixer() {
        return activeMixer != null;
    }

    /**
     * Should only be called once!
     *
     * Queries all the system mixers and write into the list of available
     * mixers. Sets the active mixer to the first mixer in this list. Only mixer
     * capable of delivering NumberOfLines lines in the target format are
     * regarded.
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

    /**
     * Closes the active mixer and releases resources.
     */
    public static void clearMixer() {
        if (activeMixer != null) {
            if (activeMixer.isOpen()) {
                activeMixer.close();
            }
            activeMixer = null;
        }
    }

    /**
     * Sets the active mixer. Opens it.
     *
     * @param name One of the outputs of getAvailableMixernames()
     */
    public static void setMixer(String name) {
        clearMixer();

        for (Mixer mixer : availableMixers) {
            if (mixer.getMixerInfo().getName().equals(name)) {
                activeMixer = mixer;
                try {
                    activeMixer.open();
                } catch (LineUnavailableException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    activeMixer = null;
                }
                break;
            }
        }
    }
}
