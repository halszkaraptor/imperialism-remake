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
package org.iremake.server.network.handler;

import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.MessageType;
import org.iremake.common.network.messages.game.setup.ClientScenarioInfo;
import org.iremake.common.network.messages.game.setup.SetupTitlesList;
import org.iremake.server.client.ScenarioScanner;
import org.iremake.server.client.ServerClient;

/**
 *
 */
public class SetupHandler implements ServerHandler {

    private ScenarioScanner scanner = new ScenarioScanner();

    @Override
    public boolean process(Message message, ServerClient client) {
        switch (message.getType()) {
            case SETUP_GET_SCENARIOS_LIST:
                // scan for scenarios
                scanner.doScan();
                client.send(MessageType.SETUP_GET_SCENARIOS_LIST.createMessage(scanner.getTitles()));
                return true;
            case SETUP_GET_SCENARIO_INFO:
                Integer ID = (Integer) message.getContent();
                if (scanner.hasID(ID)) {
                    // generate the requested scenario information and send the message back
                    ClientScenarioInfo scenarioInfo = new ClientScenarioInfo(scanner.getScenario(ID));
                    client.send(MessageType.SETUP_SCENARIO_INFO.createMessage(scenarioInfo));
                } else {
                    // we don't know the requested ID
                    client.send(MessageType.GEN_ERROR.createMessage("Received unknown id in a SetupSelectionMessage"));
                }
                return true;
        }
        return false;
    }
}
