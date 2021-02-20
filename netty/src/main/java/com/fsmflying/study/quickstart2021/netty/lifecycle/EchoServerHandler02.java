package com.fsmflying.study.quickstart2021.netty.lifecycle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.time.format.DateTimeFormatter;

/**
 *
 */

@ChannelHandler.Sharable //标示一个ChannelHandler 可以被多个 Channel 安全地共享
public class EchoServerHandler02 extends ChannelInboundHandlerAdapter {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);

        ctx.fireChannelRead(msg);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.flush();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
//        打印异常栈跟踪
        cause.printStackTrace();
        //关闭该 Channel
        ctx.close();
    }
}
