package com.fsmflying.study.quickstart2021.netty.day02;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Oio服务器示例 java.net
 * Netty实战示例4-1
 */
public class PlainOioServer {
    public static void serve() {
        final ServerSocket socket;
        try {
            //将服务器绑定指定的端口
            socket = new ServerSocket(9999);
            for (;;) {
                //接收连接，阻塞1
                final Socket clientSocket = socket.accept();
                System.out.println(
                        "Accepted connection from " + clientSocket);
                //创建一个新的线程来处理该连接
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            //将消息写给已连接的客户端
                            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                //关闭连接
                                clientSocket.close();
                            } catch (IOException ex) {
                                // ignore on close
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
