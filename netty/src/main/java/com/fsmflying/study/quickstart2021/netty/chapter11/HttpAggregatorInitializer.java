package com.fsmflying.study.quickstart2021.netty.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 用于聚合Http请求或响应
 * (Netty实战 11-3)
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpAggregatorInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        if (client) {
            ch.pipeline().addLast("codec", new HttpClientCodec());
        } else {
            ch.pipeline().addLast("codec", new HttpServerCodec());
        }
        ch.pipeline().addLast("aggregator",
                new HttpObjectAggregator(512 * 1024));
    }
}
