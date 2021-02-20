package com.fsmflying.study.quickstart2021.netty.chapter13;

import com.fsmflying.study.quickstart2021.PathUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        //引导该NioDatagramChannel(无连接的)
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                //设置 SO_BROADCAST套接字选项
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));

        this.file = file;
    }

    public void run() throws Exception {
        Channel ch = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        for (; ; ) {//启动主处理循环
            long len = file.length();

            if (len < pointer) {
                // file was reset
                //如果有必要,将文件指针设置到该文件的最后一个字节
                pointer = len;
            } else if (len > pointer) {
                // Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                //设置当前的文件指针, 以确保没有任何的旧日志被发送
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    //对于每个日志条目,写入一个 LogEvent到 Channel 中
                    ch.writeAndFlush(new LogEvent(null, -1,
                            file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer();
                raf.close();
                //存储其在文件中的当前位置
            }

            try {
                //休眠1秒, 如果被中断, 则退出循环; 否则重新处理它
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
//        if (args.length != 2) {
//            throw new IllegalArgumentException();
//        }
        int port = 9999;
        String fullFileName = null;
        try {
//            fullFileName = PathUtils.getResourcePath("csdn.10.txt");
            fullFileName = PathUtils.getResourcePath("csdn.24000.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255",port),
                new File(fullFileName));
        try {
            //创建并启动一个新的的LogEventBroadcaster实例
            broadcaster.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            broadcaster.stop();
        }
    }
}
