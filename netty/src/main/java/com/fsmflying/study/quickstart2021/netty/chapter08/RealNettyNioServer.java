package com.fsmflying.study.quickstart2021.netty.chapter08;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Netty Nio服务器示例
 * Netty实战示例4-4
 */
public class RealNettyNioServer {

    public static void main(String[] args) {
        try {
            new RealNettyNioServer().server(5555);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void server(int port) throws Exception {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss.SSS");
        //使用 OioEventLoopGroup 以允许阻塞模式(旧的I/O)
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建 ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //指定 ChannelInitializer对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //添加一个 ChannelInboundHandlerAdapter 以拦截和处理事件
                            ch.pipeline().addLast(new RealServerHandler());
                        }

                    });
            //绑定服务器以接受连接
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();

        } finally {
            //释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
