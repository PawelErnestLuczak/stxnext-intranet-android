/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.stxnext.management.server.planningpoker.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Handles a server-side channel.
 */
@Sharable
public class PokerServerHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger;
    private final HashMap<String, ChannelHandlerContext> channels = new LinkedHashMap<String, ChannelHandlerContext>(200, 0.5f);
    
    public PokerServerHandler(){
        logger = ServerConfigurator.getInstance().getLogger();
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        ctx.write(
                "Welcome to " + InetAddress.getLocalHost().getHostName() + "\r\n");
        ctx.flush();
        channels.put(ctx.channel().remoteAddress().toString(),ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        String msg = ctx.channel().remoteAddress().toString() + "UNREGISTERED \r\n";
        logger.log(Level.WARN, msg);
        broadcastToGroup(ctx.channel().remoteAddress().toString()+" has disconnected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String msg = ctx.channel().remoteAddress().toString() + " INACTIVE \r\n";
        logger.log(Level.WARN, msg);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {

        logger.log(Level.DEBUG, "request from " + ctx.channel().remoteAddress().toString() + ":"
                + request + "\r\n");
        // Generate and write a response.
        String response;
        boolean close = false;
        if (request.isEmpty()) {
            response = "[empty message].\r\n";
        } else if ("quit".equals(request.toLowerCase())) {
            response = "Exit requested, closing channel!\r\n";
            close = true;
        } else {
            response = request +"\r\n";
        }
        broadcastToGroup(response);
        
    }
    
    private void broadcastToGroup(String message){
        removeInactiveChannels();
        for (ChannelHandlerContext channel : channels.values()) {
            ChannelFuture future = channel.writeAndFlush(message);
            //future.isCancelled()
        }
        logger.log(Level.INFO,message);
    }
    
    private void removeInactiveChannels(){
        List<String> inactiveChannels = new ArrayList<String>();
        for (Entry<String, ChannelHandlerContext> entry : channels.entrySet()) {
            if(!entry.getValue().channel().isActive() || !entry.getValue().channel().isRegistered()){
                inactiveChannels.add(entry.getKey());
                entry.getValue().close();
            }
        }
        for(String key : inactiveChannels){
            channels.remove(key);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
         logger.log(
         Level.WARN,
         "Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
