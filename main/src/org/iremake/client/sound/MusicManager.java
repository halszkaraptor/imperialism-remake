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
package org.iremake.client.sound;

import java.util.logging.Logger;
import javax.sound.sampled.SourceDataLine;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.client.ui.FrameManager;
import org.tools.sound.JukeBox;
import org.tools.sound.PlayEventListener;
import org.tools.sound.SoundSystem;
import org.tools.sound.StreamPlayer;

/**
 * Initializes the sound system and controls a jukebox.
 */
public class MusicManager {

    private static final Logger LOG = Logger.getLogger(MusicManager.class.getName());
    private static JukeBox jukebox;

    /**
     * No instantiation.
     */
    private MusicManager() {
    }

    /**
     * Please only call once.
     */
    public static void setup() {
        // sound system setup
        SoundSystem.setup();

        // get a line and create the jukebox
        if (SoundSystem.hasActiveMixer()) {
            SourceDataLine line = SoundSystem.getLine();
            StreamPlayer player = StreamPlayer.create(line, "Music");
            jukebox = JukeBox.create(player);
            jukebox.setAutoRewind(true);
            jukebox.setSongBeginListener(new PlayEventListener() {
                @Override
                public void newEvent(String event) {
                    FrameManager.getInstance().scheduleInfoMessage(event, false);
                }
            });
        }

        // load music list
        if (jukebox != null) {
            MusicDatabase database = new MusicDatabase();
            IOManager.setFromXML(Places.Music, "music.xml", database);
            jukebox.setSongList(database.getBackgroundMusicList());
        }

    }

    /**
     * If there is a jukebox, start it.
     */
    public static void start() {
        if (jukebox != null) {
            // start playback
            jukebox.play();
        }
    }

    /**
     * Stop any music playing. Used when in mute mode.
     */
    public static void stop() {
        if (jukebox != null) {
            jukebox.stop();
        }
    }

    /**
     * Must be called at the end to end the playing threads and the sound mixer
     * resources.
     */
    public static void cleanup() {
        stop();
        if (jukebox != null) {
            StreamPlayer player = jukebox.getPlayer();
            player.destroy();
            jukebox = null;
        }
        SoundSystem.clearMixer();
    }
}
