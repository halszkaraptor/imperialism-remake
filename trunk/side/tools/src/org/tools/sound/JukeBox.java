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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.tools.io.Resource;

/**
 *
 */
public class JukeBox implements Runnable {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition actionCondition = lock.newCondition();
    private volatile boolean exit;
    private boolean autoRewind = false;
    private boolean autoContinue = true;
    private List<Resource> list = new ArrayList<>();
    private int nextItem = 0;
    private StreamPlayer player;

    public JukeBox(StreamPlayer player) {
        this.player = player;
    }

    public static JukeBox create(SourceDataLine line, String name) throws LineUnavailableException {
        StreamPlayer player = StreamPlayer.create(line, name + " player");
        JukeBox jukebox = new JukeBox(player);
        new Thread(jukebox, name + " jukebox").start();
        return jukebox;
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

    public StreamPlayer getPlayer() {
        return player;
    }

    /**
     * Definitely stops playing.
     *
     * You can only change the list completely so far.
     */
    public void setSongList(List<Resource> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Expect non-null, non-empty list.");
        }
        stop();
        this.list = Collections.unmodifiableList(list);
    }

    /**
     * Sets whether after playing the whole play list we should start from the
     * beginning.
     *
     * @param value
     */
    public void setAutoRewind(boolean value) {
        autoRewind = value;
    }

    /**
     *
     * @return
     */
    public boolean isAutoRewind() {
        return autoRewind;
    }

    /**
     * Sets whether after a song we should continue with the next song.
     *
     * @param value
     */
    public void setAutoContinue(boolean value) {
        autoContinue = value;
    }

    /**
     *
     * @return
     */
    public boolean isAutoContinue() {
        return autoContinue;
    }

    /**
     * Plays the next song in the list. If we have stopped or at the beginning,
     * the first song will be played.
     */
    public void play() {
        play(nextItem);
    }

    /**
     * Immediately jumps to another song in the index.
     *
     * @param index
     */
    public void play(int index) {
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException("index out of bounds.");
        }
        AudioInputStream is = SoundSystem.getAudioInputStream(list.get(index));
        if (player.isPlaying()) {
            player.stop();
        }
        player.play(is);

        // increment index
        nextItem = (index + 1) % list.size();
    }

    /**
     * Stops playing.
     */
    public void stop() {
        nextItem = 0;
    }

    @Override
    public void run() {
        while (!exit) {

            lock.lock();
            try {
                actionCondition.await();
            } catch (InterruptedException ex) {
            } finally {
                lock.unlock();
            }

            if (!exit) {
            }
        }
        player.destroy();
    }
}
