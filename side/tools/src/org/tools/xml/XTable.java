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
package org.tools.xml;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * Table (two-dimensional array of strings plus list of column heading strings).
 *
 * Implements the TableModel, therefore it can be used right away in JTable.
 *
 * Can be serialized to XML.
 */
public class XTable implements TableModel, XMLable {

    private static final Logger LOG = Logger.getLogger(XTable.class.getName());
    private List<String> headers;
    private List<List<String>> content;
    private transient List<TableModelListener> listeners = new LinkedList<>();

    /**
     * Empty table with initial capacity.
     */
    public XTable() {
        headers = new ArrayList<>(4);
        content = new ArrayList<>(4);
    }

    public XTable(XProperty property) {
        // set column names ("Keys" and "Values")
        headers = new ArrayList<>(2);
        headers.add("Keys");
        headers.add("Values");

        // Iterate over all keys and add a (key, value) pair as row each time
        Set<String> keys = property.keySet();
        content = new ArrayList<>(keys.size());

        for (String key : keys) {
            List<String> row = new ArrayList<>(2);
            row.add(key);
            row.add(property.get(key));
            content.add(row);
        }
    }

    /**
     *
     * @param headers
     * @param content
     */
    public XTable(List<String> headers, List<List<String>> content) {
        if (checkConsistency() == false) {
            LOG.log(Level.SEVERE, "Supplied content not rectangular or size of headers and columns in data do not match!");
            throw new IllegalArgumentException();
        }
        this.headers = headers;
        this.content = content;
    }

    /**
     *
     * @param headers
     * @param property
     */
    public XTable(List<String> headers, XProperty property) {
        if (headers.size() != 2) {
            throw new IllegalArgumentException("If constructing with a Property, headers must have exactly two entries.");
        }
        this.headers = headers;
        // copy all entries from property
        content = new ArrayList<>(property.size());
        for (String key : property.keySet()) {
            List<String> row = new ArrayList<>(2);
            row.add(key);
            row.add(property.get(key));
            content.add(row);
        }
    }

    /**
     *
     * @param headers
     * @param content
     * @return
     */
    private boolean checkConsistency() {
        // if there are no rows, everything is always fine
        if (content.isEmpty()) {
            return true;
        }
        // the number of columns is the number of entries in headers
        int cols = headers.size();
        if (cols == 0 && !content.isEmpty()) {
            LOG.log(Level.SEVERE, "No columns, but rows, inconsistent state of table");
            return false;
        }

        // for each row in content check that it has cols columns
        for (List<String> row : content) {
            if (row.size() != cols) {
                LOG.log(Level.SEVERE, "Row {0} has size {1} and should have {2}.", new Object[]{String.valueOf(row), String.valueOf(row.size()), String.valueOf(cols)});
                return false;
            }
        }
        // all rows in content have the same length, perfect
        return true;
    }

    /**
     * @return Number of rows in the table.
     */
    @Override
    public int getRowCount() {
        return content.size();
    }

    /**
     * New empty row. If no columns are specified, a new column is added.
     *
     * @param row Index of inserting row.
     */
    public void insertRow(int row) {
        int cols = headers.size();
        if (cols == 0) {
            LOG.log(Level.INFO, "No columns, won't add row.");
            return;
        }
        List<String> emptyRow = new ArrayList<>(cols);
        for (int c = 0; c < cols; c++) {
            emptyRow.add("");
        }
        insertRow(row, emptyRow);
    }

    /**
     *
     * @param row
     * @param rowData
     */
    public void insertRow(int row, List<String> rowData) {
        int cols = headers.size();
        if (cols == 0) {
            LOG.log(Level.INFO, "No columns, won't add row.");
            return;

        }

        if (rowData.size() != cols) {
            LOG.log(Level.INFO, "Provided row is too short or too long, do nothing.");
            return;
        }

        if (row > content.size()) {
            LOG.log(Level.INFO, "Not enough rows in table, cannot insert at this position.");
            return;
        }

        content.add(row, rowData);
        fireTableChanged();
    }

    /**
     *
     * @param row
     */
    public void removeRow(int row) {
        if (row >= content.size()) {
            LOG.log(Level.INFO, "Not enough rows in table, cannot remove at this position.");
            return;
        }
        content.remove(row);
        fireTableChanged();
    }

    /**
     * @return Number of columns in the table.
     */
    @Override
    public int getColumnCount() {
        return headers.size();
    }

    /**
     * @param columnIndex Index of column (starting at 0).
     * @return Name of the column.
     */
    @Override
    public String getColumnName(int columnIndex) {
        return headers.get(columnIndex);
    }

    public void setColumnName(int columnIndex, String columnName) {
        if (columnIndex >= headers.size()) {
            LOG.log(Level.INFO, "Cannot set this column name, not enough columns.");
        }
        headers.set(columnIndex, columnName);
        fireTableChanged();
    }

