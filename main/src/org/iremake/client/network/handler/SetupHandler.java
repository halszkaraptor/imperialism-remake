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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import org.iremake.client.network.ClientContext;
import org.iremake.client.ui.MinimalSetupDialog;
import org.iremake.common.network.messages.Message;
import org.iremake.common.network.messages.game.setup.SetupMessage;
import org.iremake.common.network.messages.game.setup.SetupSelectionMessage;
import org.iremake.common.network.messages.game.setup.SetupTitlesMessage;

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
    public boolean process(Message message, ClientContext context) {
        if (message instanceof SetupMessage) {
            if (message instanceof SetupTitlesMessage) {
                SetupTitlesMessage msg = (SetupTitlesMessage) message;
                dialog.setTitles(msg.getTitles());
                return true;
            } else if (message instanceof SetupSelectionMessage) {
                SetupSelectionMessage msg = (SetupSelectionMessage) message;
                Dimension size = dialog.getMapSize();
                Dimension mapSize = new Dimension(msg.map[0].length, msg.map.length);
                BufferedImage mapImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < size.width; x++) {
                    for (int y = 0; y < size.height; y++) {
                        int column = mapSize.width * x / size.width; // rounding down
                        int row = mapSize.height * y / size.height;
                        Color color = msg.colors[msg.map[row][column]];
                        mapImage.setRGB(x, y, color.getRGB());
                    }
                }          
                dialog.setMap(mapImage);
            }
        }
        return false;
    }

}
