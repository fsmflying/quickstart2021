package com.fsmflying.study.quickstart2021.netty;

import io.netty.buffer.*;
import org.junit.Test;

import java.nio.ByteBuffer;

public class ByteBufTest {
    @Test
    public void test01_ByteBuf_Unpool() {
        ByteBuf heapBuffer = Unpooled.buffer();
        heapBuffer.writeBoolean(true);
        heapBuffer.writeByte('0');
        heapBuffer.writeBytes("123".getBytes());
        System.out.println(heapBuffer.hasArray());
        if (heapBuffer.hasArray()) {
            byte[] bytes = heapBuffer.array();
            int offset = heapBuffer.arrayOffset() + heapBuffer.readerIndex();
            int length = heapBuffer.readableBytes();
            System.out.println("arrayOffset:" + heapBuffer.arrayOffset());
            System.out.println("readerIndex:" + heapBuffer.readerIndex());
            System.out.println("offset:" + offset);
            System.out.println("readableBytes:" + heapBuffer.readableBytes());
            System.out.println("writerIndex:" + heapBuffer.writerIndex());
            handleArray(bytes, offset, length);

        }
    }

    public void handleArray(byte[] array, int offset, int length) {

    }

    @Test
    public void test02_ByteBuf_Direct() {
        ByteBuf directBuffer = Unpooled.directBuffer();
        directBuffer.writeBytes("0123".getBytes());
        System.out.println(directBuffer.hasArray());
        if (!directBuffer.hasArray()) {
            int length = directBuffer.readableBytes();
            byte[] array = new byte[length];
            directBuffer.getBytes(directBuffer.readerIndex(), array);
            System.out.println("readableBytes:" + directBuffer.readableBytes());
            System.out.println("readerIndex:" + directBuffer.readerIndex());
            System.out.println("writerIndex:" + directBuffer.writerIndex());
            System.out.println("writableBytes:" + directBuffer.writableBytes());
        }

    }

    @Test
    public void test03_CompositeByteBuf_ByteBuffer() {
        ByteBuffer headers = ByteBuffer.allocate(2 * 1024);
        ByteBuffer body = ByteBuffer.allocate(2 * 1024);

        headers.put("GET / HTTP/1.1;".getBytes());
        headers.put("keep-aliveGET / HTTP/1.1;".getBytes());

        body.put("<!DOCTYPE html><!--STATUS OK-->".getBytes());

        ByteBuffer[] parts = new ByteBuffer[]{headers, body};
        ByteBuffer message = ByteBuffer.allocate(headers.remaining() + body.remaining());
        message.put(headers);
        message.put(body);
        message.flip();
        byte[] bytes = new byte[message.remaining()];
        message.get(bytes);
        System.out.println(new String(bytes));
    }

    @Test
    public void test04_CompositeByteBuf() {
        CompositeByteBuf message = Unpooled.compositeBuffer();
        ByteBuf headers = Unpooled.buffer();
        ByteBuf body = Unpooled.buffer();
        message.addComponents(headers, body);

        headers.writeBytes("GET / HTTP/1.1;".getBytes());
        headers.writeBytes("keep-aliveGET / HTTP/1.1;".getBytes());

        body.writeBytes("<!DOCTYPE html><!--STATUS OK-->".getBytes());

        for (ByteBuf b : message) {
            System.out.println(b.toString());
        }

        message.removeComponent(0);
        for (ByteBuf b : message) {
            System.out.println(b.toString());
        }
    }

    /**
     *
     */
    @Test
    public void test05_CompositeByteBuf() {
        CompositeByteBuf message = Unpooled.compositeBuffer();
        ByteBuf headers = Unpooled.buffer();
        ByteBuf body = Unpooled.buffer();
        message.addComponents(headers, body);

        headers.writeBytes("GET / HTTP/1.1;".getBytes());
        headers.writeBytes("keep-aliveGET / HTTP/1.1;".getBytes());

        body.writeBytes("<!DOCTYPE html><!--STATUS OK-->".getBytes());

        int length = message.readableBytes();
        byte[] array = new byte[length];

        message.getBytes(message.readerIndex(), array);
        System.out.println(new String(array));

    }

