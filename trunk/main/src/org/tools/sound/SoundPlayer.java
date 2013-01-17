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

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.tools.io.Resource;

/**
 * Elementary class for playing music.
 *
 * <p> This class is given a line (== input to a sound mixer), and it can then
 * send sound streams to be fed to this line. Additionally, the playback can
 * also be paused, resumed, and stopped. Specific classes that play, e.g.,
 * through a playlist, are derived from this player. </p>
 *
 * Only the run() method is synchronized since all other methods are probably
 * only called by the main thread. However be careful. This might be made more
 * safe in the future.
 */
// TODO multi-threading implemented right?
public class SoundPlayer implements Runnable {

    private static final Logger LOG = Logger.getLogger(SoundPlayer.class.getName());
    // private OggPlayer oggPlayer;
    private Thread me;
    private volatile List<Resource> playlist = new LinkedList<>();
    private volatile boolean autoRepeat;
    private volatile int playingNext = -1;
    private PlayStatus status = PlayStatus.Stopped;
    private FloatControl volumeControl;
    private BooleanControl muteControl;

    /**
     * Sets up a new player.
     *
     * @param line the data input line to the mixer.
     * @throws RuntimeException if the data input line cannot be opened.
     */
    SoundPlayer(SourceDataLine line, String threadName) {
        // oggPlayer = new OggPlayer(line, status);
        try {
            line.open();
            line.start();
        } catch (LineUnavailableException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        if (!volumeControl.getUnits().equals("dB")) {
            LOG.log(Level.SEVERE, "Sound system volume control has unknow units!");
        }

        muteControl = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
        // create new thread and start it
        me = new Thread(this, threadName);
        me.start();
    }

    /**
     *
     * @param is
     */
    private void action(Resource r) {
        /*
        try {
            //oggPlayer.play(r.getInputStream());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }*/
    }

    /**
     *
     */
    @Override
    public synchronized void run() {
        while (true) {
            if (PlayStatus.Stopped.equals(status)) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                }
            } else {
                if (playingNext == -1) {
                    playingNext = 0;
                }

                action(playlist.get(playingNext));

                if (playlist.size() > playingNext + 1) {
                    playingNext++;
                } else if (autoRepeat) {
                    playingNext = 0;
                } else {
                    stop();
                }
            }
        }
    }

    /**
     *
     */
    public void play() {
        status = PlayStatus.Playing;
        me.interrupt();
    }

    /**
     *
     * @param index
     */
    public void play(int index) {
        if (index > playlist.size()) {
            throw new IllegalArgumentException("");
        }
        playingNext = index;
        play();
    }

    /**
     *
     */
    public void stop() {
        playingNext = -1;
        status = PlayStatus.Stopped;
    }

    /**
     *
     * @param wantPause
     */
    public void pause() {
        status = PlayStatus.Paused;
    }

    /**
     *
     * @param autoRepeat
     */
    public void setRewind(boolean autoRepeat) {
        this.autoRepeat = autoRepeat;
    }

    /**
     *
     */
    public void clearPlaylist() {
        playlist.clear();
        stop();
    }

    /**
     *
     * @return
     */
    public int getPlaylistCount() {
        return playlist.size();
    }

    /**
     *
     * @param list
     */
    public void setPlaylist(List<Resource> list) {
        playlist = list;
        stop();
    }

    /**
     *
     * @param item
     */
    public void addToPlaylist(Resource item) {
        playlist.add(item);
    }

    /**
     *
     * @param item
     */
    public void play(Resource item) {
        clearPlaylist();
        addToPlaylist(item);
        play();
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
     * @param volume
     */
    public void setVolume(float volume) {
        if (volume < volumeControl.getMinimum() || volume > volumeControl.getMaximum()) {
            throw new IllegalArgumentException("volume out of possible range");
        }
        volumeControl.setValue(volume);
        mute(false); // setting a volume always unchecks mute
    }

    /**
     *
     * @return
     */
    public float getVolume() {
        return volumeControl.getValue();
    }

    /**
     *
     * @param volume
     * @param milliseconds
     */
    public void fade(float volume, int milliseconds) {
        float now = volumeControl.getValue();
        volumeControl.shift(now, volume, milliseconds * 1000); // shift even uses microseconds
    }

    /**
     * Convenience function, however does not start automatically.
     */
    public void fadeIn() {
        fade(0, 2000);
    }

    /**
     * Convenience function, however does not stop automatically.
     */
    public void fadeOut() {
        fade(volumeControl.getMinimum(), 2000);
    }
}
