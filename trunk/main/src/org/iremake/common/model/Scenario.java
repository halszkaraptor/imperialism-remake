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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;
import org.iremake.common.Settings;
import org.tools.utils.BitBuffer;
import org.tools.xml.XList;
import org.tools.xml.XMLHandler;
import org.tools.xml.XMLable;
import org.tools.xml.XProperty;

/**
 * The full internal Scenario model.
 */
public class Scenario implements XMLable {

    private static final String XMLNAME_NATIONS = "Nations";

    private static final Logger LOG = Logger.getLogger(Scenario.class.getName());
    private int rows = 0;
    private int columns = 0;
    private Tile[][] map;
    private XProperty properties = new XProperty(10);
    private XList<Nation> nations = new XList<>(Nation.class, XMLNAME_NATIONS);
    private Map<Integer, Province> provinces = new HashMap<>();

    private List<ScenarioChangedListener> listeners = new LinkedList<>();

    public Scenario() {
        nations.setKeepSorted(true);
    }

    /**
     * A sea(1) map.
     *
     * @param rows
     * @param columns
     */
    public void createNew(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            LOG.log(Level.INFO, "Zero or negative sizes!");
            return;
        }
        clear();
        this.rows = rows;
        this.columns = columns;
        map = new Tile[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                map[row][column] = new Tile(Settings.getDefaultTerrainID(), Province.NONE);
            }
        }
        fireScenarioChanged();
    }

    /**
     * Listeners are not cleared.
     */
    private void clear() {
        properties.clear();
        nations.clear();
        provinces.clear();
        rows = 0;
        columns = 0;
        map = null;
        // TODO notify somebody?
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
    public boolean containsPosition(MapPosition p) {
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

    public XList<Nation> getNations() {
        return nations;
    }

    public XList<Province> getProvinces(Nation nation) {

        if (!nations.contains(nation)) {
            LOG.log(Level.SEVERE, "Nation {0} not contained in this scenario?!", nation);
            return null;
        }

        XList<Province> list = new XList<>(Province.class);
        list.setKeepSorted(true);
        // TODO instead sort at the end (immutable version maybe also, or only having ListModel)
        for (Integer id: nation.getProvinces()) {
            list.addElement(provinces.get(id));
        }

        return list;
    }

    public XList<Province> getAllProvinces() {
        XList<Province> list = new XList<>(Province.class);
        list.setKeepSorted(true);
        for (Province province: provinces.values()) {
            list.addElement(province);
        }

        return list;
    }

    /**
     * Need to get unique IDs.
     *
     * @param name
     * @return
     */
    public Province newProvince(Nation nation, String name) {
        if (!nations.contains(nation)) {
            LOG.log(Level.SEVERE, "Nation {0} not contained in this scenario?!", nation);
            return null;
        }

        for (Integer id = Province.NONE + 1; id < 1024; id++) {
            if (!provinces.containsKey(id)) {
                Province province = new Province(id, name);
                provinces.put(id, province);
                nation.addProvince(id);
                return province;
            }
        }

        // all 1024 (10bits storage) province ids are taken, cannot create a new one
        return null;
    }

    public String getTitle() {
        return properties.get("title");
    }

    /**
     *
     * @param l
     */
    public void addScenarioChangedListener(ScenarioChangedListener l) {
        listeners.add(l);
    }

    /**
     *
     * @param l
     */
    public void removeScenarioChangedListener(ScenarioChangedListener l) {
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
    private void fireScenarioChanged() {
        for (ScenarioChangedListener l : listeners) {
            l.scenarioChanged(this);
        }
    }
    private static final String XMLNAME = "Scenario";
    private static final String NAME_MAP = "Geographical-Map";

    /**
     * Export to XML.
     *
     * @return
     */
    @Override
    public Element toXML() {
        Element parent = new Element(XMLNAME);

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


        // provinces
        BitBuffer buffer = new BitBuffer(10 * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                buffer.add(map[row][column].provinceID, 10);
            }
        }
        child = new Element("Provinces-Map");
        child.appendChild(buffer.toXMLString());
        parent.appendChild(child);

        // nation list
        parent.appendChild(nations.toXML());

        // write provinces
        parent.appendChild(XMLHandler.fromCollection(provinces.values(), "Provinces"));

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
        clear();
        // TODO we could also set as new of calling clear

        if (parent == null || !XMLNAME.equals(parent.getLocalName())) {
            LOG.log(Level.SEVERE, "Empty XML node or node name wrong.");
            return;
        }

        // get properties and readout sizes
        properties.fromXML(parent.getFirstChildElement(XProperty.XMLNAME));
        rows = properties.getInt("rows");
        columns = properties.getInt("columns");
        map = new Tile[rows][columns];

        // TODO test size of string with size
        // TODO more checks (positivity)
        String content = parent.getFirstChildElement(NAME_MAP).getValue();
        int p = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                // map[row][column].terrainID = content.substring(p, p + 2);
                Tile tile = new Tile(content.substring(p, p + 2), Province.NONE);
                map[row][column] = tile;
                p += 2;
            }
        }

        // reading of provinces map
        content = parent.getFirstChildElement("Provinces-Map").getValue();
        BitBuffer buffer = BitBuffer.fromXMLString(content);
        buffer.trimTo(10 * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                map[row][column].provinceID = buffer.get(10);
            }
        }
        // just a test
        if (buffer.size() != 0) {
            // TODO more in buffer... not good
        }

        // reading of nations
        nations.fromXML(parent.getFirstChildElement(XMLNAME_NATIONS));

        // reading of provinces (first as list, then converting to hashmap with id)
        List<Province> list = XMLHandler.toList(parent.getFirstChildElement("Provinces"), Province.class);
        for (Province province: list) {
            provinces.put(province.getID(), province);
        }

        // Of course everything has changed.
        fireScenarioChanged();
    }
}
