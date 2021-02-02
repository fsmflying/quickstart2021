package com.fsmflying.study.quickstart2021.io.bio;

import java.io.*;
import java.net.Socket;
import java.nio.CharBuffer;

/**
 * 客户端
 */
public class SocketIOClientCharBuffer {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 9090);
            BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //read from socket
            BufferedWriter socketBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //write to socket
            BufferedReader keyboardBufferedReader = new BufferedReader(new InputStreamReader(System.in));//read from keyboard

            /**
             * 用于打印返回消息
             */
            Thread outputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String responeMsg = null;
                    CharBuffer charBuffer = CharBuffer.allocate(2048);
                    while (true) {
                        try {
                            socketBufferedReader.read(charBuffer);
                            charBuffer.flip();
                            if (!"[[bye]]".equals(responeMsg =charBuffer.toString())) break;
                            else {
                                System.out.println(responeMsg);
                            }
                            charBuffer.compact();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            outputThread.start();
            String line = null;
            CharBuffer buffer = CharBuffer.allocate(1024);
            while (true) {
                //line = keyboardBufferedReader.readLine();
                keyboardBufferedReader.read(buffer);
                line = buffer.toString();

                if ("[[disc]]".equals(line)) {
                    //通知服务断开连接
                    socketBufferedWriter.write( "[[disc]]\n"); //write keyboard content to socket
                    socketBufferedWriter.flush();
                    break;
                } else {
                    //发送消息
                    buffer.put('\n');
                    socketBufferedWriter.write(buffer.array());
                    socketBufferedWriter.flush();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket.isConnected()) {
                try {
                    Thread.sleep(1000);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
