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
package org.iremake.client.io;

/**
 * Logical structure of the data folders, used for input/output. Helps when
 * refactoring the real folder structure. Such changes must only be adapted to
 * here.
 */
public enum Places {

    /* the places */
    None("./"),
    Log("log/"),

    Help("data/help/"),

    Common("data/game/common/"),

    Scenarios("data/game/scenarios/"),

    Graphics("data/game/artwork/graphics/"),

    GraphicsIcons(Graphics + "ui/"),
    GraphicsStartup(GraphicsIcons + "start/"),
    GraphicsBrowserIcons(GraphicsIcons + "browser/"),

    GraphicsScenario(Graphics + "scenario/"),

    Music("data/game/artwork/music/");

    /* the location of the place */
    private String location;

    /**
     * Store the location.
     *
     * @param location
     */
    Places(String location) {
        if (location == null || !location.endsWith("/")) {
            throw new IllegalArgumentException("Location cannot be null, must end with '/'!");
        }
        this.location = location;
    }

    /**
     * @return The location.
     */
    @Override
    public String toString() {
        return location;
    }
}
