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

/**
 *
 */
public class LoginMessage extends Message {

    private String version;
    private String clientname;

    private LoginMessage() {
    }

    public LoginMessage(String version, String clientname) {
        super(Channel.LOGIN);
        this.version = version;
        this.clientname = clientname;
    }

    public String getVersion() {
        return version;
    }

    public String getClientName() {
        return clientname;
    }

    @Override
    public String toString() {
        return String.format("LoginMessage [Version : %s, ClientName %s, %s]", version, clientname, getChannel());
    }
}
