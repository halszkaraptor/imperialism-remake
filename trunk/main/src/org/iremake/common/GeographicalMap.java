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
package org.iremake.common;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;
import nu.xom.Elements;
import org.tools.xml.XMLable;
import org.tools.xml.common.Property;

/**
 *
 */
public class GeographicalMap implements XMLable {

    private static final Logger LOG = Logger.getLogger(GeographicalMap.class.getName());
    private int rows;
    private int columns;
    private String[][] map;

    private List<GeographicalMapChangedListener> listeners = new LinkedList<>();

    public GeographicalMap() {
    }

    /**
     * A sea(1) map.
     * @param rows
     * @param columns
     */
    public void setEmptyMap(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        map = new String[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                // TODO get it from somewhere
                map[row][column] = "s1";
            }
        }
        fireMapChanged();
    }

    public boolean checkConsistency() {
        // TODO size and length of map are consistent, ids are valid and only 2 letters long
        return true;
    }

    public void setTerrainAt(int x, int y, String id) {
        if (x >= rows || y >= columns) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return;
        }
        map[x][y] = id;
        fireTileChanged(x, y);
    }

    public String getTerrainAt(int x, int y) {
        if (x >= rows || y >= columns) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return map[x][y];
    }

    public int getNumberRows() {
        return rows;
    }

    public int getNumberColumns() {
        return columns;
    }

    public void addMapChangedListener(GeographicalMapChangedListener l) {
        listeners.add(l);
    }

    public void removeMapChangedListener(GeographicalMapChangedListener l) {
        listeners.remove(l);
    }

    private void fireTileChanged(int x, int y) {
        String id = map[x][y];
        for (GeographicalMapChangedListener l: listeners) {
            l.tileChanged(x, y, id);
        }
    }

    private void fireMapChanged() {
        for (GeographicalMapChangedListener l: listeners) {
            l.mapChanged(this);
        }
    }

    private static final String NAME = "geographical-map";
    private static final String NAME_MAP = "map";

    @Override
    public Element toXML() {
        Element parent = new Element(NAME);

        // add size as list
        Property dimensions = new Property(2);
        dimensions.putInt("rows", rows);
        dimensions.putInt("columns", columns);
        parent.appendChild(dimensions.toXML());

        // assemble map as one big string
        int capacity = rows * columns * 2;
        StringBuilder builder = new StringBuilder(capacity);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                builder.append(map[row][column]);
            }
        }
        Element child = new Element(NAME_MAP);
        child.appendChild(builder.toString());
        parent.appendChild(child);

        return parent;
    }

    @Override
    public void fromXML(Element parent) {

        if (parent == null || !NAME.equals(parent.getLocalName())) {
            LOG.log(Level.SEVERE, "Empty XML node or node name wrong.");
            return;
        }

        Elements children = parent.getChildElements();

        // get size
        Property dimensions = new Property(0);
        dimensions.fromXML(children.get(0));
        rows = dimensions.getInt("rows");
        columns = dimensions.getInt("columns");
        map = new String[rows][columns];

        // TODO test size of string with size
        // TODO more checks (positivity)
        String content = children.get(1).getValue();
        int p = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                map[row][column] = content.substring(p, p + 2);
                p += 2;
            }
        }

        fireMapChanged();
    }
}
