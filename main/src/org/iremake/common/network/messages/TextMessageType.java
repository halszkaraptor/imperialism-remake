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
package org.iremake.common.network.messages;

/**
 * A type for text messages. It's specified by an enum.
 */
public enum TextMessageType {

    Version, Error, ClientName, Chat;

    /**
     * Directly creates a TextMessage of that type.
     *
     * @param text the text
     * @return the TextMessage
     */
    public TextMessage create(String text) {
        return new TextMessage(this, text);
    }
}
