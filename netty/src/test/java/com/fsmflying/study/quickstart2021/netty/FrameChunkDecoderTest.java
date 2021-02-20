package com.fsmflying.study.quickstart2021.netty;

import com.fsmflying.study.quickstart2021.netty.chapter09.FrameChunkDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

//        ByteBuf tempBuf = input.readBytes(2);
//        Common.printByteBufContent(tempBuf);
//        Common.printByteBufIndex(tempBuf);
        assertTrue(channel.writeInbound(input.readBytes(2)));

        try {
            //写入一个4字节大小 的帧,并捕获预期的TooLongFrameException
            channel.writeInbound(input.readBytes(4));
            //如果上面没有抛出异常,那么就会到达这个断言,并且测试失败
            Assert.fail();

        } catch (TooLongFrameException e) {
            // expected exception
        }
        //写入剩余的 2 字节,并断言将会产生一个有效帧
        assertTrue(channel.writeInbound(input.readBytes(3)));
        assertTrue(channel.finish());//将该 Channel 标记为已完成状态

        // Read frames
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(2), read);
        read.release();

        //读取产生的消息,并且验证值
        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();
        buf.release();
    }
}
