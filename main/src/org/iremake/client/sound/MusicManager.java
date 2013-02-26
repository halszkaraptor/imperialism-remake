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
import org.tools.sound.JukeBox;
import org.tools.sound.SoundSystem;
import org.tools.sound.StreamPlayer;

/**
 *
 */
public class MusicManager {

    private static JukeBox jukebox;

    /**
     * No instantiation.
     */
    private MusicManager() {
    }

    /**
     *
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
            /*
            jukebox.setSongBeginListener(new PlayEventListener() {
                @Override
                public void newEvent(String event) {
                    FrameManager.getInstance().scheduleInfoMessage(event, true);
                }
            }); */
        }

        // load music list
        if (jukebox != null) {
            MusicDatabase database = new MusicDatabase();
            IOManager.setFromXML(Places.Music, "music.xml", database);
            jukebox.setSongList(database.getBackgroundMusicList());
        }

    }

    /**
     *
     */
    public static void start() {
        if (jukebox != null) {
            // start playback
            jukebox.play();
        }
    }

    /**
     *
     */
    public static void cleanup() {
        if (jukebox != null) {
            jukebox.stop();
            StreamPlayer player = jukebox.getPlayer();
            player.destroy();
            jukebox = null;
        }
        SoundSystem.cleanup();
    }
    private static final Logger LOG = Logger.getLogger(MusicManager.class.getName());
}
