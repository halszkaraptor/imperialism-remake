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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import net.miginfocom.swing.MigLayout;

/**
 * Pretty generic.
 */
public class ListSelectDialog<E> extends UIDialog {

    private JList<E> list;
    private ListModel<E> model;

    public ListSelectDialog(String title, ListModel<E> model) {
        super(title);
        this.model = model;

        JPanel content = new JPanel();

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setModel(model);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        // layout
        content.setLayout(new MigLayout("fill", "[align center]", "[grow][]"));
        content.add(scrollPane, "wmin 100, hmin 200, wrap, sizegroupx");
        content.add(selectButton, "sizegroupx");

        setContent(content);
        setMinimumSize(0, 0);
    }

    /**
     *
     * @return Selected element or null if none is selected.
     */
    public E getSelectedElement() {
        int row = list.getSelectedIndex();
        if (row != -1) {
            return model.getElementAt(row);
        } else {
            return null;
        }
    }
}