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
package org.iremake.server.client;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.ParsingException;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.common.Settings;
import org.iremake.common.model.Nation;
import org.iremake.common.model.ServerScenario;
import org.iremake.common.model.ScenarioTitleProvider;
import org.iremake.common.model.map.MapPosition;
import org.iremake.common.network.messages.game.setup.SetupSelectionMessage;
import org.iremake.common.network.messages.game.setup.TitleListEntry;
import org.tools.io.Resource;
import org.tools.io.ResourceUtils;
import org.tools.utils.Pair;
import org.tools.xml.XMLHelper;

/**
 * Scans for scenarios in the scenario folder and returns a list of file names
 * and corresponding titles.
 */
// TODO smarter loading of scenarios (partly for title, ...)
public class ScenarioScanner {

    private static final Logger LOG = Logger.getLogger(ScenarioScanner.class.getName());
    private Map<Integer, Pair<Resource, String>> scenarios = new HashMap<>();

    public ScenarioScanner() {
    }

    /**
     * First looks at a specific directory for all files named scenario.XXX.xml,
     * then loads them all and puts the scenario titles in a list.
     */
    public void doScan() {
        scenarios.clear();

        Resource directory;
        try {
            directory = ResourceUtils.asResource(IOManager.getPath(Places.Scenarios, ""));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }

        List<Resource> files;
        try {
            files = directory.list("scenario\\.[a-zA-z0-9]+\\.xml");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }

        int id = 0;
        ScenarioTitleProvider titleProvider = new ScenarioTitleProvider();
        for (Resource resource : files) {
            try {
                // load it completely
                // TODO instead of loading and parsing completely there might be a simpler function that only loads the title
                XMLHelper.read(resource, titleProvider);
                scenarios.put(id++, new Pair<>(resource, titleProvider.getTitle()));
            } catch (IOException | ParsingException ex) {
                LOG.log(Level.SEVERE, null, ex);
                // TODO a scenario cannot be loaded, delete it from the list later on
            }
        }
    }

    /**
     * Get the list of all titles.
     *
     * @return
     */
    public List<TitleListEntry> getTitles() {
        List<TitleListEntry> list = new ArrayList<>(scenarios.size());
        for (Integer id : scenarios.keySet()) {
            list.add(new TitleListEntry(id, scenarios.get(id).getB()));
        }
        return list;
    }

    /**
     * Returns the resource for a specified index.
     *
     * @param index
     * @return
     */
    public Resource getResource(Integer id) {
        if (!scenarios.containsKey(id)) {
            // TODO error
            throw new IllegalArgumentException("id is not a valid key of the scenarios map.");
        }
        return scenarios.get(id).getA();
    }

    public boolean hasID(int id) {
        return scenarios.containsKey(id);
    }

    public ServerScenario getScenario(int id) {
        // load scenario (complete)
        Resource resource = scenarios.get(id).getA();
        ServerScenario scenario = new ServerScenario();
        try {
            XMLHelper.read(resource, scenario);
        } catch (IOException | ParsingException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
        return scenario;
    }
}
