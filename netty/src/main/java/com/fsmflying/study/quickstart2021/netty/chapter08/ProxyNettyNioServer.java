package com.fsmflying.study.quickstart2021.netty.chapter08;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Netty Nio服务器示例
 * Netty实战示例4-4
 */
public class ProxyNettyNioServer {

    public static void main(String[] args) {
        try {
            new ProxyNettyNioServer().server(9999);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        //使用 OioEventLoopGroup 以允许阻塞模式(旧的I/O)
        EventLoopGroup parent = new NioEventLoopGroup();
        try {
            //创建 ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(parent, new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //指定 ChannelInitializer对于每个已接受的连接都调用它
                    .childHandler(new ProxyServerHandler());
            //绑定服务器以接受连接
            ChannelFuture f = b.bind().sync();
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (f.isSuccess()) {
                        System.out.println("Server bound succeed!");
                    } else {
                        System.out.println("Server bound failed !");
                        future.cause().printStackTrace();
                    }

                }
            });
            f.channel().closeFuture().sync();

        } finally {
            //释放所有的资源
            parent.shutdownGracefully().sync();

        }
    }
}
