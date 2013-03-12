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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * Player device that can feed various AudioInputStream to a SourceDataLine.
 *
 * The playing can be paused, resumed, stopped. Volume can be controlled or a
 * post playing hook can be called.
 *
 * A thread is started to operate this player. The player is thread-safe.
 */
public class StreamPlayer implements Runnable {

    private static final Logger LOG = Logger.getLogger(StreamPlayer.class.getName());
    private final byte[] buffer = new byte[4096];
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition actionCondition = lock.newCondition();
    private final Condition resumeCondition = lock.newCondition();
    private final SourceDataLine line;
    private final BooleanControl muteControl;
    private final FloatControl volumeControl;
    private final float volumeRange = 1;
    private final float minimumVolume = 0;
    private volatile float volume = 0.7f;
    private volatile int defaultFadingTime = 0;
    private volatile boolean stop;
    private volatile boolean pause;
    private volatile AudioInputStream stream;
    private volatile boolean exit;
    private volatile PlayEventListener listener; // for songs ending

    /**
     * Stores the line. Assumes the line is open. Creates some controls.
     *
     * No public instantiation, use static 'create' method instead.
     *
     * @param line The sound line.
     */
    private StreamPlayer(SourceDataLine line) {
        // TODO insert code that checks if the controls are available, otherwise disable functionality
        // muteControl = (BooleanControl) line.getControl(BooleanControl.Type.MUTE); // not available under openjdk 7 icedtea
        muteControl = null;
        // volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl = null;
        /*
        minimumVolume = Math.max(volumeControl.getMinimum(), -40); // not less than -30dB
        volumeRange = volumeControl.getMaximum() - minimumVolume;
        volume();
        */
        this.line = line;
    }

    /**
     * Given a sound line and a name, creates a new StreamPlayer and runs it in
     * a thread.
     *
     * @param line The sound line.
     * @param name The thread name.
     * @return The Player object.
     */
    public static StreamPlayer create(SourceDataLine line, String name) {
        if (!line.isOpen()) {
            throw new IllegalArgumentException("Line must be open");
        }
        StreamPlayer player = new StreamPlayer(line);
        new Thread(player, name).start();
        return player;
    }

    /**
     * Tells the player to stop playing and end the running thread.
     */
    public void destroy() {
        stop();

        lock.lock();
        try {
            exit = true;
            // wake up the thread if it was pausing
            resumeCondition.signal();
            // wake up the thread if had been stopped
            actionCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Starts playing from an AudioInputStream if nothing else is playing
     * currently. Otherwise does nothing.
     *
     * @param data Audio data stream.
     */
    public void play(AudioInputStream data) {
        lock.lock();
        try {
            if (stream == null) {
                stream = data;
                actionCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return True if currently a stream is played.
     */
    public boolean isPlaying() {
        return stream != null;
    }

    /**
     * If the player is playing signal him to pause.
     */
    public void pause() {
        lock.lock();
        try {
            if (stream != null) {
                pause = true;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return True if currently pausing (implies that it can continue playing
     * something).
     */
    public boolean isPausing() {
        return pause;
    }

    /**
     * If it is pausing (was playing, has been paused but not stopped), signal
     * that it should resume playing.
     */
    public void resume() {
        lock.lock();
        try {
            if (stream != null) {
                pause = false;
                resumeCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Stops playing or pausing.
     */
    public void stop() {
        lock.lock();
        try {
            // need to make sure stop is true after this operation
            stop = true;
            if (stream != null) {
                // wake thread up, if it was pausing
                pause = false;                
                resumeCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Set the callback for the end of songs. Set to null if not needed. Is null
     * upon creation.
     *
     * @param l The callback.
     */
    public void setSongOverListener(PlayEventListener l) {
        lock.lock();
        try {
            listener = l;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Loop of the sound player thread.
     */
    @Override
    public void run() {
        // as long as we do not exit
        while (!exit) {

            // wait for the action condition, either playing or exiting
            lock.lock();
            try {
                while (!exit && stream == null) {
                    actionCondition.await();
                }
            } catch (InterruptedException ex) {
            } finally {
                lock.unlock();
            }

            // if we are playing, start playing
            if (!exit && stream != null) {

                line.start();
                stop = false;
                pause = false;
                int nBytesRead = 0, nBytesWritten = 0;

                // start of a song, fade in
                if (defaultFadingTime > 0) {
                    fadeIn(defaultFadingTime);
                }

                while (!stop && nBytesRead != -1) {
                    try {
                        nBytesRead = stream.read(buffer, 0, buffer.length);
                        // System.out.println("Bytes read " + nBytesRead);
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        // TODO exit graciously
                    }
                    if (nBytesRead != -1 && nBytesRead > 0) {
                        nBytesWritten = line.write(buffer, 0, nBytesRead);
                    }
                    // TODO check for line closed, stopped, flushed

                    // we're just pausing
                    if (pause) {
                        lock.lock();
                        try {
                            while (pause && !exit && !stop) {
                                resumeCondition.await();
                            }
                        } catch (InterruptedException ex) {
                        } finally {
                            lock.unlock();
                        }
                        pause = false;
                    }
                    // TODO fading out not yet supported
                }
            }

            // stop the line
            line.drain();
            line.stop();

            stream = null;

            // make callback to listener
            if (!stop && listener != null) {
                listener.newEvent(null);
            }
        }

        // we exit, close the line at the end
        line.close();
    }

    /**
     * Sets the output to mute via a MuteControl.
     *
     * @param mute True if setting to mute.
     */
    public void mute(boolean mute) {
        if (muteControl != null) {
            muteControl.setValue(mute);
        }
    }

    /**
     * Internally setting the volume.
     */
    private void volume() {
        if (volumeControl != null) {
            volumeControl.setValue(volume * volumeRange + minimumVolume);
            mute(false); // setting a volume always unchecks mute
        }
    }

    /**
     * Sets the volume.
     *
     * @param value New volume as number in [0, 1]
     */
    public void setVolume(float value) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("Volume must be between 0 and 1: " + value);
            // TODO throw exception
        }
        lock.lock();
        try {
            volume = value;
            volume();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return The current volume as number in [0, 1]
     */
    public float getVolume() {
        lock.lock();
        try {
            return volume;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Fades from one volume to another within a certain time period.
     *
     * Hint: Often not supported by the underlying driver.
     *
     * @param value1 Initial volume
     * @param value2 Final volume
     * @param time Duration in milliseconds
     */
    private void fade(float value1, float value2, int time) {
        if (volumeControl != null) {
            volumeControl.shift(value1 * volumeRange + minimumVolume, value2 * volumeRange + minimumVolume, time * 1000);
        }
    }

    /**
     * Fades out.
     *
     * @param time Duration in milliseconds.
     */
    public void fadeOut(int time) {
        fade(volume, 0, time);
    }

    /**
     * Fades in.
     *
     * @param time Duration in milliseconds.
     */
    public void fadeIn(int time) {
        fade(0, volume, time);
    }

    /**
     * Sets a default fading time that is used at the begin and before the end
     * of playback.
     *
     * @param time Duration in milliseconds.
     */
    public void setDefaultFadingTime(int time) {
        lock.lock();
        try {
            defaultFadingTime = time;
        } finally {
            lock.unlock();
        }
    }
}
