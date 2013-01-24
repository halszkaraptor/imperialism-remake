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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.junit.Test;

/**
 *
 */
public class SoundPlayerTest {

    private static final AudioFormat TargetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

    @Test
    public void StreamingPlayerTest() throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, TargetFormat);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        StreamPlayerControl control = StreamPlayerControl.create(line, "Music-Thread");

        URL url = new URL("http://www.twelvepm.de/vorbis/Agogo.ogg");
        AudioInputStream in = AudioSystem.getAudioInputStream(url);
        AudioInputStream data = AudioSystem.getAudioInputStream(TargetFormat, in);

        control.play(data);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SoundPlayerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
