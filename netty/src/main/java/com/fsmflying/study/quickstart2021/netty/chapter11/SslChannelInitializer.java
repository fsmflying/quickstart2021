package com.fsmflying.study.quickstart2021.netty.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 添加ssl/tls加密解密功能
 * Netty实战 11-1
 */
public class SslChannelInitializer extends ChannelInitializer<Channel>{
    private final SslContext context;
    private final boolean startTls;

    /**
     *
     * @param context 传入要使用的SslContext
     * @param startTls 如果设置为true,第一个写入的消息将不会被加密，(客户端应该为true)
     */
    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        //对于每一个SslHandler实例，都使用Channel的ByteBufAllocator从SslContext获取一个新的SSLEngine
        SSLEngine engine = context.newEngine(ch.alloc());
        //将SslHandler作为第一个ChannelHandler添加到ChannelPipeline中
        ch.pipeline().addLast(new SslHandler(engine,startTls));
    }
}