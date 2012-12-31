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
package org.iremake.server.network.clients;

import java.util.Timer;
import java.util.TimerTask;
import org.iremake.client.Option;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.server.network.ServerClientHandler;
import org.iremake.server.network.ServerLogger;

/**
 *
 */
public class UnregisteredSClient extends SClient {

    private static final int TIMEOUT = 10000;
    private Timer timer = new Timer();
    private boolean versionQuery;

    public UnregisteredSClient(ServerClientHandler handler) {
        super(handler);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boss.registrationFailed("Time out.");
            }
        }, TIMEOUT);

    }

    @Override
    public void consume(Message message) {

        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;

            switch (msg.getType()) {
                case Version:
                    // check version

                    if (Option.Version.get().equals(msg.getText())) {
                        ServerLogger.log("Version accepted.");
                        versionQuery = true;
                    } else {
                        timer.cancel();
                        boss.registrationFailed("Wrong version.");
                    }

                    break;
                case ClientName:
                    if (!versionQuery) {
                        boss.registrationFailed("Sent name before version.");
                    }

                    // now we know the name
                    ServerLogger.log("New client name: " + msg.getText());
                    boss.registrationSuccess(msg.getText());

                    break;
            }

        }
    }
}
