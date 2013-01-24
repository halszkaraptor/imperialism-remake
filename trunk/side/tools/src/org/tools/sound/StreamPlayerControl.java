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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 */
public class StreamPlayerControl {

    private enum State {
        PLAYING, PAUSING;
    }
    private State state = State.PLAYING;
    private BooleanControl muteControl;
    private FloatControl volumeControl;
    private float volumeRange;
    private float minimumVolume;
    private static final Logger LOG = Logger.getLogger(StreamPlayerControl.class.getName());
    private StreamPlayer player;

    private StreamPlayerControl(SourceDataLine line) {
        muteControl = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
        volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        minimumVolume = volumeControl.getMinimum();
        volumeRange = volumeControl.getMaximum() - minimumVolume;
        // hopefully the range is never zero!
        player = new StreamPlayer(line);
    }

    /**
     * Starts the player in a new thread.
     *
     * @param name
     */
    private void startup(String name) {
        new Thread(player, name).start();
    }

    public static StreamPlayerControl create(SourceDataLine line, String name) throws LineUnavailableException {
        if (!line.isOpen()) {
            line.open();
        }
        StreamPlayerControl control = new StreamPlayerControl(line);
        control.startup(name);
        return control;
    }

    public void play(AudioInputStream in) {
        state = State.PLAYING;

        player.play(in);
    }

    public void pause() {
        if (state != State.PAUSING) {
            state = State.PAUSING;
            // TODO ...
        }
    }

    public boolean isPausing() {
        return state.equals(State.PAUSING);
    }

    public void resume() {
        if (state == State.PAUSING) {
            state = State.PLAYING;
        }
    }

    public void mute(boolean mute) {
        muteControl.setValue(mute);
    }

    public void setVolume(float volume) {
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0 and 1: " + volume);
            // TODO throw exception
        }
        volumeControl.setValue(volume * volumeRange + minimumVolume);
        mute(false); // setting a volume always unchecks mute
    }

    public float getVolume() {
        return (volumeControl.getValue() - minimumVolume) / volumeRange;
    }

    /**
     *
     * @param volume normalized
     * @param time in milliseconds
     */
    public void fade(float volume, int time) {
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0 and 1: " + volume);
            // TODO throw exception
        }
        volumeControl.shift(volumeControl.getValue(), volume * volumeRange + minimumVolume, time * 1000);
    }

    /**
     * However does not stop any playback.
     *
     * @param time
     */
    public void fadeOut(int time) {
        fade(minimumVolume, time);
    }

    /**
     * Should only be called once.
     */
    public void shutDown() {
        player.exit();
    }
}
