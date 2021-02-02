package com.fsmflying.study.quickstart2021.io;

import com.fsmflying.study.quickstart2021.PathUtils;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class BufferTest {

    public String getResourcePath(String fileName) {
        return ClassLoader.getSystemResource(".").getPath()
                .replace("target/test-classes/", "src/main/resources/")
                + fileName;
    }

    public String getTestResourcePath(String fileName) {
        return ClassLoader.getSystemResource(".").getPath()
                .replace("target/test-classes/", "src/main/resources/")
                + fileName;
    }

    /**
     * 生成一引示例ByteBuffer,含有0-9,a-z
     * @return
     */
    public ByteBuffer getExampleByteBuffer(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < 10; i++) {
            byteBuffer.put((byte)('0'+i));
        }
        for (int i = 0; i < 26; i++) {
            byteBuffer.put((byte)('a'+i));
        }
        return byteBuffer;
    }

    public void printByteBuffer(ByteBuffer byteBuffer){
        while(byteBuffer.hasRemaining()){
            System.out.print((char)byteBuffer.get());
        }
        System.out.println();
    }

    public void printByteBuffer(ByteBuffer byteBuffer,int length){
        int i = 0;
        while(byteBuffer.hasRemaining() && (i<length)){
            System.out.print((char)byteBuffer.get());
            i++;
        }
        System.out.println();
    }

    public void printByteBufferStatus(ByteBuffer byteBuffer){
        System.out.println("****byteBuffer**Status**Start*******************");
        System.out.println("position:"+byteBuffer.position());
        System.out.println("limit:"+byteBuffer.limit());
        System.out.println("capacity:"+byteBuffer.capacity());
        System.out.println("****byteBuffer**Status**End*********************");
    }

    @Test
    public void test01_Buffer() {
        Buffer buffer = null;
        System.out.println();
    }

    @Test
    public void test02_ByteBuffer() throws FileNotFoundException {
        System.out.println(ClassLoader.getSystemResource(".").getPath());
        System.out.println(PathUtils.getResourcePath("csdn.10.txt"));
    }

    /**
     * 通过ByteBuffer读取文件，并打印到控制台
     *
     * @throws IOException
     */
    @Test
    public void test03_ByteBuffer() throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(PathUtils.getResourcePath("csdn.10.txt"), "rw");
        FileChannel fileChannel = accessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int num = fileChannel.read(byteBuffer);
        while (num > 0) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.print((char) byteBuffer.get());
            }
            byteBuffer.clear();
            num = fileChannel.read(byteBuffer);
        }

    }

    @Test
    public void test04_ByteBuffer_Write() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < 1024; i++) {
            byteBuffer.put((byte)(0x61+i%26));
        }

        byteBuffer.flip();
        byte[] bytes = new byte[1024];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes));
    }

    /**
     * 通过FileChannel向文件中写ByteBuffer内容
     */
    @Test
    public void test05_ByteBuffer_Write(){
        try {

            RandomAccessFile randomAccessFile = new RandomAccessFile(PathUtils.getResourcePath("test.txt"),"rw");
            FileChannel fileChannel = randomAccessFile.getChannel();


            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            for (int i = 0; i < 1023; i++) {
                byteBuffer.put((byte)(i%127));
            }

            byteBuffer.put((byte)'\n');

            byteBuffer.flip();
            randomAccessFile.seek(randomAccessFile.length());
            fileChannel.write(byteBuffer);
            //fileChannel.close();
            randomAccessFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * clear只是修改position limit capacity,实际数据并没有清除
     */
    @Test
    public void test06_ByteBuffer_clear(){
        ByteBuffer byteBuffer = getExampleByteBuffer();
        byteBuffer.flip();
        printByteBuffer(byteBuffer,15);

        byteBuffer.clear();
        printByteBuffer(byteBuffer);

    }

    /**
     *
     */
    @Test
    public void test07_ByteBuffer_compact(){
        ByteBuffer byteBuffer = getExampleByteBuffer();

        printByteBufferStatus(byteBuffer);
        byteBuffer.flip();
        System.out.println("====call flip");
        printByteBufferStatus(byteBuffer);

        printByteBuffer(byteBuffer,15);
        System.out.println("====读取15个字符后");
        System.out.println("====call printByteBuffer");
        printByteBufferStatus(byteBuffer);

        byteBuffer.compact();
        System.out.println("====call compact");
        printByteBufferStatus(byteBuffer);

        printByteBuffer(byteBuffer);
        System.out.println("====call printByteBuffer");
        printByteBufferStatus(byteBuffer);

    }

    @Test
    public void test08_ByteBuffer_rewind(){
        ByteBuffer byteBuffer = getExampleByteBuffer();
        byteBuffer.flip();
        printByteBuffer(byteBuffer,15);

        byteBuffer.rewind();
        printByteBuffer(byteBuffer);
    }

    @Test
    public void test09_ByteBuffer_mark_reset(){
        ByteBuffer byteBuffer = getExampleByteBuffer();

        byteBuffer.flip();
        System.out.println("====flip后");
        System.out.print("byteBuffer内容为:");
        printByteBuffer(byteBuffer);

        byteBuffer.rewind();
        System.out.print("读取15个字节内容为:");
        printByteBuffer(byteBuffer,15);


        printByteBufferStatus(byteBuffer);

        byteBuffer.mark();
        System.out.print("mark()后,读取5个字节内容为:");
        printByteBuffer(byteBuffer,5);

        byteBuffer.reset();
        System.out.print("reset()后,读取5个字节内容为:");
        printByteBuffer(byteBuffer,5);

    }


    @Test
    public void test11_CharBuffer(){
        Buffer buffer = null;
        CharBuffer charBuffer = CharBuffer.allocate(2048);
        charBuffer.put("012");
        charBuffer.flip();

        System.out.println(charBuffer.get());
        System.out.println(charBuffer.get());
        System.out.println(charBuffer.get());

    }

    @Test
    public void test12_String(){
        System.out.println(String.format("%s %s","a","b"));
    }

}
