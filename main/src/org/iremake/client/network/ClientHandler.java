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
package org.iremake.client.network;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.network.DebugConsole;
import org.iremake.common.network.messages.Messages;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

/**
 *
 */
public class ClientHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOG = Logger.getLogger(ClientHandler.class.getName());
    private DebugConsole console;

    // stateful properties
    private volatile Channel channel;

    public ClientHandler(DebugConsole console) {
        this.console = console;
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            if (((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
                LOG.info(e.toString());
            }
        }

        // Let SimpleChannelHandler call actual event handler methods below.
        super.handleUpstream(ctx, e);
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        channel = e.getChannel();
        super.channelOpen(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        console.displayMessage("[Client] connected");
    }

    @Override
    public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) {
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        // it's a Message.Msg
        Messages.Msg message = (Messages.Msg) e.getMessage();

        console.displayMessage("[Client] received: " + message.getType());
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) {
        // transferredBytes += e.getWrittenAmount();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        LOG.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
        console.displayMessage("[Client] exception: " + e.getCause());
        e.getChannel().close();
    }
}