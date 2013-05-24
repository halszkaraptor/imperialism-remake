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
 * Marker interface for Objects being sent over the network by the Kryonet
 * library.
 */
public class Message {

    private MessageType type;
    private Channel channel;

    protected Message() {
    }

    public Message(MessageType type, Channel channel) {
        this.type = type;
        this.channel = channel;
    }

    public MessageType getType() {
        return type;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return String.format("Message [%s, %s]", type, channel);
    }
}
