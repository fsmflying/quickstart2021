package com.fsmflying.study.quickstart2021.netty.lifecycle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Netty实战示例2-1
 */

@ChannelHandler.Sharable //标示一个ChannelHandler 可以被多个 Channel 安全地共享
public class EchoHandlersHandler02 extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss.SSS");

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("####EchoServerHandler02.channelRead####START###############################################");

        Map<String, ChannelHandler> handlerMap = ctx.pipeline().toMap();
        for (Map.Entry<String, ChannelHandler> e : handlerMap.entrySet()) {
            System.out.println(e.getKey() + ":" + e.getValue().getClass());
            System.out.println();
        }

        System.out.println("####EchoServerHandler02.channelRead####END#################################################");
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
//        打印异常栈跟踪
        cause.printStackTrace();
        //关闭该 Channel
        ctx.close();
    }
}
