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
import org.iremake.client.io.IOManager;
import org.iremake.client.io.Places;
import org.tools.ui.ButtonFactory;

/**
 * Factory for buttons. Loads the icon from the location specified by the
 * argument of the enum and from a specified place. Sets a few standard
 * properties. Creates a new JButton.
 *
 * As an alternative this could hold a property and store the association in an
 * xml file.
 */
public enum Button {

    // small sized buttons
    SmallAdd("misc/generic.button.add.png", "Add"),
    SmallDelete("misc/generic.button.delete.png", "Delete"),
    SmallEdit("misc/generic.button.edit.png", "Edit"),
    SmallExit("misc/generic.button.exit.png", "Exit"),
    // small sized minimap button
    MiniMapGeographical("map/map.mini.icon.geographical.png", "Geographical"),
    MiniMapPolitical("map/map.mini.icon.political.png", "Political"),
    // normal sized buttons used everywhere
    NormalExit("misc/main.button.exit.png", "Exit"),
    NormalHelp("misc/main.button.help.png", "Help"),
    // normal sized buttons used in the editor
    EditorTerrain("editor/editor.button.terrain.png", "Terrain"),
    EditorNation("editor/editor.button.nation.png", "Nation"),
    EditorProvince("editor/editor.button.province.png", "Province"),
    // normal size buttons used in the network dialog
    NetworkConnect("misc/network.button.connect.png", "Connect"),
    NetworkDisconnect("misc/network.button.disconnect.png", "Disconnect"),
    // normal sized buttons
    GameCenterSingle("misc/game.single.player.png", "Single player"),
    // normal sized buttons with general options for a scenario
    ScenarioLoad("misc/scenario.button.load.png", "Load"),
    ScenarioStart("misc/scenario.button.start.png", "Start"),
    ScenarioNew("misc/scenario.button.new.png", "New"),
    ScenarioSave("misc/scenario.button.save.png", "Save");

    /* Location of the button icon. */
    private String location;
    private String tooltip;

    /**
     * Sets the location.
     *
     * @param location
     */
    Button(String location, String tooltip) {
        this.location = location;
        this.tooltip = tooltip;
    }

    /**
     * Creates the button for a specific enum constant. Refers to the static method.
     *
     * @return
     */
    public JButton create() {
        return Button.create(location, tooltip);
    }

    /**
     * Creates a new button with a given location.
     *
     * @param location
     * @return
     */
    public static JButton create(String location, String tooltip) {
        // TODO set tooltip automatically from a description
        return ButtonFactory.create(IOManager.getAsIcon(Places.GraphicsIcons, location), tooltip);
    }
}
