package com.fsmflying.study.quickstart2021.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChannelIOServer {
    public static final int PORT = 9091;

    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);

//            Selector selector = Selector.open();
//            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            List<SocketChannel> clients = new ArrayList<>();
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();//接受连接
                if (null != socketChannel) {
                    socketChannel.configureBlocking(false);
                    System.out.println("接受连接[" + socketChannel.getRemoteAddress() + ":" + socketChannel.socket().getPort() + "]");
                    clients.add(socketChannel);
                }

                ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                StringBuffer stringBufferForTime = new StringBuffer(2048);
                StringBuilder stringBuilderForTime = new StringBuilder(2048);
                for (int i = 0; i < clients.size(); i++) {
                    SocketChannel sc = clients.get(i);
                    int num = sc.read(byteBuffer);//读取数据到Buffer
                    if (num > 0) {
                        System.out.print("收到来自[" + sc.getRemoteAddress() + ":" + sc.socket().getPort() + "]的数据:");
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.limit()];
                        byteBuffer.get(bytes);//从Bufffer读取数据到bytes
                        System.out.println(new String(bytes));

                        //响应信息
                        byteBuffer.clear();
                        stringBufferForTime.append(new SimpleDateFormat("[yyyy-MM-dd HH:MM:SS]:").format(Calendar.getInstance().getTime()));
                        stringBufferForTime.append(new String(bytes));

//                        stringBuilderForTime.append(new SimpleDateFormat("[yyyy-MM-dd HH:MM:SS]:").format(Calendar.getInstance().getTime()));
//                        stringBuilderForTime.append(new String(bytes));

                        byteBuffer.put(stringBufferForTime.toString().getBytes());
//                        byteBuffer.put(stringBuilderForTime.toString().getBytes());

                        byteBuffer.flip();
                        sc.write(byteBuffer);
                        //sc.

                        //清除
                        stringBufferForTime.delete(0,stringBufferForTime.length());
                        stringBufferForTime.delete(0,stringBuilderForTime.length());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
