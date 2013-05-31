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
package org.iremake.common.network.messages.game.setup;

import java.util.List;

/**
 *
 */
public class SetupTitlesMessage implements SetupMessage {

    private List<TitleListEntry> titlesList;

    private SetupTitlesMessage() {
    }

    public SetupTitlesMessage(List<TitleListEntry> titlesList) {
        this.titlesList = titlesList;
    }

    public List<TitleListEntry> getTitles() {
        return titlesList;
    }
}
