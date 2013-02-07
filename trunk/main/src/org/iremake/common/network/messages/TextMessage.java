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
 * A message consisting of some text and a type (given by an enum).
 */
public final class TextMessage implements Message {

    private String text;
    private TextMessageType type;

    private TextMessage() {
    }

    public TextMessage(TextMessageType type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public TextMessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("TextMessage [%s, %s]", type.name(), text);
    }
}
