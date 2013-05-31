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

import org.iremake.client.network.ClientContext;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.game.setup.SetupMessage;
import org.iremake.common.network.messages.game.setup.SetupTitlesMessage;
import org.iremake.common.network.messages.game.setup.TitleListEntry;
import org.tools.ui.SimpleListModel;

/**
 *
 */
public class SetupHandler implements ClientHandler {

    private SimpleListModel<TitleListEntry> titleListModel;

    public SetupHandler(SimpleListModel<TitleListEntry> titleListModel) {
        // TODO cannot be null
        this.titleListModel = titleListModel;

    }

    @Override
    public boolean process(Message message, ClientContext context) {
        if (message instanceof SetupMessage) {
            if (message instanceof SetupTitlesMessage) {
                SetupTitlesMessage msg = (SetupTitlesMessage) message;
                titleListModel.set(msg.getTitles());
                titleListModel.sort();
            }
        }
        return false;
    }

}
