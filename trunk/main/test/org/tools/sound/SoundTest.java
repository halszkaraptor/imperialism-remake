/*
 * Copyright (C) 2011 Trilarion
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
import org.junit.Test;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;

/**
 * Tests the sound system.
 */
// TODO get test sample and store under test package
public class SoundTest {

    static final Logger LOG = Logger.getLogger(SoundTest.class.getName());

    @Test
    public void ChannelsTest() throws InterruptedException {
        SoundController.initSoundSystem();
        Resource resource = null;
        try {
            resource = ResourceUtils.asResource("data/game/artwork/music/Awakening.ogg");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        int t = 4000;
        SoundChannel.BACKGROUND.getPlayer().play(resource);
        System.out.println("Playing for 4sec");
        Thread.sleep(t);

        SoundChannel.AUX.getPlayer().play(resource);
        System.out.println("Playing for 4sec more together");
        Thread.sleep(t);

        SoundChannel.AUX.getPlayer().stop();
        System.out.println("Playing for 4sec more alone again");
        Thread.sleep(t);

        SoundChannel.BACKGROUND.getPlayer().pause();
        System.out.println("Not playing for 4sec more");
        Thread.sleep(t);

        SoundChannel.BACKGROUND.getPlayer().play();
        System.out.println("Playing for 4sec more end then stopping");
        Thread.sleep(t);

        SoundChannel.BACKGROUND.getPlayer().stop();
        SoundChannel.AUX.getPlayer().play(resource);
        System.out.println("Starting again and setting mute after 4sec");
        System.out.println("Initial volume is: " + SoundChannel.AUX.getPlayer().getVolume());
        Thread.sleep(t);

        SoundChannel.AUX.getPlayer().mute(true);
        System.out.println("Setting to 50% volume after 4sec");
        Thread.sleep(t);

        SoundChannel.AUX.getPlayer().setVolume(-1);
        System.out.println("Setting to 100% volume after 4sec");
        Thread.sleep(t);

        SoundChannel.AUX.getPlayer().setVolume(0);
        System.out.println("Fade out after 4sec and stop");
        Thread.sleep(t);

        SoundChannel.AUX.getPlayer().fade(-6, t);
        Thread.sleep(t);
    }
}