    /**
     * New empty column.
     *
     * @param columnName
     * @param columnIndex
     */
    public void insertColumn(String columnName, int columnIndex) {
        int rows = getRowCount();
        if (rows == 0) {
            if (columnIndex > headers.size()) {
                LOG.log(Level.INFO, "Not enough columns, cannot add at this position.");
                return;
            }
            headers.add(columnIndex, columnName);
            fireTableChanged();
        } else {
            List<String> emptyColumn = new ArrayList<>(rows);
            for (int r = 0; r < rows; r++) {
                emptyColumn.add("");
            }
            insertColumn(columnName, columnIndex, emptyColumn);
        }
    }

    /**
     *
     * @param columnName
     * @param columnIndex
     * @param columnData
     */
    public void insertColumn(String columnName, int columnIndex, List<String> columnData) {
        if (columnIndex > headers.size()) {
            LOG.log(Level.INFO, "Not enough columns, cannot add at this position.");
            return;
        }

        if (!content.isEmpty()) {
            if (content.size() != columnData.size()) {
                LOG.log(Level.INFO, "Provided column is too short or too long, do nothing.");
                return;
            }
            // insert in each row
            for (int r = 0; r < getRowCount(); r++) {
                content.get(r).add(columnIndex, columnData.get(r));
            }
        }

        // insert in header
        headers.add(columnIndex, columnName);
        fireTableChanged();
    }

    /**
     *
     * @param column
     */
    public void removeColumn(int column) {
        if (column >= headers.size()) {
            LOG.log(Level.INFO, "Not enough columns in table, cannot remove at this position.");
            return;
        }
        headers.remove(column);
        for (int r = content.size() - 1; r >= 0; r--) {
            content.get(r).remove(column);
            if (content.get(r).isEmpty()) {
                content.remove(r); // reason for the loop going backwards
            }
        }
        fireTableChanged();
    }

    /**
     * Always Strings.
     *
     * @param columnIndex The column.
     * @return The class of the column.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public String getEntryAt(int rowIndex, int columnIndex) {
        if (rowIndex >= content.size() || columnIndex >= headers.size()) {
            LOG.log(Level.INFO, "Row or column index out of bounds. Return null.");
            return null;
        }
        return content.get(rowIndex).get(columnIndex);
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return content.get(rowIndex).get(columnIndex);
    }

    /**
     *
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex >= content.size() || columnIndex >= headers.size()) {
            LOG.log(Level.INFO, "Indices are out of range, do nothing.");
            return;
        }
        content.get(rowIndex).set(columnIndex, (String) aValue);
        fireTableChanged();
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     *
     * @param l
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    /**
     *
     * @param l
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    /**
     *
     */
    private void fireTableChanged() {
        // TableModelEvent e = new TableModelEvent(this);
        TableModelEvent event = new TableModelEvent(this, TableModelEvent.HEADER_ROW);
        for (TableModelListener listener : listeners) {
            listener.tableChanged(event);
        }
    }

    /**
     * Listeners are not copied.
     *
     * @return
     */
    public XTable copy() {
        int rows = getRowCount();
        int columns = getColumnCount();
        List<String> heads = new ArrayList<>(columns);
        int r, c;
        for (c = 0; c < columns; c++) {
            heads.add(headers.get(c));
        }
        List<List<String>> cont = new ArrayList<>(rows);
        for (r = 0; r < rows; r++) {
            List<String> row = new ArrayList<>(columns);
            for (c = 0; c < columns; c++) {
                row.add(content.get(r).get(c));
            }
            cont.add(row);
        }

        XTable table = new XTable(heads, cont);
        return table;
    }

    /**
     * Clears all content, preserves the listeners.
     */
    public void clear() {
        headers.clear();
        content.clear();
        fireTableChanged();
    }
    private static final String NAME = "Table";
    private static final String NAME_HEADER = "Column-Names";
    private static final String NAME_CONTENT = "Content-Row";

    /**
     * Listeners are not saved.
     *
     * @return
     */
    @Override
    public Element toXML() {
        Element parent = new Element(NAME);

        // store headers as list
        Element child = XMLHandler.fromStringList(headers, NAME_HEADER);
        parent.appendChild(child);

        // store each row as list
        for (List<String> row : content) {
            child = XMLHandler.fromStringList(row, NAME_CONTENT);
            parent.appendChild(child);
        }

        return parent;
    }

    /**
     * Listeners are not loaded.
     *
     * @param parent
     */
    @Override
    public void fromXML(Element parent) {

        // first clear
        clear();

        if (parent == null || !NAME.equals(parent.getLocalName())) {
            LOG.log(Level.SEVERE, "Empty XML node or node name wrong.");
            return;
        }

        // TODO test names
        Elements children = parent.getChildElements();

        // first child contains the header names as list
        headers = XMLHandler.toStringList(children.get(0));

        // number of childs - 1 is number of rows
        int rows = children.size() - 1;
        content = new ArrayList<>(rows);

        // parse each row (one child) as a list
        for (int i = 0; i < rows; i++) {
            content.add(XMLHandler.toStringList(children.get(i + 1)));
        }

        // check consistency
        if (checkConsistency() == false) {
            // TODO throw an exception?
        }

        fireTableChanged();
    }
}
