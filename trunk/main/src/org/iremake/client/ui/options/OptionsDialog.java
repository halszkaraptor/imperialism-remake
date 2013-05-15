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
package org.iremake.client.ui.options;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.Option;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.client.sound.MusicManager;
import org.iremake.client.ui.FrameManager;
import org.iremake.client.ui.UIDialog;
import org.iremake.client.ui.WindowClosingListener;
import org.iremake.common.BigBag;
import org.iremake.server.network.ServerManager;
import org.tools.sound.SoundSystem;
import org.tools.ui.PanelWithBackground;
import org.tools.ui.SimpleComboBoxModel;

/**
 * Dialog for displaying and modifying options in a tabbed pane style. The
 * update and check for modified values is done by iterating over a list of
 * OptionsDIalogItems. Therefore we avoid abundant use of listeners. Anyway we
 * only want to check the options upon exit of the dialog.
 *
 * If any OptionItem is modified and the user accepts the change then all
 * modified OptionItems are updated. This always changes the Option value but
 * sometimes might have more effects. One example is the Mute Option where
 * additionally the music system is set to mute or not mute.
 */
public class OptionsDialog extends UIDialog {

    private static final Logger LOG = Logger.getLogger(OptionsDialog.class.getName());
    /* list holding all items */
    private List<OptionsDialogItem> items = new LinkedList<>();
    /* additional item */
    private SimpleComboBoxModel<String> mixerModel;

    /**
     * Setup of the dialog, add the tabs to the tabbed pane.
     */
    public OptionsDialog() {
        super("Options");
        // tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // add tabs
        tabs.add(createGeneralPanel(), "General");
        tabs.add(createServerPanel(), "Server");
        tabs.add(createMusicPanel(), "Music");

        setContent(tabs);

        setClosingListener(new WindowClosingListener() {
            @Override
            public boolean closing() {
                if (isAnyModified()) {
                    if (JOptionPane.showConfirmDialog(null, "Save modified options?", "Modified options", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        updateOptions();
                        FrameManager.getInstance().scheduleInfoMessage("Options updated");
                    }
                }
                return true;
            }
        });
    }

    /**
     * @return General options tab.
     */
    private Component createGeneralPanel() {

        JPanel graphics = new JPanel();
        graphics.setOpaque(false);
        graphics.setBorder(BorderFactory.createTitledBorder("Graphics"));

        // components
        JCheckBox fullScreen = new JCheckBox("Start in Full Screen");
        fullScreen.setOpaque(false);
        items.add(new OptionsDialogCheckBoxItem(fullScreen, Option.Graphics_FullScreenMode));
        if (!Option.isOSWindows) {
            fullScreen.setEnabled(false);
        }

        JCheckBox mainControlsRight = new JCheckBox("Main Screen controls on right side");
        mainControlsRight.setOpaque(false);
        items.add(new OptionsDialogCheckBoxItem(mainControlsRight, Option.Graphics_MainScreenControlsRight));

        // graphics panel layout
        graphics.setLayout(new MigLayout("flowy"));
        graphics.add(fullScreen);
        graphics.add(mainControlsRight);

        JPanel language = new JPanel();
        language.setBorder(BorderFactory.createTitledBorder("Language"));

        // layout
        JPanel panel = new PanelWithBackground(IOManager.getAsImage(Places.GraphicsIcons, "misc/dialog.background.png"));
        panel.setLayout(new MigLayout("flowy"));
        panel.add(graphics, "sizegroupx");
        panel.add(language, "sizegroupx");

        return panel;
    }

    /**
     * @return Server options tab.
     */
    private Component createServerPanel() {
        JPanel panel = new JPanel();

        // server toggle
        final ServerManager serverManager = BigBag.serverManager;
        final JLabel serverStatus = new JLabel();
        serverStatus.setText(serverManager.getStatus());

        final JToggleButton serverToggleButton = new JToggleButton("Start local server");
        serverToggleButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                int state = itemEvent.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    serverManager.start();
                    serverToggleButton.setText("Shutdown local server");
                } else {
                    serverManager.stop();
                    serverToggleButton.setText("Start local server");
                }
                // update status
                serverStatus.setText(serverManager.getStatus());
            }
        });

        // components
        JTextField networkAlias = new JTextField();
        items.add(new OptionsDialogTextFieldItem(networkAlias, Option.Client_Alias));

        // layout
        panel.setLayout(new MigLayout("wrap 2, fillx", "[][grow]"));
        panel.add(serverStatus, "span 2");
        panel.add(new JLabel("Start/stop local server"));
        panel.add(serverToggleButton);
        panel.add(new JLabel("Default network alias"));
        panel.add(networkAlias, "wmin 200");

        return panel;
    }

    /**
     * @return Music options tab.
     */
    private Component createMusicPanel() {
        JPanel panel = new JPanel();

        // TODO use OptionsDialogComboBoxItem
        // TODO only if there are some, otherwise disable
        JComboBox<String> mixerBox = new JComboBox<>();
        if (SoundSystem.hasActiveMixer()) {
            mixerModel = new SimpleComboBoxModel<>(SoundSystem.getAvailableMixerNames());
            mixerBox.setModel(mixerModel);
            mixerBox.setSelectedItem(SoundSystem.getActiveMixerName());
            mixerBox.setToolTipText("Changes will be applied upon next startup.");
        } else {
            mixerBox.setEnabled(false);
        }

        JCheckBox muteSound = new JCheckBox("Mute sound");
        items.add(new OptionsDialogCheckBoxItem(muteSound, Option.Music_Mute) {
            @Override
            public void updateOption() {
                super.updateOption(); // do not forget!
                if (Option.Music_Mute.getBoolean() == true) {
                    MusicManager.stop();
                } else {
                    MusicManager.start();
                }
            }
        });

        // layout
        panel.setLayout(new MigLayout("wrap 2"));
        panel.add(new JLabel("Available devices"));
        panel.add(mixerBox);
        panel.add(muteSound);

        return panel;
    }

    /**
     * Check upon closing if any option is modified. Iterates over the list of
     * items.
     *
     * @return True if any Options item is modified.
     */
    private boolean isAnyModified() {
        for (OptionsDialogItem item : items) {
            if (item.isModified()) {
                return true;
            }
        }
        return false;
    }

    /**
     * User accepts modified Options. Update!
     */
    private void updateOptions() {
        for (OptionsDialogItem item : items) {
            if (item.isModified()) {
                item.updateOption();
            }
        }
    }
}
