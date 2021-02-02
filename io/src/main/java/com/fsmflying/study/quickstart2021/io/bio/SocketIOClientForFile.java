package com.fsmflying.study.quickstart2021.io.bio;

import com.fsmflying.study.quickstart2021.PathUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketIOClientForFile {
    public static void main(String[] args) {

        Socket socket = null;
        BufferedReader fileBufferedReader = null;
        try {
            socket = new Socket("localhost",9090);
            String fullFileName = PathUtils.getResourcePath("csdn.10.txt");
            fileBufferedReader = new BufferedReader(new FileReader(fullFileName));//java.io.

            BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            /**
             * 用于打印返回消息
             */
            Thread outputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String responeMsg = null;
                    while (true){
                        try {
                            if (!!"[[bye]]".equals(responeMsg=socketBufferedReader.readLine())) break;
                            else{
                                System.out.println(responeMsg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            outputThread.start();

            String line = null;
            byte[] bytes = new byte[1024];
            while (null != (line = fileBufferedReader.readLine())) {

                System.out.print("发送消息:" + line);
                socketBufferedWriter.write(line+"\n");
                socketBufferedWriter.flush();

                System.out.println(new String(bytes));


            }
            //断开连接
            socketBufferedWriter.write("[[disc]]\n");
            socketBufferedWriter.flush();

            outputThread.join();

            //fileBufferedReader.close();
            //socket.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(socket.isConnected()){
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
