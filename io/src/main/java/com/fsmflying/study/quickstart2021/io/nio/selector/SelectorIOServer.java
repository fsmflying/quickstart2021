package com.fsmflying.study.quickstart2021.io.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SelectorIOServer {
    public static Map<SocketChannel, String> dataMap = new HashMap<>();

    public static void printSelectionKeyStatus(SelectionKey selectionKey) {
        System.out.println("****START****SELECTIONkEY****STATUS*****************************");
        System.out.println("##isAcceptable:" + selectionKey.isAcceptable());
        System.out.println("##isConnectable:" + selectionKey.isConnectable());
        System.out.println("##isReadable:" + selectionKey.isReadable());
        System.out.println("##isWritable:" + selectionKey.isWritable());
        System.out.println("##isValid:" + selectionKey.isValid());
        System.out.println("****END******SELECTIONkEY****STATUS*****************************");

    }

    public static void main(String[] args) {
        Selector selector = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
        try {
            selector = Selector.open();
            ServerSocketChannel listenSocketChannel = ServerSocketChannel.open();
            listenSocketChannel.bind(new InetSocketAddress(9999));
            listenSocketChannel.configureBlocking(false);
            SelectionKey selectorKey = listenSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                int readyOps = selector.select();
                if (readyOps == 0) continue;
                System.out.println("readOps:" + readyOps);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();
                System.out.println("=====START==============================================");
                ByteBuffer requestByteBuffer = ByteBuffer.allocate(2 * 1024);
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    printSelectionKeyStatus(selectionKey);
                    if (!selectionKey.isValid()) continue;
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);

                        client.write(ByteBuffer.wrap("Hello!\n".getBytes("UTF-8")));
//                        client.close();
//                        selectionKey.cancel();
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        requestByteBuffer.clear();
                        int numRead = client.read(requestByteBuffer);
                        System.out.println("numRead:"+numRead);
                        //requestByteBuffer.flip();
                        byte[] bytes = new byte[requestByteBuffer.limit()];
                        requestByteBuffer.get(bytes);
                        String receiveMsg = new String(bytes);
                        if ('\n' == bytes[bytes.length - 1]) {
                            System.out.println("message ends with \n");
                        }
                        dataMap.put(client, receiveMsg);
                        System.out.println("[收到消息]:" + receiveMsg);

                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    } else if (selectionKey.isWritable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        String receiveMsg = dataMap.get(client);
                        dataMap.remove(client);

                        ByteBuffer responseByteBuffer = ByteBuffer.allocate(2048);
                        String responseMessage = String.format("[%s][%s]:received !\n",
                                dateTimeFormatter.format(ZonedDateTime.now()),
                                receiveMsg);
                        responseByteBuffer.put(responseMessage.getBytes());

                        responseByteBuffer.flip();
                        client.write(responseByteBuffer);
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    }
                }
                System.out.println("=====END================================================");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (selector != null)
                    selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
