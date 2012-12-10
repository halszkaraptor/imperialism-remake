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

import java.util.LinkedList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.Option;

/**
 *
 */
public class OptionsDialog extends UIDialog {

    private List<OptionsDialogItem> items = new LinkedList<>();

    public OptionsDialog() {
        super("Options");
        // tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // add tabs
        tabs.add(createGeneralPanel(), "General");
        tabs.add(createServerPanel(), "Server");

        setContent(tabs);
    }

    @Override
    public void start() {
        setClosingListener(new WindowClosingListener() {
            @Override
            public boolean closing() {
                if (isAnyModified()) {
                    if (JOptionPane.showConfirmDialog(null, "Save modified options?", "Modified options", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        updateOptions();
                        // TODO show notification
                    }
                }
                return true;
            }
        });
        super.start();
    }

    private JPanel createGeneralPanel() {
        JPanel panel = new JPanel();

        // components
        JCheckBox fullScreen = new JCheckBox("Start in Full Screen");
        items.add(new OptionsDialogCheckBoxItem(fullScreen, Option.FullScreenMode));
        if (!Option.isOSWindows) {
            fullScreen.setEnabled(false);
        }

        JCheckBox mainControlsRight = new JCheckBox("Main Screen controls on right side");
        items.add(new OptionsDialogCheckBoxItem(mainControlsRight, Option.MainScreenControlsRight));

        // layout
        panel.setLayout(new MigLayout("flowy"));
        panel.add(fullScreen);
        panel.add(mainControlsRight);

        return panel;
    }

    private JPanel createServerPanel() {
        JPanel panel = new JPanel();

        // components
        JTextField networkAlias = new JTextField();
        items.add(new OptionsDialogTextFieldItem(networkAlias, Option.NetworkAlias));

        // layout
        panel.setLayout(new MigLayout("wrap 2, fillx", "[][grow]"));
        panel.add(new JLabel("Default network alias"));
        panel.add(networkAlias, "wmin 200");

        return panel;
    }


    private boolean isAnyModified() {
        for (OptionsDialogItem item: items) {
            if (item.isModified()) {
                return true;
            }
        }
        return false;
    }

    private void updateOptions() {
        for (OptionsDialogItem item: items) {
            item.updateOption();
        }
    }

}
