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
public class LoginData {

    private String version;
    private String clientname;

    private LoginData() {
    }

    public LoginData(String version, String clientname) {
        if (version == null || clientname == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
        this.version = version;
        this.clientname = clientname;
    }

    public String getVersion() {
        return version;
    }

    public String getClientName() {
        return clientname;
    }
}
