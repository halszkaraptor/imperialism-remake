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
package org.tools.ui.common;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.tools.common.Pair;

/**
 * A tabbed Panels dialog. Ideal prototype for setting options.
 *
 * The size of the tabbed panels is given by the largest preferred size of all
 * Tabs, so it's best to make them approximately equally large.
 */
// TODO GroupLayout
public class TabbedDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     *
     * @param parent
     * @param modal
     * @param panelList
     */
    public TabbedDialog(Frame parent, boolean modal, List<Pair<String, JPanel>> panelList, ActionListener listener) {
        super(parent, modal);
        init(panelList, listener);
    }

    /**
     *
     * @param panelList
     */
    private void init(List<Pair<String, JPanel>> panelList, ActionListener listener) {

        // the whole frame is filled with a panel with BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTabbedPane tabbedPane = new JTabbedPane();
        for (Pair<String, JPanel> p : panelList) {
            tabbedPane.addTab(p.getA(), p.getB());
        }


        // bottom panel is for showing the forward, backward, close buttons in right aligned FlowLayout
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(listener);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        bottom.add(saveButton);
        bottom.add(cancelButton);

        // add all sub panels to panel and add it to the dialog
        panel.add(tabbedPane);
        panel.add(bottom);
        add(panel);

        // we also dispose on close and the modality is of type APPLICATION_MODAL
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);
        pack();
    }

    /**
     *
     * @param parent
     * @param title
     * @param modal
     * @param panelList
     * @param listener
     */
    public static void create(JFrame parent, String title, boolean modal, List<Pair<String, JPanel>> panelList, ActionListener listener) {
        // create new modal dialog
        TabbedDialog dlg = new TabbedDialog(parent, modal, panelList, listener);

        // set title
        dlg.setTitle(title);

        // start dialog
        dlg.setVisible(true);
    }
}
