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
 *
 */
public final class NumberMessage implements Message {
    
    private int number;
    private Type type;
    
    public enum Type {
        ID;
    }
    
    private NumberMessage() {
    }
    
    public NumberMessage(int number, Type type) {
        this.number = number;
        this.type = type;
    }
    
    public int getNumber() {
        return number;
    }
    
    public Type getType() {
        return type;
    }
}
