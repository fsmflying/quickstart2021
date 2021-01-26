package com.fsmflying.study.quickstart2021.io.bio;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * strace -ff -o SocketIOServer.log /usr/local/jdk/jdk1.8.0_261/bin/java SocketIOServer
 */
public class SocketIOServer {
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
                        String stringForTime = null;
                        while (true) {
                            String dataLine = null;
                            try {
                                dataLine = bufferedReader.readLine();//阻塞2,等待数据
                                stringForTime = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(Calendar.getInstance().getTime());
                                bufferedWriter.write("[" + stringForTime + "][" + dataLine + "]" + ": received!\n");
                                bufferedWriter.flush();
                                if (null != dataLine && !"disc".equals(dataLine)) {
                                    System.out.println("[" + stringForTime + "][Receive Message]:" + dataLine);
                                } else {
                                    socket.close();
                                    break;
                                }
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
