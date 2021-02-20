package com.fsmflying.study.quickstart2021.netty;

import com.fsmflying.study.quickstart2021.netty.chapter10.ToIntegerDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class Chapter10EncodeAndDecodeTest {

    @Test
    public void test01_ToIntegerDecoder() {
//        ByteBuf buf = Unpooled.buffer();
//        for (int i = 0; i < 10; i++) {
//            buf.writeInt(i);
//        }

        EmbeddedChannel channel = new EmbeddedChannel(new ToIntegerDecoder());
        for (int i = 0; i < 10; i++) {
            channel.writeInbound(i);
        }

        for (int i = 0; i < 10; i++) {
            System.out.println((int) channel.readInbound());
        }
        channel.close();
    }
}
