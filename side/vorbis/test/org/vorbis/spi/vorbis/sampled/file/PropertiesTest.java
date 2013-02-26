/*
 * Copyright (C) 2005 Javazoom
 *               2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vorbis.spi.vorbis.sampled.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.junit.Assert;
import org.junit.Test;
import org.sound.sampled.TAudioFormat;
import org.sound.sampled.file.TAudioFileFormat;

/**
 * PropertiesContainer unit test. It matches test.ogg properties to
 * test.ogg.properties expected results. As we don't ship test.ogg, you have to
 * generate your own test.ogg.properties Uncomment out = System.out; in setUp()
 * method to generated it on stdout from your own Ogg Vorbis file.
 */
public class PropertiesTest {

    private String filename = null;
    private String fileurl = null;

    /**
     *
     * @throws IOException
     */
    public PropertiesTest() throws IOException {

        Properties props = new Properties();
        props.load(new FileInputStream("spi.test.properties"));
        filename = (String) props.getProperty("filename");
        fileurl = (String) props.getProperty("stream");
    }

    /**
     *
     */
    @Test
    public void testPropertiesFile() {
        String[] testPropsAFF = {"duration", "title", "author", "album", "date", "comment",
            "copyright", "ogg.bitrate.min", "ogg.bitrate.nominal", "ogg.bitrate.max"};
        String[] testPropsAF = {"vbr", "bitrate"};

        File file = new File(filename);
        try {
            AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat baseFormat = baseFileFormat.getFormat();
            System.out.println("-> Filename : " + filename + " <-");
            System.out.println(baseFileFormat);
            if (baseFileFormat instanceof TAudioFileFormat) {
                Map properties = ((TAudioFileFormat) baseFileFormat).properties();
                System.out.println(properties);
                for (int i = 0; i < testPropsAFF.length; i++) {
                    String key = testPropsAFF[i];
                    if (properties.get(key) != null) {
                        String val = (properties.get(key)).toString();
                        System.out.println(key + "=" + val);
                    }
                }
            } else {
                Assert.fail("testPropertiesFile : TAudioFileFormat expected");
            }

            if (baseFormat instanceof TAudioFormat) {
                Map properties = ((TAudioFormat) baseFormat).properties();
                for (int i = 0; i < testPropsAF.length; i++) {
                    String key = testPropsAF[i];
                    if (properties.get(key) != null) {
                        String val = (properties.get(key)).toString();
                        System.out.println(key + "=" + val);
                    }
                }
            } else {
                Assert.fail("testPropertiesFile : TAudioFormat expected");
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            Assert.fail("testPropertiesFile : " + e.getMessage());
        }
    }

    /**
     *
     */
    @Test
    public void testPropertiesURL() {
        String[] testPropsAFF = {"duration", "title", "author", "album", "date", "comment",
            "copyright", "ogg.bitrate.min", "ogg.bitrate.nominal", "ogg.bitrate.max"};
        String[] testPropsAF = {"vbr", "bitrate"};
        try {
            URL url = new URL(fileurl);
            AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(url);
            AudioFormat baseFormat = baseFileFormat.getFormat();
            System.out.println("-> URL : " + fileurl + " <-");
            System.out.println(baseFileFormat);
            if (baseFileFormat instanceof TAudioFileFormat) {
                Map properties = ((TAudioFileFormat) baseFileFormat).properties();
                System.out.println(properties);
                for (int i = 0; i < testPropsAFF.length; i++) {
                    String key = testPropsAFF[i];
                    if (properties.get(key) != null) {
                        String val = (properties.get(key)).toString();
                        System.out.println(key + "=" + val);
                    }
                }
            } else {
                Assert.fail("testPropertiesURL : TAudioFileFormat expected");
            }
            if (baseFormat instanceof TAudioFormat) {
                Map properties = ((TAudioFormat) baseFormat).properties();
                for (int i = 0; i < testPropsAF.length; i++) {
                    String key = testPropsAF[i];
                    if (properties.get(key) != null) {
                        String val = (properties.get(key)).toString();
                        System.out.println(key + "=" + val);
                    }
                }
            } else {
                Assert.fail("testPropertiesURL : TAudioFormat expected");
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            Assert.fail("testPropertiesURL : " + e.getMessage());
        }
    }
    private static final Logger LOG = Logger.getLogger(PropertiesTest.class.getName());
}
