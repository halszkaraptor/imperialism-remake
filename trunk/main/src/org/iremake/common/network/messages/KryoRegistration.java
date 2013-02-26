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
package org.iremake.common.network.messages;

import com.esotericsoftware.kryo.Kryo;
import java.util.logging.Logger;

/**
 * Registration of Message classes to the Kryo serializer. All objects that are
 * sent by the Kryonet library must be registered here and they must contain a
 * private empty constructor. Also they have to be registered on both sides with
 * the same order which is easily achieved by using only this class for
 * registration.
 */
public class KryoRegistration {

    /**
     * Registers all necessary classes for our network communication.
     * @param kryo the kryo object
     */
    public static void register(Kryo kryo) {
        kryo.register(ActionMessage.class);

        kryo.register(TextMessage.class);
        kryo.register(TextMessageType.class);

        kryo.register(NumberMessage.class);
        kryo.register(NumberMessageType.class);
    }

    private KryoRegistration() {
    }
    private static final Logger LOG = Logger.getLogger(KryoRegistration.class.getName());
}
