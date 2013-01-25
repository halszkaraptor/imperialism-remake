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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.junit.Test;
import org.tools.io.Resource;
import org.tools.io.URLResource;

/**
 *
 */
public class SoundPlayerTest {

    private static final AudioFormat TargetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

    @Test
    public void PlayListTest() throws LineUnavailableException, InterruptedException {

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, TargetFormat);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        JukeBox jukebox = JukeBox.create(line, "Music");

        List<Resource> list = new LinkedList<>();
        list.add(new URLResource("http://www.twelvepm.de/vorbis/Agogo.ogg"));
        jukebox.setSongList(list);

        jukebox.play();

        Thread.sleep(3000);
    }

    // @Test
    public void StreamingPlayerTest() throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, TargetFormat);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        StreamPlayer player = StreamPlayer.create(line, "Music-Thread");

        URL url = new URL("http://www.twelvepm.de/vorbis/Agogo.ogg");
        AudioInputStream in = AudioSystem.getAudioInputStream(url);
        AudioInputStream data = AudioSystem.getAudioInputStream(TargetFormat, in);

        player.setDefaultVolume(0.8f);
        player.setDefaultFadingTime(6000);
        player.play(data);
        Thread.sleep(6000);

        /*
         Thread.sleep(2000);
         player.pause();
         // player.mute(true);
         Thread.sleep(2000);
         // player.mute(false);
         player.resume();
         Thread.sleep(2000);
         player.stop();
         Thread.sleep(3000);
         */
    }
}
