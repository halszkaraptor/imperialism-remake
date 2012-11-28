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
package org.iremake.client.ui;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.iremake.client.ui.editor.EditorBuilder;
import org.iremake.client.ui.editor.EditorManager;
import org.iremake.common.ui.utils.GraphicsUtils;

/**
 * Startframe / Mainframe / Editorframe juggling.
 */
public class FrameManager {

    private static FrameManager singleton;

    private JFrame frame;
    private JPanel panel;
    private JComponent content;

    /**
     *
     */
    private FrameManager() {
        setupMainFrame();

    }

    /**
     *
     * @return
     */
    public static FrameManager getInstance() {
        if (singleton == null) {
            singleton = new FrameManager();
        }
        return singleton;
    }

    /**
     *
     */
    private void setupMainFrame() {
        if (frame != null) {
            return;
        }

        frame = new JFrame();

        // undecorated
        frame.setUndecorated(true);

        // no resizing
        frame.setResizable(false);

        // turn of usual exiting mechanisms
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // set title
        frame.setTitle("Imperialism Remake");

        // set app icon
        frame.setIconImage(IOManager.getAsImage(Places.GraphicsIcons, "icon.app.png"));

        // set to bounds of the screen
        GraphicsUtils.setFullScreen(frame);

        // create panel with border
        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        panel.setOpaque(false);
        // set layoutmanager
        panel.setLayout(new MigLayout("fill, gap 0, insets 0"));

        // add panel to frame
        frame.add(panel);

        // set it visible
        frame.setVisible(true);
    }

    /**
     *
     */
    public void switchToStartScreen() {

        if (content != null) {
            panel.remove(content);
        }
        content = StartScreenBuilder.build();
        panel.add(content, "grow");
    }

    /**
     *
     */
    public void switchToEditorScreen() {

        if (content != null) {
            panel.remove(content);
        }
        content = EditorBuilder.build();
        panel.add(content, "grow");

        // and load a basic scenario
        EditorManager.getInstance().loadInitialScenario();
    }

    /**
     *
     */
    public void switchToMainScreen() {
        if (content != null) {
            panel.remove(content);
        }
    }

    /**
     *
     */
    public void shutDown() {
        frame.dispose();
    }

    /**
     *
     * @return
     */
    public Frame getFrame() {
        return frame;
    }


}
