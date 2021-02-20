package com.fsmflying.study.quickstart2021.netty.lifecycle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Netty实战示例2-1
 */

@ChannelHandler.Sharable //标示一个ChannelHandler 可以被多个 Channel 安全地共享
public class EchoServerHandler01 extends ChannelInboundHandlerAdapter {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("####EchoServerHandler01.channelRead####START###############################################");
        //super.channelRead(ctx, msg);
        ByteBuf in = (ByteBuf) msg;
        //将消息记录到控制台
        String strForTime = formatter.format(ZonedDateTime.now());
        String remoteAddress = ctx.channel().remoteAddress().toString();
        String message = String.format("[%s][%s][Server receive]:%s", remoteAddress, strForTime, in.toString(CharsetUtil.UTF_8));
        System.out.println(message);
        //将接收到的消息写给发送者,而不冲刷出站消息
        //ctx.writeAndFlush(message.getBytes(Charset.forName("UTF-8")));
        ByteBuf buf = Unpooled.copiedBuffer(message.getBytes(Charset.forName("UTF-8")));
        ctx.writeAndFlush(buf);
        //ctx.pipeline().writeAndFlush(buf);
        //ctx.channel().writeAndFlush(buf);
        System.out.println("####EchoServerHandler01.channelRead####END#################################################");
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        //将未决消息冲刷到远程节点,并且关闭该 Channel
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//                .addListener(ChannelFutureListener.CLOSE);
        ctx.flush();


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        //打印异常栈跟踪
        cause.printStackTrace();
        //关闭该 Channel
        ctx.close();
    }
}
