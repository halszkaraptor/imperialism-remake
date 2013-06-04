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
import org.iremake.common.network.messages.MessageContainer;
import org.iremake.common.network.messages.game.setup.ClientScenarioInfo;
import org.iremake.server.client.ScenarioScanner;
import org.iremake.server.client.ServerClient;

/**
 *
 */
public class SetupHandler implements ServerHandler {

    /* Gives information about stored scenarios */
    private ScenarioScanner scanner = new ScenarioScanner();

    @Override
    public boolean process(MessageContainer message, ServerClient client) {
        // filter, we only work on general messages
        if (!message.getType().isKindOf(Message.SETUP)) {
            return false;
        }
        // go through each message and do something        
        switch (message.getType()) {
            case SETUP_GET_SCENARIOS_LIST:
                // wants available scenarios, scan for scenarios
                scanner.doScan();
                client.send(Message.SETUP_SCENARIOS_LIST.createNew(scanner.getTitles()));
                return true;

            case SETUP_GET_SCENARIO_INFO:
                // wants info on a specific scenario, send it
                Integer ID = (Integer) message.getAttachment();
                if (scanner.hasID(ID)) {
                    // generate the requested scenario information and send the message back
                    ClientScenarioInfo scenarioInfo = new ClientScenarioInfo(scanner.getScenario(ID));
                    client.send(Message.SETUP_SCENARIO_INFO.createNew(scenarioInfo));
                } else {
                    // we don't know the requested ID
                    client.send(Message.GEN_ERROR.createNew("Received unknown id in a SetupSelectionMessage"));
                }
                return true;
        }
        // still here, then it has nothing to do with us
        return false;
    }
}
