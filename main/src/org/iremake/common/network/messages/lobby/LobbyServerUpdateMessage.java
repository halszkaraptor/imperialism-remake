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

import org.iremake.common.network.messages.Message;

/**
 *
 */
public class LobbyServerUpdateMessage implements Message {

    private LobbyClientEntry arrivingClient;
    private LobbyClientEntry leavingClient;
    private String chatUpdate;

    private LobbyServerUpdateMessage() {
    }

    public LobbyServerUpdateMessage(LobbyClientEntry arrivingClient, LobbyClientEntry leavingClient, String chatUpdate) {
        if (arrivingClient == null && leavingClient == null && chatUpdate == null) {
            throw new IllegalArgumentException("Not all arguments in a LobbyServerUpdate can be null!");
        }
        this.arrivingClient = arrivingClient;
        this.leavingClient = leavingClient;
        this.chatUpdate = chatUpdate;
    }

    public LobbyClientEntry getArrivingClient() {
        return arrivingClient;
    }

    public LobbyClientEntry getLeavingClient() {
        return leavingClient;
    }

    public String getChatUpdate() {
        return chatUpdate;
    }

    @Override
    public String toString() {
        return String.format("LobbyServerUpdateMessage");
    }
}
