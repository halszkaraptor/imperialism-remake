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
package org.iremake.common.network.messages;

import java.util.List;
import org.iremake.common.network.messages.game.setup.ClientScenarioInfo;

/**
 *
 */
public enum MessageType {

    // general messages
    GENERAL(0, null),
    GEN_ERROR(0, String.class),
    GEN_LOGIN(0, LoginData.class),

    // lobby messages
    LOBBY(1, null),
    LOBBY_OVERVIEW(1, null),
    LOBBY_CHAT(1, null),
    LOBBY_UPDATE(1, null),

    // setup area messages
    SETUP(2, null),
    SETUP_GET_SCENARIOS_LIST(2, List.class),
    SETUP_GET_SCENARIO_INFO(2, Integer.class),
    SETUP_SCENARIO_INFO(2, ClientScenarioInfo.class),

    // game area message
    GAME(3, null);


    private int category;
    private Class clazz;

    MessageType(int category, Class clazz) {
        this.category = category;
        this.clazz = clazz;
    }

    public boolean isKindOf(MessageType type) {
        return category == type.category;
    }

    public boolean checkClass(Object object) {
        if (clazz != null) {
            return clazz.equals(object.getClass());
        }
        return false;
    }

    public <T> Message<T> createMessage(T content) {
        return new Message<>(content, this);
    }
}
