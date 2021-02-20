package com.fsmflying.study.quickstart2021.netty.chapter13;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * 日志监视器
 *
 * mvn clean package exec:exec -PLogEventMonitor
 */
public class LogEventMonitor {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;

    public LogEventMonitor(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                //引导该 NioDatagramChannel
                .channel(NioDatagramChannel.class)
                //设置套接字选项SO_BROADCAST
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel)
                            throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //将LogEventDecoder 和LogEventHandler 添加到 ChannelPipeline中
                        pipeline.addLast(new LogEventDecoder());
                        pipeline.addLast(new LogEventHandler());
                    }
                })
                .localAddress(address);
    }

    public Channel bind() {
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            throw new IllegalArgumentException(
//                    "Usage: LogEventMonitor <port>");
//        }
        int port = 9999;
        //构造一个新的LogEventMonitor
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(port));
        try {
            Channel channel = monitor.bind();
            System.out.println("LogEventMonitor running");
            channel.closeFuture().sync();
        } finally {
            monitor.stop();
        }
    }
}
