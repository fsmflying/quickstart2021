package com.fsmflying.study.quickstart2021.netty.chapter13;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 扩展 SimpleChannelInboundHandler以处理LogEvent消息
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        cause.printStackTrace();
        //当异常发生时,打印栈跟踪信息,并关闭对应的Channel
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
        //创建StringBuilder,并且构建输出的字符串
        StringBuilder builder = new StringBuilder();
        builder.append(event.getReceivedTimestamp());
        builder.append(" [");
        builder.append(event.getSource().toString());
        builder.append("] [");
        builder.append(event.getLogfile());
        builder.append("] : ");
        builder.append(event.getMsg());
        //打印LogEvent的数据
        System.out.println(builder.toString());
    }

}
