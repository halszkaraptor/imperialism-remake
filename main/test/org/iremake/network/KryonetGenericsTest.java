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
package org.iremake.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class KryonetGenericsTest {
    
    private static class Packet<T> {
        private T content;
        private Packet() {
        }
        
        public Packet(T content) {
            this.content = content;
        }
        
        public T getContent() {
            return content;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        final Server server = new Server();
        Kryo kryo = server.getKryo();
        kryo.register(Packet.class);
        
        server.start();
        server.bind(12_345);
        
        final Client client = new Client();
        kryo = client.getKryo();
        kryo.register(Packet.class);        
        
        client.start();
        client.connect(5_000, "localhost", 12_345);        
        
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                System.out.println("type of object " + object.getClass().getName());
                if (object instanceof Packet) {
                    System.out.println("type of content " + ((Packet) object).getContent().getClass().getName());
                }
            }
        });        
        
        server.sendToAllTCP(new Packet<String>("Hello world!"));
        

        // stopping timer after 2s
        final Timer timer = new Timer("timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                client.stop();
                server.stop();
            }
        }, 200_000);        
        
    }
}
