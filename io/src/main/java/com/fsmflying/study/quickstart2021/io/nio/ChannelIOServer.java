package com.fsmflying.study.quickstart2021.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ChannelIOServer {
    public static final int PORT = 9091;
    public static void main(String[] args) {
       ServerSocketChannel serverSocketChannel;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
