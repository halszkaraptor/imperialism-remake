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
package org.iremake.applications;

import icons.Loader;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import net.miginfocom.swing.MigLayout;
import org.tools.ui.utils.BasicFrame;
import org.tools.ui.utils.LookAndFeel;

/**
 * An Ogg file player with graphical user interface.
 */
public class OggPlayerApp {

    public OggPlayerApp(String title) {
        JFrame frame = new BasicFrame(title);

        // icon
        frame.setIconImage(Loader.getAsImage("/icons/app.icon.png"));

        // components
        JLabel label = new JLabel("Song");
        JProgressBar progress = new JProgressBar();
        JButton load = new JButton("Browse");
        JToggleButton play = new JToggleButton("Play");

        // layout
        frame.setLayout(new MigLayout("wrap 2"));
        frame.add(label, "wmin 200, sg a");
        frame.add(load, "sg b");
        frame.add(progress, "sg a");
        frame.add(play, "sg b");


        // pack and set visible
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LookAndFeel.setSystemLookAndFeel();

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OggPlayerApp("Ogg Player Test");
            }
        });
    }
}
