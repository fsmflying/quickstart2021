package com.fsmflying.study.quickstart2021.netty.day02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Netty Nio服务器示例
 * Netty实战示例4-4
 */
public class NettyNioServer {

    public static void main(String[] args) {
        try {
            new NettyNioServer().server(9999);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
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
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            //将消息写到客户端,并添加 ChannelFutureListener,以便消息一被写完就关闭连接

                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(ChannelFutureListener.CLOSE);
                                        }

                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            super.channelRead(ctx, msg);
                                            ByteBuf buf = (ByteBuf) msg;
                                            while(buf.isReadable()){
                                                System.out.print((char)buf.readByte());
                                            }
                                            //ReferenceCountUtil.release(msg);
                                        }
                                    });
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
