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

import javax.swing.JButton;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;

/**
 *
 * As an alternative this could hold a property and store the association in an
 * xml file.
 */
public enum Button {

    // big buttons in the start menu
    StartMenuScenario("start/start.button.scenario.png"),
    StartMenuNetwork("start/start.button.network.png"),
    StartMenuOptions("start/start.button.options.png"),
    StartMenuHelp("start/start.button.help.png"),
    StartMenuEditor("start/start.button.editor.png"),
    StartMenuExit("start/start.button.exit.png"),

    // small sized buttons
    SmallAdd("misc/generic.button.add.png"),
    SmallExit("misc/generic.button.exit.png"),

    // normal sized buttons used everywhere
    NormalExit("misc/main.button.exit.png"),
    NormalHelp("misc/main.button.help.png"),

    // normal sized buttons used in the editor
    EditorTerrain("editor/editor.button.terrain.png"),
    EditorNation("editor/editor.button.nation.png"),
    EditorProvince("editor/editor.button.province.png"),

    // normal size buttons used in the network dialog
    NetworkConnect("misc/network.button.connect.png"),

    // normal sized buttons with general options for a scenario
    ScenarioLoad("misc/scenario.button.load.png"),
    ScenarioStart("misc/scenario.button.start.png"),
    ScenarioNew("misc/scenario.button.new.png"),
    ScenarioSave("misc/scenario.button.save.png");

    /* */
    private String location;

    /**
     *
     * @param location
     */
    Button(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public JButton create() {
        return Button.create(location);
    }

    public static JButton create(String location) {
        JButton button = new JButton();
        button.setFocusable(false);
        button.setIcon(IOManager.getAsIcon(Places.GraphicsIcons, location));
        button.setBorder(null);
        button.setMargin(null);
        return button;
    }
}
