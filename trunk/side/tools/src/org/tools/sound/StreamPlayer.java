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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;

/**
 *
 */
public class StreamPlayer implements Runnable {

    private static final Logger LOG = Logger.getLogger(StreamPlayer.class.getName());

    private final byte[] buffer = new byte[4096];
    private final SourceDataLine line;

    private AudioInputStream stream;

    private boolean exit = false;

    public StreamPlayer(SourceDataLine line) {
        this.line = line;
    }

    public void exit() {
        exit = true;
    }

    public void play(AudioInputStream data) {
        synchronized (stream) {
            if (stream == null) {
                stream = data;
            }
        }
    }


    @Override
    public void run() {
        // a loop until we are signalled to die
        while (!exit) {

            // there is a new song, we play it
            if (stream != null) {

                line.start();
                int nBytesRead = 0, nBytesWritten = 0;

                while (nBytesRead != -1) {
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
                    // TODO is stop is wanted we should do it here
                }
                line.drain();
                line.stop();

                stream = null;
            }
        }
        line.close();
    }
}
