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
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
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
    private final float volumeRange;
    private final float minimumVolume;
    private volatile float defaultVolume = 0.8f;
    private volatile int defaultFadingTime = 0;
    private volatile boolean stop;
    private volatile boolean pause;
    private volatile AudioInputStream stream;
    private volatile boolean exit;
    private volatile SongOverListener listener;

    /**
     *
     * @param line
     */
    private StreamPlayer(SourceDataLine line) {
        // TODO insert code that checks if the controls are available, otherwise disable functionality
        muteControl = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
        volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        minimumVolume = Math.max(volumeControl.getMinimum(), -40); // not less than -30dB
        volumeRange = volumeControl.getMaximum() - minimumVolume;
        setVolume();

        this.line = line;
    }

    /**
     *
     * @param line
     * @param name
     * @return
     * @throws LineUnavailableException
     */
    public static StreamPlayer create(SourceDataLine line, String name) throws LineUnavailableException {
        if (!line.isOpen()) {
            line.open();
        }
        StreamPlayer player = new StreamPlayer(line);
        new Thread(player, name).start();
        return player;
    }

    /**
     *
     */
    public void destroy() {
        stop();

        lock.lock();
        try {
            exit = true;
            actionCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     *
     * @param data
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
     *
     * @return
     */
    public boolean isPlaying() {
        return stream != null;
    }

    /**
     *
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

    public boolean isPausing() {
        return pause;
    }

    /**
     *
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
     *
     */
    public void stop() {
        lock.lock();
        try {
            if (stream != null) {
                stop = true;
            }
        } finally {
            lock.unlock();
        }
        resume();
    }

    public void setListener(SongOverListener l) {
        lock.lock();
        try {
            listener = l;
        } finally {
            lock.unlock();
        }
    }

    /**
     *
     */
    @Override
    public void run() {
        while (!exit) {

            lock.lock();
            try {
                while (!exit && stream == null) {
                    actionCondition.await();
                }
            } catch (InterruptedException ex) {
            } finally {
                lock.unlock();
            }

            // something happened, if it is a new song, tell it
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
                        System.out.println("Bytes read " + nBytesRead);
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


            line.drain();
            line.stop();

            stream = null;
            if (listener != null) {
                listener.completedSong();
            }
        }

        line.close();
    }

    /**
     *
     * @param mute
     */
    public void mute(boolean mute) {
        muteControl.setValue(mute);
    }

    /**
     *
     */
    private void setVolume() {
        volumeControl.setValue(defaultVolume * volumeRange + minimumVolume);
        mute(false); // setting a volume always unchecks mute
    }

    /**
     *
     * @param volume
     */
    public void setDefaultVolume(float volume) {
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0 and 1: " + volume);
            // TODO throw exception
        }
        lock.lock();
        try {
            defaultVolume = volume;
            setVolume();
        } finally {
            lock.unlock();
        }
    }

    /**
     *
     * @return
     */
    public float getDefaultVolume() {
        lock.lock();
        try {
            return defaultVolume;
        } finally {
            lock.unlock();
        }
    }

    /**
     *
     * @param volumeA
     * @param volumeB
     * @param time in milliseconds
     */
    private void fade(float volumeA, float volumeB, int time) {
        volumeControl.shift(volumeA * volumeRange + minimumVolume, volumeB * volumeRange + minimumVolume, time * 1000);
    }

    /**
     *
     * @param time
     */
    public void fadeOut(int time) {
        fade(defaultVolume, 0, time);
    }

    /**
     *
     * @param time
     */
    public void fadeIn(int time) {
        fade(0, defaultVolume, time);
    }

    /**
     *
     * @param time
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
