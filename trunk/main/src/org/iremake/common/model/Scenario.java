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
import org.iremake.common.model.map.MapPosition;
import org.iremake.common.model.map.Tile;
import org.iremake.common.model.map.TilesBorder;
import org.iremake.common.model.map.TilesTransition;
import org.tools.utils.BitBuffer;
import org.tools.xml.XList;
import org.tools.xml.XMLHandler;
import org.tools.xml.XMLable;
import org.tools.xml.XProperty;

/**
 * The full internal Scenario model. It can hold all data structures related to
 * a game and can read from a scenario file and store into it again.
 *
 * There should be much more documentation here, but unfortunately so far there
 * isn't.
 */
// TODO this class has not enough methods and therefore exposes local variables
public class Scenario implements XMLable {

    private static final String XMLNAME = "Scenario";
    private static final String XMLNAME_MAP = "Geographical-Map";
    private static final String XMLNAME_NATIONS = "Nations";
    private static final Logger LOG = Logger.getLogger(Scenario.class.getName());
    private int rows = 0;
    private int columns = 0;
    private Tile[][] map;
    private XProperty properties = new XProperty(10);
    private XList<Nation> nations = new XList<>(Nation.class);
    private Map<Integer, Province> provinces = new HashMap<>(1000);
    private List<ScenarioChangedListener> listeners = new LinkedList<>();

    /**
     * The list of nations shall be kept sorted internally.
     */
    public Scenario() {
        nations.setXMLName(XMLNAME_NATIONS);
        nations.setKeepSorted(true);
    }

