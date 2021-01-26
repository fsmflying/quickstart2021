package com.fsmflying.study.quickstart2021.io.bio;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SocketIOClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",9090);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //read from socket
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //write to socket
            BufferedReader bufferedReaderForKeyBoard = new BufferedReader(new InputStreamReader(System.in));//read from keyboard
            bufferedWriter.write(bufferedReaderForKeyBoard.readLine()); //write keyboard content to socket
            String stringForTime = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(Calendar.getInstance().getTime());
            System.out.println("["+stringForTime+"]:"+bufferedReader.readLine());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
