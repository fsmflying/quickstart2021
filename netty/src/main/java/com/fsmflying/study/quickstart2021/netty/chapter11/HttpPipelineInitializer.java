package com.fsmflying.study.quickstart2021.netty.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 用于初始化pipeline编码或解码Http请求
 * Netty实战 11-2
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public HttpPipelineInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //如果是客户端
        if (isClient) {
            //添加HttpResponseDecoder 以处理来自服务器的响应
            pipeline.addLast("decoder", new HttpResponseDecoder());
            //添加HttpRequestEncoder 以发送请求
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {//如果是服务器
            //添加 HttpRequestDecoder以接受来自客户端的请求
            pipeline.addLast("decoder", new HttpRequestDecoder());
            //添加 HttpResponseEncoder以发送响应
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
