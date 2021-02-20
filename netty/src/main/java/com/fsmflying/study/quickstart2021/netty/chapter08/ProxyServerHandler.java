package com.fsmflying.study.quickstart2021.netty.chapter08;

import com.fsmflying.study.quickstart2021.netty.Common;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class ProxyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    ChannelFuture connectFuture;
    Map<Channel, ByteBuf> messageMap = new HashMap<>();

    //Bootstrap bootstrap;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        Bootstrap bootstrap = new Bootstrap();
        AttributeKey<String> message = AttributeKey.valueOf("message");
        bootstrap.channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        //super.channelRegistered(ctx);
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        //将从真实服务器收到的消息写回给客户端
                        //Common.writeAndFlush(ctx, true, "real server receive", true, msg);
                        System.out.println(Common.readByteBuf(msg));
                        //ctx.fireChannelReadComplete();
                        //ctx.pipeline().channel().eventLoop()

                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                        //super.channelReadComplete(ctx);

                    }
                });
        bootstrap.group(ctx.channel().eventLoop());
        bootstrap.attr(message, "appp");
        connectFuture = bootstrap.connect(new InetSocketAddress("localhost", 5555)).sync();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //super.channelInactive(ctx);
        System.out.println("");
        messageMap.put(ctx.channel(), null);

        //bootstrap.group().shutdownGracefully();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (connectFuture.isDone()) {
//            System.out.println("代理服务器收到客户端消息："
//                    + ((ByteBuf) msg).toString(Charset.forName("UTF-8")));
//            ;
            //将代理服务器收到的消息发送到真实的服务器
            connectFuture.channel().writeAndFlush(msg);
        }
    }
}
