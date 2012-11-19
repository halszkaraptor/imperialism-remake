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
package org.iremake.client.sound;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 * Controling the Sound players.
 */
public class SoundController {

    private static final Logger LOG = Logger.getLogger(SoundController.class.getName());
    public static final int channels = 2;
    public static final int rate = 44100;

    /**
     * Private Constructor avoids instantiation.
     */
    private SoundController() {
    }

    /**
     *
     */
    public static void initSoundSystem() {

        // the audio format we would like to have
        AudioFormat requestedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) rate, 16, channels, 4, 44100, false);
        // AudioFormat audioFormat = new AudioFormat((float) rate, 16, channels, true, // PCM_Signed false // littleEndian );

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, requestedFormat);
        // DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);

        // is this kind of line supported?
        if (!AudioSystem.isLineSupported(info)) {
            LOG.log(Level.INFO, "Line {0} not supported.", info);
            return;
        }

        // Open the first mixer that is presented in the audio list.
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixinfo : mixers) {
            Mixer mix = AudioSystem.getMixer(mixinfo);
            LOG.info(mixinfo.getDescription());
            LOG.log(Level.INFO, "\tMixer has approx. {0} lines available.", mix.getMaxLines(info));
        }
        if (mixers.length == 0) {
            LOG.info("No audio system available; disabling audio.");
            return;
        }

        // finally choose the
        Mixer mix = AudioSystem.getMixer(mixers[0]);
        try {
            // Now try to obtain exactly four data lines. If not possible, reduce functionality.
            mix.open();
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        for (Sound sound : Sound.values()) {
            try {
                SourceDataLine line = (SourceDataLine) mix.getLine(info);
                sound.createPlayer(line);
            } catch (LineUnavailableException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        // set standard values for the volume; it should be the same for all lines.
        //if (backgroundPlayer != null) {
        //FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        //Float volume = new Float( 100 * (volumeControl.getValue() - volumeControl.getMinimum()) / (volumeControl.getMaximum() - volumeControl.getMinimum()) );
        //volume.intValue();
    }
}
