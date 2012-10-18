/*
 * Copyright (C) 2012 Trilarion
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
package org.iremake.client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import org.iremake.client.ui.StartScreenBuilder;
import org.tools.Main;
import org.tools.ui.helper.LookAndFeel;

/**
 * Main entry point for client.
 */
// TODO logging into a file
public class StartClient {

    private static final Logger LOG = Logger.getLogger(StartClient.class.getName());

    private StartClient() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // set look and feel
        LookAndFeel.setSystemLookAndFeel();

        installFonts();

        // fire up start frame
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = StartScreenBuilder.makeFrame();
                frame.setVisible(true);
            }
        });

    }

    public static void installFonts() {
        FontUIResource r = (FontUIResource) UIManager.get("Label.font");
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, StartClient.class.getResource("/data/game/artwork/graphics/fonts/Lora-Regular.ttf").openStream());
        } catch (FontFormatException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        font = font.deriveFont(r.getStyle(), r.getSize());
        // font = font.deriveFont(Font.ITALIC, r.getSize()+2);
        UIManager.put("Label.font", new FontUIResource(font));
    }

    public static void shutDown() {
    }
}