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
package org.tools.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;
import net.miginfocom.swing.MigLayout;
import nu.xom.Element;
import nu.xom.ParsingException;
import org.tools.ui.utils.BasicFrame;
import org.tools.ui.notification.NotificationFactory;
import org.tools.xml.XMLHelper;
import org.tools.xml.XTable;

/**
 * Currently still simple editor, that works on a 2D array of cells, i.e. a
 * table.
 *
 */
// TODO copy and paste between cells,
// TODO undo?
// TODO add column/row at the end
// TODO sorting of columns or rows not reflected in the model, how to incorporate
// TODO search by typing
// TODO composition instead?
public class TableEditorFrame extends BasicFrame {

    private static final Logger LOG = Logger.getLogger(TableEditorFrame.class.getName());
    private static final long serialVersionUID = 1L;

    /**
     * Title for the Frame.
     *
     * @param title
     */
    public TableEditorFrame(String title) {
        super(title);
        initComponents();
    }

    /**
     * Init components.
     */
    private void initComponents() {
        // construct table
        final JTable table = new JTable();
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        // add model
        final XTable model = new XTable();
        model.insertColumn("New Column", 0);
        model.insertRow(0);
        table.setModel(model);

        // create tool bar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // file chooser used in load/save dialogs
        final String extension = ".xml";
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isFile() && f.getName().endsWith(extension);
            }

            @Override
            public String getDescription() {
                return "Table files (*.xml)";
            }
        });

        // create buttons
        JButton newButton = new JButton("New Table");
        JButton loadButton = new JButton("Load Table");
        JButton saveButton = new JButton("Save Table");
        JButton addRowButton = new JButton("Add row");
        JButton delRowButton = new JButton("Remove row");
        JButton addColumnButton = new JButton("Add column");
        JButton delColumnButton = new JButton("Remove column");
        JButton renColumnButton = new JButton("Rename column");
        JButton helpButton = new JButton("Help");

        // action handlers
        // new table button action
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(TableEditorFrame.this, "Discard all changes?", "New Table", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    model.clear();
                    model.insertColumn("New Column", 0);
                    model.insertRow(0);
                }
            }
        });
        // load table button action
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // choose xml file
                if (fileChooser.showOpenDialog(TableEditorFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    // read file and parse to xml
                    Element xml;
                    try (InputStream is = new FileInputStream(f)) {
                        xml = XMLHelper.read(is);
                    } catch (ParsingException | IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        // NotificationFactory.createInfoPane(TableEditorFrame.this, "Loading failed."); // TODO fix
                        return;
                    }
                    // initialize from xml
                    model.fromXML(xml);
                    // NotificationFactory.createInfoPane(TableEditorFrame.this, "Table loaded."); // TODO fix
                }
            }
        });
        // save table button action
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(TableEditorFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File f = fileChooser.getSelectedFile();
                    String name = f.getAbsolutePath();
                    if (!name.endsWith(extension)) {
                        f = new File(name + extension);
                    }
                    Element xml = model.toXML();
                    OutputStream os;
                    try {
                        os = new FileOutputStream(f);
                        XMLHelper.write(os, xml);
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        // NotificationFactory.createInfoPane(TableEditorFrame.this, "Saving failed."); // TODO fix
                        return;
                    }
                    // NotificationFactory.createInfoPane(TableEditorFrame.this, "Table saved."); // TODO fix
                }
            }
        });
        // add row button action
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    row = 0;
                }
                model.insertRow(row);
            }
        });
        // remove row button action
        delRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    row = 0;
                }
                model.removeRow(row);
            }
        });
        // add column button action
        addColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int column = table.getSelectedColumn();
                if (column == -1) {
                    column = 0;
                }
                model.insertColumn("New Column", column);
            }
        });
        // remove column button action
        delColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int column = table.getSelectedColumn();
                if (column == -1) {
                    column = 0;
                }
                // confirmation dialog
                int confirm = JOptionPane.showConfirmDialog(TableEditorFrame.this, "Are you sure?", "Remove column", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    model.removeColumn(column);
                }
            }
        });
        // rename column button action
        renColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = JOptionPane.showInputDialog(TableEditorFrame.this, "New column name", model.getColumnName(0));
                // if user click on cancel newName will be null
                if (newName != null) {
                    int column = table.getSelectedColumn();
                    if (column == -1) {
                        column = 0;
                    }
                    model.setColumnName(column, newName);
                }
            }
        });
        // help button action
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        // add to toolbar
        toolBar.add(newButton);
        toolBar.add(loadButton);
        toolBar.add(saveButton);
        toolBar.addSeparator();
        toolBar.add(addRowButton);
        toolBar.add(delRowButton);
        toolBar.addSeparator();
        toolBar.add(addColumnButton);
        toolBar.add(delColumnButton);
        toolBar.add(renColumnButton);
        toolBar.add(helpButton);

        // create scroll pane (only vertical scroll bar)
        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(table);

        // layout
        Container c = getContentPane();
        c.setLayout(new MigLayout("wrap 1, fill", "", "[][grow]"));
        add(toolBar);
        add(scrollPane, "grow, hmin 400, wmin 400");

        // set to preferred size, validate layout and set minimum size to preferred size
        pack();
        setMinimumSize(getSize());
    }
}
