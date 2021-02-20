package com.fsmflying.study.quickstart2021.netty.chapter08;

import com.fsmflying.study.quickstart2021.netty.Common;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class RealServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //将消息写到客户端,并添加 ChannelFutureListener,以便消息一被写完就关闭连接
        Common.writeAndFlush(ctx, true, "server receive", true, "Hi");
        //ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将接收到的消息写给发送者,而不冲刷出站消息
        String message = Common.writeAndFlush(ctx, true, "server receive", true, (ByteBuf) msg);
        System.out.println("");
    }
}
