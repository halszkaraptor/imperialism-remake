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
 * Just a String and a enumeration indicating which type the message is.
 */
public class TextMessage implements Message {
    
    private String text;
    private Type type;
    
    public enum Type {
        VERSION, REGISTER;
    }
    
    private TextMessage() {
    }
    
    public TextMessage(String text) {
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
    
    public Type getType() {
        return type;
    }
}
