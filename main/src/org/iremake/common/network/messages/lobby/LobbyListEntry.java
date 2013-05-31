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
package org.iremake.common.network.messages.lobby;

/**
 *
 */
public class LobbyListEntry implements Comparable {

    public String name;
    public String ip;
    public String joined;
    
    private LobbyListEntry() {
    }
    
    public LobbyListEntry(String name, String ip, String joined) {
        // TODO not null
        this.name = name;
        this.ip = ip;
        this.joined = joined;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        // TODO throw exception when compared to something else
        LobbyListEntry other = (LobbyListEntry) o;
        return name.compareTo(other.name);
    }
}