    /**
     * 读取数据，不改变读写索引
     */
    @Test
    public void test06_ByteBuf_read() {
        ByteBuf heapBuffer = Unpooled.buffer();
        heapBuffer.writeBoolean(true);
        heapBuffer.writeByte('0');
        heapBuffer.writeBytes("123".getBytes());
        printByteBufContent(heapBuffer);
    }


    @Test
    public void test07_ByteBuf_discard() {
        ByteBuf heapBuffer = Unpooled.buffer();
        //heapBuffer.writeBytes("0123456789abcdefghijklmnopqrstuvwxyz".getBytes());
        heapBuffer.writeBytes("0123456789".getBytes());
        heapBuffer.writeBytes("abcdefg".getBytes());
        heapBuffer.writeBytes("hijklmn".getBytes());
        heapBuffer.writeBytes("opqrst".getBytes());
        heapBuffer.writeBytes("uvwxyz".getBytes());

        //
        printByteBufIndex(heapBuffer);

        System.out.println("====call 10 readByte=====");
        readAndPrintCharOfByteBuf(heapBuffer, 0, 10);
        printByteBufIndex(heapBuffer);

        heapBuffer.discardReadBytes();
        System.out.println("====discardReadBytes=====");
        printByteBufIndex(heapBuffer);
        System.out.println("====call 10 readByte=====");
        readAndPrintCharOfByteBuf(heapBuffer, 0, 10);
        printByteBufIndex(heapBuffer);


    }

    /**
     * 打印读索引和写索引，不改变它们
     *
     * @param buf
     */
    public void printByteBufIndex(ByteBuf buf) {

        System.out.println("----printByteBufIndex----START------------");
        if (buf != null) {
            System.out.println("readerIndex:" + buf.readerIndex());
            System.out.println("writerIndex:" + buf.writerIndex());
            System.out.println("capacity:" + buf.capacity());
        }

        System.out.println("----printByteBufIndex----END--------------");
    }

    /**
     * 在不改变读写索引的情况下，打印ByteBuf所有可读内容，从第0位开始读取
     * 使用readableBytes判定读取长度
     * @param buf
     */
    public void printByteBufContent(ByteBuf buf) {
        System.out.println("----printByteBufContent----START------------");
        for (int i = 0; i < buf.readableBytes(); i++) {
            System.out.print(String.format("%c", buf.getByte(i)));
        }
        System.out.println();
        System.out.println("----printByteBufContent----END--------------");
    }

    /**
     * 在不改变读写索引的情况下，打印ByteBuf所有可读内容，从第0位开始读取
     * 使用isReadable判定是否还有字节可读
     * @param buf
     */
    public void readAndPrintByteOfByteBufContent2(ByteBuf buf) {
        while(buf.isReadable()){
            System.out.print(Integer.toHexString(buf.readByte()));
            System.out.print(' ');
        }
        System.out.println();
    }

    /**
     * 在不改变读写索引的情况下，打印ByteBuf所有可读内容，从第0位开始读取
     * 使用isReadable判定是否还有字节可读
     * @param buf
     */
    public void readAndPrintCharOfByteBufContent2(ByteBuf buf) {
        while(buf.isReadable()){
            System.out.print((char)buf.readByte());
            System.out.print(' ');
        }
        System.out.println();
    }

    /**
     * 读取并打印ByteBuf的部分或全部内容
     *
     * @param buf    ByteBuf实例
     * @param offset 读取开始偏移位置
     * @param length 读取长度
     */
    public void readAndPrintCharOfByteBuf(ByteBuf buf, int offset, int length) {
        if (buf == null) return;
        else if (offset >= buf.readableBytes()) return;
        int start = offset, end = Integer.min(offset + length - 1, buf.readableBytes() - 1);
        buf.readerIndex(offset);
        for (int i = start; i <= end; i++) {
            System.out.print(String.format("%c", buf.readByte()));
        }
        System.out.println();
    }


