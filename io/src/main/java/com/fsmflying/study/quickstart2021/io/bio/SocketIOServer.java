package com.fsmflying.study.quickstart2021.io.bio;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * strace -ff -o SocketIOServer.log /usr/local/jdk/jdk1.8.0_261/bin/java SocketIOServer
 */
public class SocketIOServer {
    public static boolean isReadLine = false;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9090);
            while (true) {
                final Socket socket = serverSocket.accept();//阻塞1,等待连接
                System.out.println("established connection local port [" + socket.getLocalPort()
                        + "],remote [" + socket.getRemoteSocketAddress() + ":" + socket.getPort() + "]");
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = socket.getInputStream();
                            OutputStream outputStream = socket.getOutputStream();

                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                            String stringForTime = null;
                            while (true) {
                                String dataLine = null;
                                try {
                                    dataLine = bufferedReader.readLine();//阻塞2,等待数据
                                    if (dataLine == null || dataLine.isEmpty()) continue;
                                    if ("[[disc]]".equals(dataLine)) {
                                        bufferedWriter.write("[[bye]]\n");
                                        bufferedWriter.flush();
                                        socket.close();
                                        break;
                                    } else {
                                        stringForTime = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(Calendar.getInstance().getTime());
                                        bufferedWriter.write("[" + stringForTime + "][" + dataLine + "]:received\n");
                                        bufferedWriter.flush();
                                        System.out.println("[" + stringForTime + "][Receive Message]:" + dataLine);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                Thread.sleep(5000);
                                if (socket.isConnected()) {
                                    socket.close();
                                }
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
