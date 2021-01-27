package com.fsmflying.study.quickstart2021.io.nio.channel.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
//import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.Properties;

public class Startup {
    public static void main(String[] args) {
        //printEnviroment();
        //printProperties();
        printClassLoadEnviroment();


        RandomAccessFile aFile = null;
        try {
            //URL url = ClassLoader.getSystemClassLoader().getResource("./");
            URL url = ClassLoader.getSystemResource("./");
            String fileFullName = url.getPath().replace("target/classes/", "src/main/resources/csdn.10.txt");
            aFile = new RandomAccessFile(fileFullName, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = 0;
        try {
            bytesRead = inChannel.read(buf);//读取文件中的数据，写入到Buffer中，返回读取数量
            while (bytesRead != -1) {
                //System.out.println("Read " + bytesRead);
                buf.flip();//将buffer设置为读取模式
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());//打印字符
                }
                buf.clear();
                bytesRead = inChannel.read(buf);//继续从文件Channel中读取数据到Buffer中
            }
            aFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void printProperties() {
        Properties properties = System.getProperties();
        for (Object key : properties.keySet()
        ) {
            System.out.println(key + ":" + properties.get(key));
        }
    }

    public static void printEnviroment() {
        Map<String, String> envs = System.getenv();
        for (Map.Entry<String, String> e : envs.entrySet()
        ) {
            System.out.println(e.getKey() + ":" + e.getValue());
        }
    }

    private static void printClassLoadEnviroment() {
        URL url = ClassLoader.getSystemClassLoader().getResource("./");
        //URL url = ClassLoader.getSystemResource("./");
        String fileFullName = url.getPath().replace("target/classes/", "src/main/resources/csdn.10.txt");


    }
}
