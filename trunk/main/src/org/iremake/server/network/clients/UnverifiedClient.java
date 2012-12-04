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
import org.iremake.common.network.messages.ActionMessage;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.TextMessage;
import org.iremake.common.network.messages.TextMessageType;
import org.iremake.server.network.ServerClientHandler;
import org.iremake.server.network.ServerLogger;

/**
 *
 */
public class UnverifiedClient extends Client {

    private static final int MaxRepeats = 5;

    private Timer timer = new Timer();
    private int repeats = 0;

    public UnverifiedClient(ServerClientHandler handler) {
        super(handler);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                repeats++;
                if (repeats > MaxRepeats) {
                    timer.cancel();
                    boss.verificationFailed(TextMessageType.Error.create("Verification time out."));
                }
                boss.send(ActionMessage.Verify);
            }
        }, 500, 2000);
    }

    @Override
    public void consume(Message message) {
        if (message instanceof TextMessage) {
            TextMessage msg = (TextMessage) message;
            if (TextMessageType.Version.equals(msg.getType())) {
                if (Option.Version.get().equals(msg.getText())) {
                    ServerLogger.log("Version accepted, promote to valid.");
                    timer.cancel();
                    boss.verificationSuccess();
                } else {
                    timer.cancel();
                    boss.verificationFailed(TextMessageType.Error.create("Version mismatch."));
                }
            }
        }
    }
}
