/*
 * Copyright (C) 2010-11 Trilarion
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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * A simple, modal list selection dialog.
 *
 * It has certain customizations: - rows vary between 8 and 20 - minimal,
 * maximal ViewPort width is 200 to 400 - no horizontal scrolling for cell
 * widths larger 400 - at most one row can be selected.
 */
public class ListSelectDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    /**
     * Default size or borders in the dialog is this.
     */
    private static final int BorderSize = 10;
    /**
     * List component from which one is selected.
     */
    private JList<String> list;

    /**
     * Creates a new dialog.
     *
     * @param parent Parent frame.
     * @param title Dialog title.
     * @param buttonText Selection button text.
     */
    public ListSelectDialog(Frame parent, String title, String buttonText) {
        // this is a modal dialog
        super(parent, true);

        // the whole frame is filled with a panel with BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(BorderSize, BorderSize, BorderSize, BorderSize));

        // create new List with special getPreferredScrollableViewportSize method
        list = new JList<String>() {
            private static final long serialVersionUID = 1L;
            /**
             * Minimal width of viewport
             */
            private static final int MinWidth = 200;
            /**
             * Maximal width of viewport
             */
            private static final int MaxWidth = 400;

            @Override
            public Dimension getPreferredScrollableViewportSize() {
                Dimension size = super.getPreferredScrollableViewportSize();
                // If the width is below MinWidth, set it to MinWidth,
                // if above MaxWidth, set it to MaxWidth.
                size.width = Math.max(MinWidth, Math.min(MaxWidth, size.width));
                return size;
            }
        };
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // create new ScrollPane and set ViewportView to the List
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        // a bit of border below the List
        panel.add(Box.createRigidArea(new Dimension(0, BorderSize)));

        // a centered button with a hardcoded text, whose default action is to dispose the dialog
        JButton button = new JButton();
        button.setText(buttonText);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        button.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(button);

        add(panel);
        // we also dispose on close and the modality is of type APPLICATION_MODAL
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);
        setTitle(title);
    }

    /**
     * Populate the JList with data from a List, and packs the dialog
     * afterwards.
     *
     * @param entries Data
     */
    public void populateList(final List<String> entries) {
        // setting up a JList model
        list.setModel(new AbstractListModel<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public int getSize() {
                return entries.size();
            }

            @Override
            public String getElementAt(int index) {
                return entries.get(index);
            }
        });

        // we want to display at least 8, at most 20 and in between the true number
        // of rows, so vertical scrolling will occur for more than 20 rows
        list.setVisibleRowCount(Math.min(20, Math.max(8, entries.size())));

        // set sizes
        pack();
    }

    /**
     * @return Selected index in the list.
     */
    public int selectedIndexList() {
        return list.getSelectedIndex();
    }

    /**
     * Creates a new dialog, populates it with data, makes it visible, waits for
     * execution and returns the selected content or null if nothing is
     * selected.
     *
     * No entry is selected initially.
     *
     * @param parent Parent frame
     * @param title Dialog title
     * @param buttonText Selection button text
     * @param entries List of Strings
     * @return The selected row or null if nothing has been selected.
     */
    public static String createAndRun(JFrame parent, String title, String buttonText, List<String> entries) {

        // create new modal dialog
        ListSelectDialog dlg = new ListSelectDialog(parent, title, buttonText);

        // set list
        dlg.populateList(entries);

        // createAndRun dialog
        dlg.setVisible(true);

        // get selection and return (return null if nothing is selected)
        int index = dlg.selectedIndexList();
        if (index == -1) {
            return null;
        }
        return entries.get(index);
    }
}
