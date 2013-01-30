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
package org.iremake.client.network.clients;

import java.util.Timer;
import java.util.TimerTask;
import org.iremake.client.Option;
import org.iremake.client.network.ClientHandler;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessageType;

/**
 * Automatically tries to register.
 */
public class UnregisteredClient extends Client {

    private static final int DELAY = 500;

    public UnregisteredClient(ClientHandler handler) {
        super(handler);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boss.send(TextMessageType.Version.create(Option.General_Version.get()));
                boss.send(TextMessageType.ClientName.create("Name"));
            }
        }, DELAY);
    }

    @Override
    public void consume(Message message) {
    }
}
