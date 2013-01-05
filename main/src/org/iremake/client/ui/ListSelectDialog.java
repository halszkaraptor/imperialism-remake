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
 * Pretty generic dialog that selects one element from a list. Based on JList.
 * The ListModel is given.
 *
 * After construction you should add a closing listener and read out
 * getSelectedElement() in order to obtain the selected value.
 *
 * Example: final ListSelectDialog<E> dialog = new...
 * dialog.setClosingListener(new WindowClosingListener() {
 * @Override public boolean closing() { E e = dialog.getSelectedElement(); ...
 * return true; } }); dialog.start();
 */
public class ListSelectDialog<E> extends UIDialog {

    /* the JList component */
    private JList<E> list;
    /* the list model */
    private ListModel<E> model;

    /**
     * Setup of the dialog. Only needs to construct the content panel, then tell
     * UIDialog about.
     *
     * @param title Title of the dialog.
     * @param model Model of the list.
     */
    public ListSelectDialog(String title, ListModel<E> model) {
        super(title);
        this.model = model;

        // create a content panel
        JPanel content = new JPanel();

        // new JList, single item selection
        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setModel(model);

        // put list in scroll pane, only vertical scrollong
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // a select button which immediately will close the dialog
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        // layout, at least 100 px width and 200px height of scroll pane, button equal width than scroll pane
        content.setLayout(new MigLayout("fill", "[align center]", "[grow][]"));
        content.add(scrollPane, "wmin 100, hmin 200, wrap, sizegroupx");
        content.add(selectButton, "sizegroupx");

        setContent(content);
        setMinimumSize(0, 0);
    }

    /**
     * Returns the currently selected element or null if none is selected.
     * @return selected element
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