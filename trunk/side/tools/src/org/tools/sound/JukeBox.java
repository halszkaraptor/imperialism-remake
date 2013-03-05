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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.tools.io.Resource;

/**
 * A jukebox device controlling a stream player and being able to play a number
 * of pieces in a consecutive way. It listens to the stream player and whenever
 * it is finished with a piece, we tell it to play another one.
 *
 * Internally the jukebox listens to the stream player, so it knows when a song
 * is over.
 *
 * For controlling the volume, the stream player is also given to the outside
 * world.
 */
public class JukeBox implements PlayEventListener {

    /* The logger */
    private static final Logger LOG = Logger.getLogger(JukeBox.class.getName());
    /* After playing all pieces in the list, should we rewind and continue playing? */
    private volatile boolean autoRewind = false;
    /* After finishing one piece, should the next be played? */
    private volatile boolean autoContinue = true;
    /* A list of resources we might be played (album) */
    private volatile List<Resource> list = new ArrayList<>(0);
    /* Current index of the next to be played piece in the list */
    private volatile int nextItem = 0;
    /* The underlying stream player */
    private final StreamPlayer player;
    /* One can listen to the work of this juke box. */
    private volatile PlayEventListener listener;

    /**
     * Private access, call the static create method instead.
     *
     * @param player The player to be used.
     */
    private JukeBox(StreamPlayer player) {
        this.player = player;
    }

    /**
     * Initializes a new jukebox, given a player.
     *
     * @param player The player to be used.
     * @return A new jukebox.
     */
    public static JukeBox create(StreamPlayer player) {
        JukeBox jukebox = new JukeBox(player);
        player.setSongOverListener(jukebox);
        return jukebox;
    }

    /**
     * @return Returns the player for player specific adjustments (volume, ...).
     */
    public StreamPlayer getPlayer() {
        return player;
    }

    /**
     * @param l A new listener to be set.
     */
    public void setSongBeginListener(PlayEventListener l) {
        listener = l;
    }

    /**
     * Sets a new playlist, definitely stopping current play and reset the next
     * item counter to the first entry in the new list.
     *
     * You can only change the list completely so far.
     *
     * @param list The new play list.
     */
    public void setSongList(List<Resource> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Expect non-null, non-empty list.");
        }
        stop();
        this.list = Collections.unmodifiableList(list);
    }

    /**
     * @return Number of songs in the current playlist
     */
    public int getListSize() {
        return list.size();
    }

    /**
     * @param value If true playing will continue at the beginning of the
     * playlist after reaching the end.
     */
    public void setAutoRewind(boolean value) {
        autoRewind = value;
    }

    /**
     * @return True if auto rewind is set.
     */
    public boolean isAutoRewind() {
        return autoRewind;
    }

    /**
     * @param value If true, playing continues with the next song after a song
     * has finished.
     */
    public void setAutoContinue(boolean value) {
        autoContinue = value;
    }

    /**
     * @return True if auto continue is set.
     */
    public boolean isAutoContinue() {
        return autoContinue;
    }

    /**
     * The player signals the jukebox that a song is over.
     *
     * @param event Description of the event.
     */
    @Override
    public void newEvent(String event) {
        // if we continue to play
        if (autoContinue) {
            if (nextItem < list.size() - 1) {
                // still unplayed songs in the play list, play them
                nextItem++;
                play();
            } else if (autoRewind) {
                // at the end of the playlist, but auto rewind is set, rewind and play
                nextItem = 0;
                play();
            }
        }
    }

    /**
     * Plays the next song in the list. If we have stopped or at the beginning,
     * the first song will be played.
     */
    public void play() {
        play(nextItem);
    }

    /**
     * Immediately jumps to certain song in the playlist and plays it.
     *
     * @param index The index of the new song to play.
     */
    public void play(int index) {
        // check for index within bounds
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException("index out of bounds.");
        }

        // store in the counter
        nextItem = index;

        // if player was playing, stop it immediately
        if (player.isPlaying()) {
            player.stop();
        }

        // get and AudioInputStream from the resource
        AudioFileFormat fmt = null;
        try {
            fmt = AudioSystem.getAudioFileFormat(list.get(index).getInputStream());
        } catch (UnsupportedAudioFileException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        AudioInputStream is = SoundSystem.getAudioInputStream(list.get(index));

        // if we have a listener, tell about it
        if (listener != null) {
            String title = (String) fmt.properties().get("title");
            listener.newEvent("Audio: \"" + title + "\" now playing.");
        }

        // start playing this piece
        player.play(is);
    }

    /**
     * Stops playing.
     */
    public void stop() {
        player.stop();
        nextItem = 0;
    }
}
