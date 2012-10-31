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
package org.iremake.client.ui.main;

import java.awt.Window;
import javax.swing.JDialog;
import nu.xom.Element;
import org.iremake.client.ui.Model;
import org.iremake.client.ui.ModelChangedListener;
import org.iremake.client.ui.map.MainMapPanel;
import org.iremake.common.MapPosition;
import org.iremake.common.model.Scenario;
import org.iremake.common.resources.Loader;
import org.iremake.common.resources.Places;
import org.tools.xml.XMLHelper;

/**
 *
 */
// TODO generalize between EditorManager and this class (some code duplicates)
public class MainScreenManager implements ModelChangedListener {
    
    private Scenario map;
    private MainMapPanel mainMapPanel;    
    private Model model;    
    private Window window;

    public void setFrame(JDialog dialog) {
        window = dialog;
    }
    
    public void setPanels(MainMapPanel mainPanel) {
        mainMapPanel = mainPanel;
    }    

    public void setScenarioContent(Scenario map, Model model) {
        this.map = map;
        this.model = model;

        // wiring (map tells model, model tells manager)
        map.addMapChangedListener(model);
        model.setScenarioChangedListener(this);
    }    
    
    public void loadInitialScenario() {
        //map.setEmptyMap(60, 100);
        loadScenario(Loader.getPath(Places.ScenarioMaps, "map.Europe1814.xml"));
    }

    private void loadScenario(String location) {
        Element xml = XMLHelper.read(location);
        map.fromXML(xml);
    }    

    @Override
    public void tileChanged(MapPosition p) {
        // TODO do we need this method? (adapter)
    }

    @Override
    public void mapChanged() {
        // TODO size of map could also have changed!!!
        mainMapPanel.mapChanged();
    }

    public void exit() {
        window.dispose();
    }
    
}
