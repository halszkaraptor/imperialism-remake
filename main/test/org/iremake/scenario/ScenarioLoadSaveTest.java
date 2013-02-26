/*
 * Copyright (C) 2013 Trilarion
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
package org.iremake.scenario;

import java.util.logging.Logger;
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.iremake.common.model.Nation;
import org.iremake.common.model.Province;
import org.iremake.common.model.Scenario;

/**
 *
 */
public class ScenarioLoadSaveTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // create a new empty scenario
        Scenario scenario = new Scenario();
        scenario.createEmptyMap(60, 100);
        scenario.setTitle("Test Scenario");

        // add a nation and a province
        Nation nation = new Nation();
        nation.setProperty(Nation.KEY_NAME, "Test Nation");
        scenario.getNations().addElement(nation);

        Province province = new Province(1, "Test province");
        nation.addProvince(province);

        nation.setProperty(Nation.KEY_CAPITAL, String.valueOf(province.getID()));

        IOManager.saveToXML(Places.None, "test-scenario.xml", scenario);
    }
    private static final Logger LOG = Logger.getLogger(ScenarioLoadSaveTest.class.getName());
}
