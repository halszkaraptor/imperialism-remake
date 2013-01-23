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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 */
public class BackgroundPlayer implements Runnable {

    private static final Logger LOG = Logger.getLogger(BackgroundPlayer.class.getName());
    private static Thread thread;

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void start(SourceDataLine line) {
        if (line == null) {
            LOG.log(Level.SEVERE, "Given line cannot be null.");
            return;
        }
        if (!line.isOpen()) {
            try {
                line.open();
            } catch (LineUnavailableException ex) {
                LOG.log(Level.SEVERE, null, ex);
                return;
            }
        }
        thread = new Thread();

    }

    public static void stop() {

    }

}
