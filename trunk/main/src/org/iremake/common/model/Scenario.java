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
package org.iremake.common.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;
import nu.xom.Elements;
import org.iremake.common.Settings;
import org.tools.xml.XMLHandler;
import org.tools.xml.XMLable;
import org.tools.xml.XList;
import org.tools.xml.XProperty;

/**
 * The full internal Scenario model.
 */
public class Scenario implements XMLable {

    private static final Logger LOG = Logger.getLogger(Scenario.class.getName());
    private int rows;
    private int columns;
    private Tile[][] map;
    private XList<Nation> nations = new XList<>(Nation.class);
    private List<Province> provinces = new ArrayList<>(1024);
    private List<ScenarioChangedListener> listeners = new LinkedList<>();
    private XProperty properties = new XProperty(10);

    public Scenario() {
    }

    /**
     * A sea(1) map.
     *
     * @param rows
     * @param columns
     */
    public void setEmptyMap(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            LOG.log(Level.INFO, "Zero or negative sizes!");
            return;
        }
        this.rows = rows;
        this.columns = columns;
        map = new Tile[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                map[row][column] = new Tile(Settings.getDefaultTerrainID(), -1, -1);
            }
        }
        fireMapChanged();
    }

    /**
     *
     * @return
     */
    public boolean checkConsistency() {
        // TODO size and length of map are consistent, ids are valid and only 2 letters long
        return true;
    }

    /**
     *
     * @param p
     * @return
     */
    private boolean containsPosition(MapPosition p) {
        return p.row >= 0 && p.row < rows && p.column >= 0 && p.column < columns;
    }

    /**
     *
     * @param p
     * @param id
     */
    public void setTerrainAt(MapPosition p, String id) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return;
        }
        map[p.row][p.column].terrainID = id;
        fireTileChanged(p);
    }

    /**
     *
     * @param p
     * @return
     */
    public String getTerrainAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return map[p.row][p.column].terrainID;
    }

    /**
     *
     * @return
     */
    public int getNumberRows() {
        return rows;
    }

    /**
     *
     * @return
     */
    public int getNumberColumns() {
        return columns;
    }

    // TODO sort alphabetically
    public void addNation(Nation nation) {
        // TODO add element at end
        nations.addElement(nation);
        // TODO fire something
    }

    public XList<Nation> getNations() {
        return nations;
    }

    public String getTitle() {
        return properties.get("title");
    }

    /**
     *
     * @param l
     */
    public void addMapChangedListener(ScenarioChangedListener l) {
        listeners.add(l);
    }

    /**
     *
     * @param l
     */
    public void removeMapChangedListener(ScenarioChangedListener l) {
        listeners.remove(l);
    }

    /**
     *
     * @param p
     */
    private void fireTileChanged(MapPosition p) {
        String id = map[p.row][p.column].terrainID;
        for (ScenarioChangedListener l : listeners) {
            l.tileChanged(p, id);
        }
    }

    /**
     *
     */
    private void fireMapChanged() {
        for (ScenarioChangedListener l : listeners) {
            l.mapChanged(this);
        }
    }
    private static final String NAME = "Scenario";
    private static final String NAME_MAP = "Geographical-Map";

    /**
     * Export to XML.
     *
     * @return
     */
    @Override
    public Element toXML() {
        Element parent = new Element(NAME);

        // update sizes and write properties
        properties.putInt("rows", rows);
        properties.putInt("columns", columns);
        parent.appendChild(properties.toXML());

        // assemble map as one big string
        int capacity = rows * columns * 2; // TODO check somewhere that ids have size 2
        StringBuilder builder = new StringBuilder(capacity);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                builder.append(map[row][column].terrainID);
            }
        }
        Element child = new Element(NAME_MAP);
        child.appendChild(builder.toString());
        parent.appendChild(child);

        // nation list
        parent.appendChild(nations.toXML());

        // provinces list
        parent.appendChild(XMLHandler.fromList(provinces, "Provinces"));

        return parent;
    }

    /**
     * Import from XML.
     *
     * @param parent
     */
    @Override
    public void fromXML(Element parent) {

        // TODO clear (?) neccessary?
        properties.clear();

        if (parent == null || !NAME.equals(parent.getLocalName())) {
            LOG.log(Level.SEVERE, "Empty XML node or node name wrong.");
            return;
        }

        Elements children = parent.getChildElements();

        // get properties and readout sizes
        properties.fromXML(children.get(0));
        rows = properties.getInt("rows");
        columns = properties.getInt("columns");
        map = new Tile[rows][columns];

        // TODO test size of string with size
        // TODO more checks (positivity)
        String content = children.get(1).getValue();
        int p = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Tile tile = new Tile(content.substring(p, p + 2), -1, -1);
                map[row][column] = tile;
                p += 2;
            }
        }

        // TODO reading of Nations and Provinces
        nations.fromXML(children.get(2));

        provinces = XMLHandler.toList(children.get(3), Province.class);

        // Of course everything has changed.
        fireMapChanged();
    }
}
