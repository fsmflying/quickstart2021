package com.fsmflying.study.quickstart2021.netty.chapter09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        //检查是否有足够的字节用来编码
        while (msg.readableBytes() >= 4) {
            //从输入中ByteBuf读取一个整数，并计算其绝对值
            int value = Math.abs(msg.readInt());
            //将整数写到编码消息的列表
            out.add(value);
        }
    }
}
