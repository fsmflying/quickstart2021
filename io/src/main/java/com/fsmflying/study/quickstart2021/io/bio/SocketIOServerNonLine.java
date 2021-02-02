package com.fsmflying.study.quickstart2021.io.bio;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * strace -ff -o SocketIOServer.log /usr/local/jdk/jdk1.8.0_261/bin/java SocketIOServer
 */
public class SocketIOServerNonLine {
    public static boolean isReadLine = false;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
        try {
            serverSocket = new ServerSocket(9090);
            while (true) {
                final Socket socket = serverSocket.accept();//阻塞1,等待连接
                System.out.println("established connection local port [" + socket.getLocalPort()
                        + "],remote [" + socket.getRemoteSocketAddress() + ":" + socket.getPort() + "]");
                new Thread() {

                    @Override
                    public void run() {
                        InputStream inputStream = null;
                        OutputStream outputStream = null;
                        try {
                            inputStream = socket.getInputStream();
                            outputStream = socket.getOutputStream();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                        StringBuilder stringBuilderTime = new StringBuilder(16);
                        StringBuilder stringBuilder = new StringBuilder(2048);
                        char[] chars = new char[2048];
                        while (true) {
                            String dataLine = null;
                            try {
                                bufferedReader.read(chars);
                                dataLine = new String(chars);
                                if ("[[disc]]".equals(dataLine)) {
                                    bufferedWriter.write("[[bye]]");
                                    bufferedWriter.flush();
                                    socket.close();
                                    break;
                                }

                                else {
                                    stringBuilderTime.append(dateTimeFormatter.format(ZonedDateTime.now()));
                                    bufferedWriter.write("[" + stringBuilderTime.toString() + "][" + dataLine + "]:received");
                                    bufferedWriter.flush();
                                    System.out.println("[" + stringBuilderTime.toString() + "][Receive Message]:" + dataLine);
                                    stringBuilderTime.delete(0, stringBuilder.length());

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
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
