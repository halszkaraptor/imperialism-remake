/*
 * Copyright (C) 2012 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either Version 3 of the License, or
 * (at your option) any later Version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.iremake.client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.client.network.ClientManager;
import org.iremake.client.sound.MusicManager;
import org.iremake.client.ui.FrameManager;
import org.iremake.client.ui.StartScreen;
import org.iremake.client.ui.UIFrame;
import org.iremake.common.Settings;
import org.iremake.server.network.ServerManager;
import org.tools.io.ResourceUtils;
import org.tools.ui.utils.LookAndFeel;

/**
 * Main entry point for client.
 */
// TODO too many things are done in the EDT, check this and put stuff in WorkerThreads
public class StartClient {

    private static final Logger LOG = Logger.getLogger(StartClient.class.getName());
    public static final ActionListener ExitAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            StartClient.shutDown();
        }
    };

    /**
     * No instantiation.
     */
    private StartClient() {
    }

    /**
     * Main entry point for the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // first things first: logger
            setupLogger();

            // set look and feel
            LookAndFeel.setSystemLookAndFeel();

            // install font
            installFonts();

            // load terrain and scenario settings
            Settings.load();

            // load options
            Option.load();

            // music setup
            MusicManager.setup();

            // fire up start frame
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    FrameManager.getInstance().initialize();
                    UIFrame screen = new StartScreen();
                    screen.switchTo();
                    // do not want the music to start before the screen
                    if (Option.Music_Mute.getBoolean() == false) {
                        MusicManager.start();
                    }
                }
            });
        } catch (Exception ex) {
            // every possible exception is caught and logged
            // and that's it, please contact support in this case
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Install Fonts
     */
    public static void installFonts() {
        FontUIResource r = (FontUIResource) UIManager.get("Label.font");
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, IOManager.getAsInputStream(Places.Graphics, "fonts/Lora-Regular.ttf"));
        } catch (FontFormatException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        font = font.deriveFont(r.getStyle(), r.getSize());
        // font = font.deriveFont(Font.ITALIC, r.getSize()+2);
        UIManager.put("Label.font", new FontUIResource(font));
    }

    /**
     * Shut down clean up.
     */
    public static void shutDown() {

        // shutdown sound
        MusicManager.cleanup();

        // dispose of the screen frame
        FrameManager.dispose();

        // if client is still running disconnect

        if (ClientManager.NETWORK.isRunning()) {
            LOG.log(Level.INFO, "Client still running, shut down.");
            ClientManager.NETWORK.stop();
        }

        // if server is still running shut down
        if (ServerManager.NETWORK.isRunning()) {
            LOG.log(Level.INFO, "Server still running, shut down.");
            ServerManager.NETWORK.stop();
        }

        // save options
        Option.save();

        // save statistics from loader to simple text file
        List<String> stats = IOManager.getStatistics();
        try {
            ResourceUtils.writeText(stats, IOManager.getAsResource(Places.Log, "file_usage.txt"));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Setup logger
     */
    private static void setupLogger() throws IOException {

        IOManager.createDirectory(Places.Log);
        // if log directory not yet existing, create it

        // setup of the logger
        Handler handler = new FileHandler(IOManager.getPath(Places.Log, "remake%g.log"), (int) 1e5, 10, false);
        handler.setFormatter(new SimpleFormatter()); // TODO is using the default (system specific) a good way, set by command line, from a file?
        handler.setLevel(Level.INFO);
        Logger.getGlobal().addHandler(handler);

        // our first log message (just to get the date and time)
        LOG.log(Level.INFO, "Logger is setup");
    }
}