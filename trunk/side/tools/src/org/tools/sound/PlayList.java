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
import java.util.List;
import java.util.Map.Entry;
import org.tools.io.Resource;

/**
 *
 */
public class PlayList {

    private boolean autoRewind = false;
    private boolean autoContinue = true;
    private int fadeTime = 1000;
    private float volume = 0.8f;
    private List<Entry<String, Resource>> list = new ArrayList<>();
    private StreamPlayerControl player;

    public PlayList(StreamPlayerControl player) {
        this.player = player;
    }

    /**
     * Definitely stops playing.
     *
     * You can only change the list completely so far.
     */
    public void changeList(List<Entry<String, Resource>> list) {

    }

    /**
     * Sets whether after playing the whole play list we should start from the beginning.
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
     *
     * @param time
     */
    public void setFadeTime(int time) {

    }

    /**
     *
     * @return
     */
    public int getFadeTime() {
        return fadeTime;
    }

    /**
     * Plays the next song in the list. If we have stopped or at the beginning,
     * the first song will be played.
     */
    public void play() {
    }

    /**
     * Immediately jumps to another song in the index.
     *
     * @param index
     */
    public void play(int index) {
    }

    /**
     * Immediately plays another song.
     *
     * @param title
     */
    public void play(String title) {
    }

    /**
     * Pauses playing.
     */
    public void pause() {
    }

    /**
     * Resumes playing.
     */
    public void resume() {
    }

    public void setVolume(float volume) {
        // TODO checks
        this.volume = volume;

    }

    public float getVolume() {
        return volume;
    }

    /**
     * Stops playing.
     */
    public void stop() {
    }
}
