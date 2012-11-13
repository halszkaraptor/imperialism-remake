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
package org.iremake.server.network;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.DebugConsole;
import org.iremake.common.network.messages.Messages;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 *
 */
public class ServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOG = Logger.getLogger(ServerHandler.class.getName());
    private DebugConsole console;

    /**
     * Use null if you don't need it.
     *
     * @param console
     */
    public ServerHandler(DebugConsole console) {
        this.console = console;
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            LOG.info(e.toString());
        }

        // Let SimpleChannelHandler call actual event handler methods below.
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        // it's a Message.Msg
        Messages.Msg message = (Messages.Msg) e.getMessage();

        if (console != null) {
            console.displayMessage("[Server] received: " + message.getType());
        }

        // e.getChannel().write(xxx); // the answer
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        LOG.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());

        if (console != null) {
            console.displayMessage("[Server] exception: " + e.getCause());
        }

        e.getChannel().close();
    }
}