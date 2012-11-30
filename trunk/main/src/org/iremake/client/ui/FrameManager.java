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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.iremake.client.ui.editor.EditorBuilder;
import org.iremake.client.ui.editor.EditorManager;
import org.iremake.client.ui.main.MainScreenBuilder;
import org.iremake.client.ui.main.MainScreenManager;
import org.tools.ui.ComponentMover;
import org.tools.ui.ComponentResizer;
import org.tools.ui.utils.GraphicsUtils;

/**
 * Startframe / Mainframe / Editorframe juggling.
 */
public class FrameManager {

    private static FrameManager singleton;
    private JFrame frame;
    private JDialog dialog;
    private ActionListener exitDialogListener;
    private JPanel panel;
    private JComponent content;

    /**
     *
     */
    private FrameManager() {
        setupMainFrame();

        exitDialogListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        };
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
        panel.validate();
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
        panel.validate();

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

        content = MainScreenBuilder.build();
        panel.add(content, "grow");
        panel.validate();

        // and load a basic scenario
        MainScreenManager.getInstance().loadInitialScenario();
    }

    /**
     * Potentially dangerous but the only way unless we import many dialog
     * creations.
     *
     * @return
     */
    public Frame getFrame() {
        return frame;
    }

    private final static Color blue = new Color(160, 224, 255);
    private final static Color brown = new Color(128, 80, 16);

    public void startDialog(JComponent content, String title) {
        startDialog(content, title, new Dimension(700, 600));
    }

    public ActionListener getExitDialogListener() {
        return exitDialogListener;
    }

    public void startDialog(JComponent content, String title, Dimension minimumSize) {

        dialog = new JDialog(frame, true);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setUndecorated(true);
        dialog.setResizable(true);

        dialog.getContentPane().setBackground(brown);

        JPanel top = new JPanel();
        top.setBackground(blue);

        JButton exitButton = Button.SmallExit.create();
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setBorder(null);
        exitButton.setBackground(blue);
        exitButton.addActionListener(exitDialogListener);

        JLabel titleLabel = new JLabel(title);

        top.setLayout(new MigLayout("fill, insets 2"));
        top.add(titleLabel, "alignx left");
        top.add(exitButton, "alignx right");

        dialog.setLayout(new MigLayout("wrap 1, fill, insets 0 5 5 5", "", "[][grow]"));
        dialog.add(top, "growx");
        dialog.add(content, String.format("grow, hmin %d, wmin %d", minimumSize.height, minimumSize.width));

        // instead of this we should set the bounds from options
        dialog.pack();

        ComponentMover mover = new ComponentMover();
        mover.setDestination(dialog);
        mover.registerComponent(top);

        ComponentResizer resizer = new ComponentResizer(new Insets(5, 5, 5, 5), new Dimension(10, 10));
        resizer.registerComponent(dialog);

        dialog.setVisible(true);
    }

    /**
     *
     * @param title
     * @param size
     * @param modal
     * @return
     */
    public JDialog makeDialog(String title, Dimension size) {

        // TODO make the dialog undecorated but resizable and moveable

        JDialog dialog = new JDialog(frame, title, true);

        // set undecorated
        dialog.setUndecorated(true);

        // turn of usual exiting mechanisms
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // resizable
        dialog.setResizable(true);

        // initial bounds (centered on frame)
        Rectangle pb = frame.getBounds();
        Rectangle bounds = new Rectangle(pb.x+pb.width/2-size.width/2,pb.y+pb.height/2-size.height/2,size.width,size.height);
        dialog.setBounds(bounds);

        // set background
        dialog.getContentPane().setBackground(Color.WHITE);    // white color

        return dialog;
    }
}
