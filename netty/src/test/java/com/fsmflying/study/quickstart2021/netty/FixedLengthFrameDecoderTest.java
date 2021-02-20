package com.fsmflying.study.quickstart2021.netty;

import com.fsmflying.study.quickstart2021.netty.chapter09.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

public class FixedLengthFrameDecoderTest {


    @Test
    public void test01_ByteBuf_duplicate() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 26; i++) {
            buf.writeByte(0x41 + i);
        }
        Common.printByteBufContent(buf);
        Common.printByteBufStatus(buf);
        System.out.println("1:读取buf中5个字节后,buf内容");
        Common.readAndPrintCharOfByteBuf(buf, 0, 5);
        Common.printByteBufStatus(buf);

        System.out.println("2:通过buf.duplicate()创建新的ByteBuf(input)后，input内容:");
        ByteBuf input = buf.duplicate();
        Common.printByteBufContent(input);
        Common.printByteBufStatus(input);

        System.out.println("3:再读取buf中5个字节后,buf内容");
        Common.readAndPrintCharOfByteBuf(buf, 5);
        Common.printByteBufStatus(buf);

        System.out.println("4:input内容");
        Common.printByteBufContent(input);
        Common.printByteBufStatus(input);


    }

    @Test
    public void test01_ByteBuf_copy() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 26; i++) {
            buf.writeByte(0x41 + i);
        }
        Common.printByteBufContent(buf);
        Common.printByteBufStatus(buf);
        System.out.println("1:读取buf中5个字节后,buf内容");
        Common.readAndPrintCharOfByteBuf(buf, 0, 5);
        Common.printByteBufStatus(buf);

        System.out.println("2:通过buf.copy()创建新的ByteBuf(input)后，input内容:");
        ByteBuf input = buf.copy();
        Common.printByteBufContent(input);
        Common.printByteBufStatus(input);

        System.out.println("3:再读取buf中5个字节后,buf内容");
        Common.readAndPrintCharOfByteBuf(buf, 5);
        Common.printByteBufStatus(buf);

        System.out.println("4:input内容");
        Common.printByteBufContent(input);
        Common.printByteBufStatus(input);

    }


    @Test
    public void test01_ByteBuf_retain() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 26; i++) {
            buf.writeByte(0x41 + i);
        }
        System.out.println(">>>1.1 buf初始内容：");
        Common.printByteBufContent(buf);
        System.out.println(">>>1.2 buf初始状态：");
        Common.printByteBufStatus(buf);
        System.out.println(">>>2.1 读取buf 5个字节内容");
        Common.readAndPrintCharOfByteBuf(buf, 5);
        System.out.println(">>>2.2 buf状态：");
        Common.printByteBufStatus(buf);

        System.out.println(">>>3.1 通过retain创建input01后，input01内容:");
        ByteBuf input01 = buf.retain();
        Common.printByteBufContent(input01);
        System.out.println(">>>3.2 input01状态：");
        Common.printByteBufStatus(input01);

        System.out.println(">>>4.1 读取buf 5个字节内容");
        Common.readAndPrintCharOfByteBuf(buf, 5);
        System.out.println(">>>4.2 buf状态：");
        Common.printByteBufStatus(buf);

        System.out.println(">>>5.1 通过retain创建input02后，input02内容:");
        ByteBuf input02 = buf.retain();
        Common.printByteBufContent(input02);
        System.out.println(">>>5.2 input02状态：");
        Common.printByteBufStatus(input02);

        System.out.println(">>>6.1 读取buf 5个字节内容");
        Common.readAndPrintCharOfByteBuf(buf, 5);
        System.out.println(">>>6.2 buf状态：");
        Common.printByteBufStatus(buf);

    }

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(0x31 + i);
        }

        ByteBuf input = buf.duplicate();
        //创建一个EmbeddedChannel,并添加一个FixedLengthFrameDecoder,其将以3字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        //将数据写到channel
        assertTrue(channel.writeInbound(input.retain()));

        //标记Channel为已完成状态
        assertTrue(channel.finish());

        //读取所生成的消息,并且验证是否有3帧(切片),其中每帧(切片)都为3字节
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }


    @Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        ByteBuf input = buf.duplicate();
        //创建一个EmbeddedChannel,并添加一个FixedLengthFrameDecoder,其将以3字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        //返回 false,因为没有一个完整的可供读取的帧
        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(7)));

        assertTrue(channel.finish());
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();

    }
}
