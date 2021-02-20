package com.fsmflying.study.quickstart2021.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class CombinedChannelDuplexHandlerTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }


    public ByteBuf getExampleByteBuf() {
        ByteBuf buf = Unpooled.buffer();
//        for (int i = 0; i < 10; i++) {
//            buf.writeShort(0x30 + i);
////            buf.writeByte(0x30 + i);
//        }
        for (int i = 0; i < 26; i++) {
            buf.writeShort(0x41 + i);
//            buf.writeByte(0x41 + i);
        }
        for (int i = 0; i < 26; i++) {
            buf.writeShort(0x61 + i);
//            buf.writeByte(0x61 + i);
        }
        return buf;
    }

    @Test
    public void test01_ByteToCharDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new ByteToCharDecoder());
        ByteBuf buf = getExampleByteBuf();
        Common.printByteBufContent(buf);
        Common.printByteBufStatus(buf);

        channel.writeInbound(buf.readBytes(2));
        channel.writeInbound(buf.readBytes(2));
        System.out.println("[[the first  character]]:" + (Character) channel.readInbound());
        System.out.println("[[the second character]]:" + (Character) channel.readInbound());


        buf.release();
    }

    @Test
    public void test02_CharToByteDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new CharToByteEncoder());
//        channel.writeOutbound(new Character('A'));
//        channel.writeOutbound(new Character('B'));
//        channel.writeOutbound(new Character('C'));;
        channel.writeOutbound('A', 'B', 'C', 'D');

        ByteBuf c1 = channel.readOutbound();
        Common.printByteBufContentWithHex(c1);
        Common.printByteBufStatus(c1);
        c1.release();

        ByteBuf c2 = channel.readOutbound();
        Common.printByteBufContentWithHex(c2);
        Common.printByteBufStatus(c2);
        c2.release();

        ByteBuf c3 = channel.readOutbound();
        Common.printByteBufContentWithHex(c3);
        Common.printByteBufStatus(c3);
        c3.release();

    }

    @Test
    public void test03_CombinedByteCharCodec_Inbound() {
        EmbeddedChannel channel = new EmbeddedChannel(new CombinedByteCharCodec());
        ByteBuf buf = getExampleByteBuf();
        Common.printByteBufContent(buf);
        Common.printByteBufStatus(buf);

        channel.writeInbound(buf.readBytes(2));
        channel.writeInbound(buf.readBytes(2));
        System.out.println("[[the first  character]]:" + (Character) channel.readInbound());
        System.out.println("[[the second character]]:" + (Character) channel.readInbound());

        buf.release();
        channel.close();
    }




    @Test
    public void test04_CombinedByteCharCodec_Outbound() {
        EmbeddedChannel channel = new EmbeddedChannel(new CombinedByteCharCodec());
        channel.writeOutbound('A', 'B', 'C', 'D');

        ByteBuf c1 = channel.readOutbound();
        Common.printByteBufContentWithHex(c1);
        Common.printByteBufStatus(c1);
        c1.release();

        ByteBuf c2 = channel.readOutbound();
        Common.printByteBufContentWithHex(c2);
        Common.printByteBufStatus(c2);
        c2.release();

        ByteBuf c3 = channel.readOutbound();
        Common.printByteBufContentWithHex(c3);
        Common.printByteBufStatus(c3);
        c3.release();

        channel.close();
    }


    public static class ByteToCharDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            while (in.readableBytes() >= 2) {
                out.add(in.readChar());//将一个或者多个Character对象添加到传出的 List 中
            }
        }
    }

    public static class CharToByteEncoder extends MessageToByteEncoder<Character> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out) throws Exception {
            out.writeChar(msg);
        }
    }

    public static class CombinedByteCharCodec
            extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
        public CombinedByteCharCodec() {
            super(new ByteToCharDecoder(), new CharToByteEncoder());
        }
    }


}
