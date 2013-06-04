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
package org.iremake.client.network.handler;

import java.util.List;
import org.iremake.client.network.ClientContext;
import org.iremake.client.ui.MinimalSetupDialog;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.MessageContainer;
import org.iremake.common.network.messages.game.setup.ClientScenarioInfo;
import org.iremake.common.network.messages.game.setup.TitleListEntry;

/**
 *
 */
public class SetupHandler implements ClientHandler {

    private MinimalSetupDialog dialog;

    public SetupHandler(MinimalSetupDialog dialog) {
        if (dialog == null) {
            throw new IllegalArgumentException("Argument dialog cannot be null.");
        }
        this.dialog = dialog;
    }

    @Override
    public boolean process(MessageContainer message, ClientContext context) {
        // filter, we only work on general messages
        if (!message.getType().isKindOf(Message.SETUP)) {
            return false;
        }
        // go through each message and do something        
        switch (message.getType()) {
            case SETUP_SCENARIOS_LIST:

                @SuppressWarnings("unchecked") // we only cast to List, but we know/are fairly sure it will be okay
                List<TitleListEntry> titles = (List<TitleListEntry>) message.getAttachment();
                dialog.setTitles(titles);
                return true;

            case SETUP_SCENARIO_INFO:

                dialog.setInfo((ClientScenarioInfo) message.getAttachment());
                return true;
        }

        // still here, then it has nothing to do with us        
        return false;
    }
}
