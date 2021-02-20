package com.fsmflying.study.quickstart2021.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Netty实战示例 第11章
 */
public class Chapter11Test {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    public static class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
            pipeline.addLast(new FrameHandler());
        }
    }

    public static final class FrameHandler
            extends SimpleChannelInboundHandler<ByteBuf> {

        private final int skipLength;


        public FrameHandler() {
            this.skipLength = 0;
        }

        public FrameHandler(int skipLength) {
            this.skipLength = skipLength;
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx,
                                 ByteBuf msg) throws Exception {
            // Do something with the data extracted from the frame
            //msg.skipBytes(this.skipLength);
            if (this.skipLength == 0) {
                Common.printByteBufContent(msg);
            } else {

                Common.printByteBufContent(msg.slice(this.skipLength, msg.readableBytes() - this.skipLength));
            }


        }
    }


    @Test
    public void test01_exampleByteBuf() {
        EmbeddedChannel channel = new EmbeddedChannel(new LineBasedHandlerInitializer());
        ByteBuf buf = Common.getExampleByteBuf();
        Common.printByteBufContent(buf);
        Common.printByteBufStatus(buf);
        buf.release();
    }

    /**
     * 基于行的消息
     */
    @Test
    public void test02_LineBaseFrame() {
        EmbeddedChannel channel = new EmbeddedChannel(new LineBasedHandlerInitializer());
        ByteBuf buf = Common.getExampleByteBuf();
        Common.printByteBufStatus(buf);
        channel.writeInbound(buf.duplicate());

        Object obj;
        while ((obj = channel.readInbound()) != null) {
            System.out.println(obj.getClass());
        }

        while (buf.refCnt() > 1) {
            buf.release();
        }
    }

    /**
     * 分隔符解码初始化器
     */
    public static class DelimiterBasedHandlerInitializer extends ChannelInitializer<Channel> {

        /**
         * 分割符
         */
        private final ByteBuf delimiter;

        public DelimiterBasedHandlerInitializer(ByteBuf delimiter) {
            this.delimiter = delimiter;
        }

        /**
         * 分割符
         *
         * @param delimiter
         */
        public DelimiterBasedHandlerInitializer(String delimiter) {
            this.delimiter = Unpooled.copiedBuffer(delimiter.getBytes(CharsetUtil.ISO_8859_1));
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new DelimiterBasedFrameDecoder(256, delimiter));
            pipeline.addLast(new FrameHandler());
        }
    }

    /**
     * 基于自定义分割符
     */
    @Test
    public void test03_DelimiterBaseFrame() {
        EmbeddedChannel channel = new EmbeddedChannel(new DelimiterBasedHandlerInitializer(";"));

        ByteBuf buf = Common.getExampleByteBuf(';', true);
        channel.writeInbound(buf.duplicate());

        if (buf.refCnt() > 0) {
            buf.release();
        }
    }

    public static class CustomChannelInitializer extends ChannelInitializer<Channel> {

        List<ChannelHandler> handlers = new ArrayList<ChannelHandler>();

        public CustomChannelInitializer(List<ChannelHandler> handlers) {
            this.handlers = handlers;
        }

        public CustomChannelInitializer(ChannelHandler... handlers) {
            if (handlers != null) {
                for (int i = 0; i < handlers.length; i++) {
                    if (handlers[i] != null) {
                        this.handlers.add(handlers[i]);
                    }
                }
            }
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            for (ChannelHandler handler : this.handlers) {
                pipeline.addLast(handler);
            }
        }
    }


    //public static class

    /**
     * 解码固定长度的包
      */
    @Test
    public void test04_FixedLengthFrame() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new CustomChannelInitializer(
                        new FixedLengthFrameDecoder(5),
                        new FrameHandler()
                ));
        ByteBuf buf = Common.getExampleByteBuf(';', true);
        Common.printByteBufStatus(buf);
        channel.writeInbound(buf.duplicate());

        if (buf.refCnt() > 0) {
            buf.release();
        }
    }

    /**
     *
     */
    @Test
    public void test05_getExampleByteBuf() {
        ByteBuf buf = Common.getExampleByteBuf(65);
        Common.printByteBufStatus(buf);
        Common.printByteBufContent(buf);
    }

    /**
     * 解码指定长度的包
     */
    @Test
    public void test05_LengthFiledBasedDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new CustomChannelInitializer(
                        new LengthFieldBasedFrameDecoder(64 * 1024, 0, 8),
                        new FrameHandler(8)
                ));
        ByteBuf buf = Common.getExampleByteBuf(36);
        channel.writeInbound(buf.copy());
        channel.writeInbound(buf.copy());
        channel.writeInbound(buf.copy());
    }


}
