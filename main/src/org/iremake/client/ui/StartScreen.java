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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import org.iremake.client.Option;
import org.iremake.client.StartClient;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.client.ui.editor.EditorScreen;
import org.iremake.client.ui.options.OptionsDialog;
import org.tools.ui.BrowserPanel;
import org.tools.ui.ImageMapLabel;
import org.tools.ui.layout.RelativeLayout;
import org.tools.ui.layout.RelativeLayoutConstraint;
import org.tools.ui.utils.WindowCorner;

/**
 * Setup of the start screen with a big background image, a logo and some nice
 * big buttons for various game modes, all in layered layout.
 *
 * We are not using MigLayout, because at the time of writing it had problems
 * with LayeredPanes. http://migcalendar.com/forums/viewtopic.php?f=8&t=4075
 */
public class StartScreen extends UIFrame {

    private static final Logger LOG = Logger.getLogger(StartScreen.class.getName());

    /**
     * Setup of the elements and layout.
     */
    public StartScreen() {

        JLayeredPane pane = new JLayeredPane();

        // language drop down menu
        JComboBox<String> languageComboBox = new JComboBox<>();
        languageComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"English", "Spanish", "German", "Chinese"}));   // set model
        languageComboBox.setToolTipText("Select language");     // tooltip

        // background image and image map items
        ImageMapLabel menuLabel = new ImageMapLabel();
        menuLabel.setIcon(IOManager.getAsIcon(Places.GraphicsStartup, "start.background.jpg"));    // set image
        // add all icons
        ActionListener editor = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIFrame frame = new EditorScreen();
                frame.switchTo();
            }
        };
        ActionListener help = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URL index = IOManager.getURL(Places.Help, "en_index.html");
                BrowserPanel browser = new BrowserPanel(index, index, IOManager.getAsLoader(Places.GraphicsBrowserIcons), IOManager.getAsImage(Places.GraphicsIcons, "misc/dialog.background.png"));
                UIDialog.make(browser, "Help");
            }
        };
        menuLabel.addMapItem(new Rectangle(421, 459, 110, 90), new Point(421, 459), "Editor", IOManager.getAsImage(Places.GraphicsStartup, "start.overlay.throne.png"), editor);
        menuLabel.addMapItem(new Rectangle(821, 60, 200, 375), new Point(821, 60), "New Scenario", IOManager.getAsImage(Places.GraphicsStartup, "start.overlay.map.png"), new UIDialogStartAction(NewLocalScenarioDialog.class));
        menuLabel.addMapItem(new Rectangle(575, 412, 70, 140), new Point(575, 412), "Exit", IOManager.getAsImage(Places.GraphicsStartup, "start.overlay.door.right.png"), StartClient.ExitAction);
        menuLabel.addMapItem(new Rectangle(832, 505, 65, 130), new Point(832, 505), "Preferences", IOManager.getAsImage(Places.GraphicsStartup, "start.overlay.fireplace.png"), new UIDialogStartAction(OptionsDialog.class));
        menuLabel.addMapItem(new Rectangle(127, 397, 130, 130), new Point(127, 397), "Help", IOManager.getAsImage(Places.GraphicsStartup, "start.overlay.window.left.png"), help);

        // Version label
        JLabel versionLabel = new JLabel("Version " + Option.General_Version.get());
        versionLabel.setForeground(Color.WHITE);    // white color

        RelativeLayout layout = new RelativeLayout();
        pane.setLayout(layout);

        // add background image (centered)
        pane.add(menuLabel, new Integer(1));
        layout.addConstraint(menuLabel, RelativeLayoutConstraint.centered());

        // add Version in the same layer (right, lower border)
        pane.add(versionLabel, new Integer(2));
        layout.addConstraint(versionLabel, RelativeLayoutConstraint.corner(WindowCorner.SouthEast, 20, 20));

        // add language combo box
        pane.add(languageComboBox, new Integer(4));
        // layout.addConstraint(languageComboBox, RelativeLayoutConstraint.relative(0.7f, 0.5f));
        layout.addConstraint(languageComboBox, RelativeLayoutConstraint.corner(WindowCorner.NorthWest, 5, 5));

        pane.setOpaque(true);
        pane.setBackground(new Color(30, 20, 0));

        setContent(pane);

        FrameManager.getInstance().setShutDownOnClose();
    }
}