    /**
     * An empty map consisting of default values for the tile at each position.
     *
     * @param rows number of rows of the new map
     * @param columns number of columns of the new map
     */
    public void createEmptyMap(int rows, int columns) {
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
                map[row][column] = new Tile();
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
     * @param p
     * @return
     */
    public boolean containsPosition(MapPosition p) {
        return p.row >= 0 && p.row < rows && p.column >= 0 && p.column < columns;
    }

    /**
     * Internal function allowing for easier syntax and direct use of
     * MapPosition.
     *
     * @param p the map position
     * @return the tile
     */
    private Tile getTile(MapPosition p) {
        return map[p.row][p.column];
    }

    /**
     * Is resource visible.
     *
     * @param p
     * @return
     */
    public boolean isResourceVisibleAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return false;
        }
        return getTile(p).resourceVisible;
    }

    /**
     * Changes the terrain at a specific position.
     *
     * @param p
     * @param id
     */
    public void setTerrainAt(MapPosition p, int id) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return;
        }
        getTile(p).terrainID = id;
        fireTileChanged(p);
    }

    /**
     * Returns the terrain at a specific position.
     *
     * @param p
     * @return
     */
    public Integer getTerrainAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return getTile(p).terrainID;
    }

    /**
     * Convenience function for now. Allows too much access.
     *
     * @param p
     * @return
     */
    public Tile getTileAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Position outside of map.");
            return null;
        }
        return getTile(p);
    }

    /**
     * @return number of rows of the map
     */
    public int getNumberRows() {
        return rows;
    }

    /**
     * @return number of columns of the map
     */
    public int getNumberColumns() {
        return columns;
    }

    /**
     * @return list of nations, can be used as model for JList
     */
    public XList<Nation> getNations() {
        return nations;
    }

    /**
     * Returns all the provinces belonging to a certain nation as a sorted
     * XList.
     *
     * @param nation a specific nation
     * @return a XList
     */
    public XList<Province> getProvinces(Nation nation) {

        if (!nations.contains(nation)) {
            LOG.log(Level.SEVERE, "Nation {0} not contained in this scenario?!", nation);
            return null;
        }

        XList<Province> list = new XList<>(Province.class);
        list.setKeepSorted(true);
        // TODO instead sort at the end (immutable version maybe also, or only having ListModel)
        for (Integer id : nation.getProvinces()) {
            list.addElement(provinces.get(id));
        }

        return list;
    }

    /**
     * Returns all the provinces of the scenario in an XList, copies the
     * internal list.
     *
     * @return a XList
     */
    public XList<Province> getAllProvinces() {
        XList<Province> list = new XList<>(Province.class);
        list.setKeepSorted(true);
        for (Province province : provinces.values()) {
            list.addElement(province);
        }

        return list;
    }

    /**
     * Need to get unique IDs. So new Provinces must be created here. A Nation
     * and a name must be given.
     *
     * @param nation the nation
     * @param name the name
     * @return a new province
     */
    public Province newProvince(Nation nation, String name) {
        if (!nations.contains(nation)) {
            LOG.log(Level.SEVERE, "Nation {0} not contained in this scenario?!", nation);
            return null;
        }

        for (int id = Province.NONE + 1; id < 1024; id++) {
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

    /**
     * Returns the nation at a specific position.
     *
     * @param p the position
     * @return the nation
     */
    public Nation getNationAt(MapPosition p) {
        int id = getTile(p).provinceID;
        // need to go through all nations until we find the province, because no map: position -> nation is stored
        for (Nation nation : nations) {
            for (Integer id2 : nation.getProvinces()) {
                if (id == id2) {
                    return nation;
                }
            }
        }
        return null;
    }

    /**
     * Returns the name of the town at a specific position or null if not
     * existing.
     *
     * @param p
     * @return
     */
    public String getTownAt(MapPosition p) {
        for (Province province : provinces.values()) {
            if (province.getTownPosition().equals(p)) {
                return "town";
            }
        }
        return null;
    }

    /**
     * Returns the province at a specific position.
     *
     * @param p
     * @return
     */
    public Province getProvinceAt(MapPosition p) {
        return provinces.get(getTile(p).provinceID);
    }

    /**
     * Calculates neighbored positions for a given map position of a map in
     * staggered layout (i.e. every second row is shifted by half a tile) and
     * for a given transition, where only 3 transitions are actually taken into
     * account so far.
     *
     * The new position might not be on the map any more.
     *
     * @param p the position
     * @param transition the transition
     * @return the position of the neighbor
     */
    private MapPosition getNeighbourPosition(MapPosition p, TilesTransition transition) {
        int row, column, shift;
        switch (transition) {
            case East:
                row = p.row;
                column = p.column + 1;
                break;
            case SouthEast:
                row = p.row + 1;
                shift = p.row % 2 == 0 ? 1 : 0;
                column = p.column + 1 - shift;
                break;
            case SouthWest:
                row = p.row + 1;
                shift = p.row % 2 == 0 ? 1 : 0;
                column = p.column - shift;
                break;
            default:
                row = -1;
                column = -1;
        }
        return new MapPosition(row, column);
    }

    /**
     * Returns the border for a given position and a given tile transition.
     *
     * @param p the position
     * @param transition the transition
     * @return the type of the border
     */
    public TilesBorder getBorder(MapPosition p, TilesTransition transition) {
        if (getTile(p).provinceID == Province.NONE) {
            return TilesBorder.None;
        }
        MapPosition p2 = getNeighbourPosition(p, transition);
        if (!containsPosition(p2) || getTile(p2).provinceID == Province.NONE) {
            return TilesBorder.None;
        }
        // TODO if p2 outside of map also province border
        if (getTile(p).provinceID != getTile(p2).provinceID) {
            return TilesBorder.Province;
            // TODO TileBorder Nation
        }
        return TilesBorder.None;
    }

    public boolean hasRailRoad(MapPosition p, TilesTransition transition) {
        if (!containsPosition(p)) {
            return false;
        }
        return (getTile(p).railroadConfig & (1 << transition.order())) != 0;
    }

    /**
     * Returns the title of the scenario.
     *
     * @return the title
     */
    public String getTitle() {
        return properties.get("title");
    }

    /**
     * Sets the title of the scenario.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        properties.put("title", title);
    }

    /**
     * Adds a listener.
     *
     * @param l the listener
     */
    public void addScenarioChangedListener(ScenarioChangedListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener.
     *
     * @param l the listener
     */
    public void removeScenarioChangedListener(ScenarioChangedListener l) {
        listeners.remove(l);
    }

    /**
     * Tells all listeners that a specific tile has changed.
     *
     * @param p the position of the tile
     */
    private void fireTileChanged(MapPosition p) {
        for (ScenarioChangedListener l : listeners) {
            l.tileChanged(p);
        }
    }

    /**
     * Tells all listeners that everything has changed.
     */
    private void fireScenarioChanged() {
        for (ScenarioChangedListener l : listeners) {
            l.scenarioChanged();
        }
    }

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

        Element child = new Element("Maps");
        parent.appendChild(child);

        // terrain map
        BitBuffer terrainMapBuffer = new BitBuffer(10 * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                terrainMapBuffer.add(map[row][column].terrainID, 5);
                terrainMapBuffer.add(map[row][column].terrainSubID, 5);
            }
        }
        Element schild = new Element("Terrains");
        schild.appendChild(terrainMapBuffer.toXMLString());
        child.appendChild(schild);

        // resources map
        BitBuffer resourcesMapBuffer = new BitBuffer(6 * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                resourcesMapBuffer.add(map[row][column].resourceID, 5);
                resourcesMapBuffer.add(map[row][column].resourceVisible);
            }
        }
        schild = new Element("Resources");
        schild.appendChild(resourcesMapBuffer.toXMLString());
        child.appendChild(schild);

        // provinces
        BitBuffer provinceMapBuffer = new BitBuffer(10 * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                provinceMapBuffer.add(map[row][column].provinceID, 10);
            }
        }
        schild = new Element("Provinces");
        schild.appendChild(provinceMapBuffer.toXMLString());
        child.appendChild(schild);

        // railroads
        BitBuffer railMapBuffer = new BitBuffer(3 * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                railMapBuffer.add(map[row][column].railroadConfig, 3);
            }
        }
        schild = new Element("Railroads");
        schild.appendChild(railMapBuffer.toXMLString());
        child.appendChild(schild);

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
        int size = rows * columns;
        map = new Tile[rows][columns];

        Element child = parent.getFirstChildElement("Maps");

        // readingt of terrain map
        String content = child.getFirstChildElement("Terrains").getValue();
        BitBuffer terbuffer = BitBuffer.fromXMLString(content);
        terbuffer.trimTo(10 * size);

        // readingt of terrain map
        content = child.getFirstChildElement("Resources").getValue();
        BitBuffer resbuffer = BitBuffer.fromXMLString(content);
        resbuffer.trimTo(6 * size);

        // reading of provinces map
        // TODO if no such element returns null and getValue will throw exception
        content = child.getFirstChildElement("Provinces").getValue();
        BitBuffer probuffer = BitBuffer.fromXMLString(content);
        probuffer.trimTo(10 * size);

        // reading of railroads map
        /*
        content = child.getFirstChildElement("Railroads").getValue();
        BitBuffer railbuffer = BitBuffer.fromXMLString(content);
        railbuffer.trimTo(3 * size);
        * */

        // TODO test size of string with size
        // TODO more checks (positivity)
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Tile tile = new Tile();
                tile.terrainID = terbuffer.get(5);
                tile.terrainSubID = terbuffer.get(5);
                tile.resourceID = resbuffer.get(5);
                tile.resourceVisible = resbuffer.get();
                tile.provinceID = probuffer.get(10);
                // tile.railroadConfig = railbuffer.get(3);
                map[row][column] = tile;
            }
        }

        // just a test
        if (terbuffer.size() != 0) {
            // TODO more in buffer... not good, terrainMapBuffer also
        }

        // reading of nations
        nations.fromXML(parent.getFirstChildElement(XMLNAME_NATIONS));

        // reading of provinces (first as list, then converting to hashmap with id)
        List<Province> list = XMLHandler.toList(parent.getFirstChildElement("Provinces"), Province.class);
        for (Province province : list) {
            provinces.put(province.getID(), province);
        }

        // Of course everything has changed.
        fireScenarioChanged();
    }
}
