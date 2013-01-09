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

import javax.sound.sampled.SourceDataLine;

/**
 * The typical 4 channels we have in an application, each with a SoundPlayer.
 */
public enum SoundChannel {

    BACKGROUND("Background"),
    EVENTS("Events"),
    EFFECTS("Effects"),
    AUX("Auxilliary background");

    private String title;
    private SoundPlayer player;

    /**
     *
     * @param title
     */
    private SoundChannel(String title) {
        this.title = title;
    }

    /**
     *
     * @param line
     */
    public void createPlayer(SourceDataLine line) {
        player = new SoundPlayer(line, title);
    }

    /**
     *
     * @return
     */
    public SoundPlayer getPlayer() {
        return player;
    }
}
