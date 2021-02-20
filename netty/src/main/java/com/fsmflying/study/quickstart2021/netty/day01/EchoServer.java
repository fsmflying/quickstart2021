package com.fsmflying.study.quickstart2021.netty.day01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 服务器引导
 * Netty实战示例2-2
 */
public class EchoServer {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new EchoServer(9999).start();
    }

    public void start() {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        //1.创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //2.创建ServerBootstrap,即服务器
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)//3.指定所使用的Nio传输Channel
                    .localAddress(new InetSocketAddress(port))//4.使用指定的端口进行监听
                    //5.添加一个ChannelHandler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ctx.writeAndFlush(buf.duplicate());
                            logger.debug("connected from [" + ctx.channel().remoteAddress() + "]");
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            super.channelInactive(ctx);
                            logger.debug("[" + ctx.channel().remoteAddress() + "][inactive]");
                        }

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //6.echoServerHandler被标注为Sharable,因此总是使用相同的实例
                            ch.pipeline().addLast(echoServerHandler);
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            super.exceptionCaught(ctx, cause);
                        }
                    });
            //异步地绑定服务，并阻塞到绑定完成
            ChannelFuture f = b.bind().sync();
            //获取channel的closeFuture,并阻塞当前线程,直到它完成
            f.channel().closeFuture().sync();
            //关闭EventLoopGroup，释放所有资源
            group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
