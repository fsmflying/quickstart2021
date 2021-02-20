package com.fsmflying.study.quickstart2021.netty.chapter14;

import io.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
 * 代码清单 14-2
 * RequestController
 */
public class RequestController extends IdleStateAwareChannelUpstreamHandler {
    @Override
    public void channelIdle(ChannelHandlerContext ctx,
                            IdleStateEvent e) throws Exception {
        // Shut down connection to client and roll everything back.
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx,
                                 ChannelStateEvent e) throws Exception {
        if (!acquireConnectionSlot()) {
            // Maximum number of allowed server connections reached,
            // respond with 503 service unavailable
            // and shutdown connection.
        } else {
            // Set up the connection's request pipeline.
        }
    }

    private boolean acquireConnectionSlot() {
        return false;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx,
                                MessageEvent e) throws Exception {
        if (isDone()) return;

        if (e.getMessage() instanceof HttpRequest) {
            //Droplr的服务器请求验证的关键点
            handleHttpRequest((HttpRequest) e.getMessage());
        } else if (e.getMessage() instanceof HttpChunk) {
            //如果针对当前请求有一个活动的处理器,并且它能够接受HttpChunk数据,
            //那么它将继续按HttpChunk传递
            handleHttpChunk((HttpChunk) e.getMessage());
        }
    }

    private boolean isDone() {
        return false;
    }

    private void handleHttpRequest(HttpRequest message) {
    }

    private void handleHttpChunk(HttpChunk httpChunk) {

    }
}
