/*
 * Copyright (C) 2010 Trilarion
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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 * Controlling the sound players.
 */
public class SoundController {

    private static final Logger LOG = Logger.getLogger(SoundController.class.getName());

    /* for playbeck we use two channels (stereo) */
    public static final int channels = 2;
    /* and 44 kHz */
    public static final int rate = 44100;

    /**
     * No instantiation.
     */
    private SoundController() {
    }

    /**
     *
     */
    public static boolean initSoundSystem() {

        // the audio format we would like to have
        AudioFormat requestedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, channels, 4, rate, false);
        // little endian byte order

        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, requestedFormat);

        // get infos about mixers
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        // ask how many lines there are
        for (Mixer.Info mixerInfo: mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            int maxLines = mixer.getMaxLines(lineInfo);
            if (maxLines == AudioSystem.NOT_SPECIFIED || maxLines >= 4) {
                try {
                    mixer.open();
                    for (SoundChannel channel: SoundChannel.values()) {
                        SourceDataLine line = (SourceDataLine) mixer.getLine(lineInfo);
                        channel.createPlayer(line);
                    }
                    break;
                } catch (LineUnavailableException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        }


        // set standard values for the volume; it should be the same for all lines.
        //if (backgroundPlayer != null) {
        //FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        //Float volume = new Float( 100 * (volumeControl.getValue() - volumeControl.getMinimum()) / (volumeControl.getMaximum() - volumeControl.getMinimum()) );
        //volume.intValue();

        return true;
    }
}
