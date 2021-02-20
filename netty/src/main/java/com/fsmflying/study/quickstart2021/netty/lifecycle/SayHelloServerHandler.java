package com.fsmflying.study.quickstart2021.netty.lifecycle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.logging.InternalLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;

/**
 * Netty实战示例2-1
 */

@ChannelHandler.Sharable //标示一个ChannelHandler 可以被多个 Channel 安全地共享
public class SayHelloServerHandler extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss.SSS");
    final ByteBuf bufHello = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("Hello!\r\n", Charset.forName("UTF-8")));

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        ctx.writeAndFlush(bufHello);
        logger.debug("active to [" + ctx.channel().remoteAddress().toString() + "]");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //super.channelInactive(ctx);
        //ctx.writeAndFlush(bufBye);
        logger.debug("inactive to [" + ctx.channel().remoteAddress().toString() + "]");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //super.channelRegistered(ctx);
        logger.debug("registered to [" + ctx.channel().remoteAddress().toString() + "]");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //super.channelUnregistered(ctx);
        logger.debug("unregiistered to [" + ctx.channel().remoteAddress().toString() + "]");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        //打印异常栈跟踪
        cause.printStackTrace();
        //关闭该 Channel
        ctx.close();
    }
}