    @Test
    public void test08_ByteBuf() {
        ByteBuf heapBuffer = Unpooled.buffer();
        heapBuffer.writeBytes("0123456789abcdefghijklmnopqrstuvwxyz".getBytes());
        System.out.println("====heapBuffer content:====");
        printByteBufContent(heapBuffer);
        printByteBufIndex(heapBuffer);

        System.out.println("====read 5 bytes:====");
        readAndPrintCharOfByteBuf(heapBuffer,0,5);
        System.out.println("====heapBuffer content:====");
        printByteBufContent(heapBuffer);
        printByteBufIndex(heapBuffer);


        readAndPrintCharOfByteBuf(heapBuffer, 0, 10);
        heapBuffer.discardReadBytes();
        System.out.println();
        printByteBufIndex(heapBuffer);

    }

    @Test
    public void test09_ByteBuf_read(){
        ByteBuf heapBuffer = Unpooled.buffer();
        heapBuffer.writeBytes("0123456789abcdefghijklmnopqrstuvwxyz".getBytes());
        printByteBufIndex(heapBuffer);
        System.out.println();
        readAndPrintByteOfByteBufContent2(heapBuffer);
        System.out.println();
        printByteBufIndex(heapBuffer);

    }

    @Test
    public void test10_ByteBuf_writeData(){
        ByteBuf heapBuffer = Unpooled.buffer();

        heapBuffer.writeByte(0x12);
        System.out.println("####After call writeByte(0x12)####");
        printByteBufIndex(heapBuffer);

        heapBuffer.clear();
        System.out.println("####After call clear()####");
        printByteBufIndex(heapBuffer);

        heapBuffer.clear();
        System.out.println("####After call writeShort(0x1234)####");
        heapBuffer.writeShort(0x1234);
        printByteBufIndex(heapBuffer);
        readAndPrintByteOfByteBufContent2(heapBuffer);

        heapBuffer.clear();
        System.out.println("####After call writeInt(0x12345678)####");
        heapBuffer.writeInt(0x12345678);
        printByteBufIndex(heapBuffer);
        readAndPrintByteOfByteBufContent2(heapBuffer);

    }


    @Test
    public void test11_ByteBuf_clear(){
        ByteBuf heapBuffer = Unpooled.buffer();
        heapBuffer.writeBytes("0123456789abcdefghijklmnopqrstuvwxyz".getBytes());
        readAndPrintByteOfByteBufContent2(heapBuffer);
        printByteBufIndex(heapBuffer);

        System.out.println("====================================");
        System.out.println("####After call clear()####");
        heapBuffer.clear();
        readAndPrintByteOfByteBufContent2(heapBuffer);
        printByteBufIndex(heapBuffer);

        for (int i = 0; i < 36; i++) {
            System.out.print((char)heapBuffer.getByte(i));
            System.out.print(' ');
        }
        System.out.println();
        System.out.println("####isReadable####");
        int index = 0;
        while(heapBuffer.isReadable()){
            System.out.print((char)heapBuffer.getByte(index));
            System.out.print(' ');

        }
        System.out.println();

    }

    public void test12_ByteBuf_Pooled(){
    }

    public void test13_ByteBufHolder(){
        ByteBufHolder byteBufHolder = null;
    }

    @Test
    public void test14_ByteBuf_refCnt(){
        System.out.println("####test14_ByteBuf_refCnt####START#######################################");
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes("0123456789abcdefghijklmnopqrstuvwxyz".getBytes());
        System.out.println(ByteBufUtil.hexDump(buffer));

        printByteBufIndex(buffer);
        printByteBufContent(buffer);
        System.out.println(buffer.refCnt());
        ByteBuf copyOfBuffer = buffer.duplicate();
        System.out.println(buffer.refCnt());
        System.out.println("####test14_ByteBuf_refCnt####END#########################################");

    }
}
