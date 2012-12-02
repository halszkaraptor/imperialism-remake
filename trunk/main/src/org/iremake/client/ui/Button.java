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

import java.awt.Color;
import javax.swing.BorderFactory;
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
    StartMenuScenario("start.button.scenario.png"),
    StartMenuNetwork("start.button.network.png"),
    StartMenuOptions("start.button.options.png"),
    StartMenuHelp("start.button.help.png"),
    StartMenuEditor("start.button.editor.png"),
    StartMenuExit("start.button.exit.png"),

    // small sized buttons
    SmallAdd("generic.button.add.png"),
    SmallExit("generic.button.exit.png"),

    // normal sized buttons used everywhere
    NormalExit("main.button.exit.png"),
    NormalHelp("main.button.exit.png"), // TODO own icon

    // normal sized buttons used in the editor
    EditorTerrain("editor.button.terrain.png"),
    EditorNation("editor.button.nation.png"),
    EditorProvince("editor.button.province.png"),

    // normal sized buttons with general options for a scenario
    ScenarioLoad("scenario.button.load.png"),
    ScenarioStart("scenario.button.start.png"),
    ScenarioNew("scenario.button.new.png"),
    ScenarioSave("scenario.button.save.png");

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
        button.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
        return button;
    }
}
