package com.fsmflying.study.quickstart2021.netty.chapter12;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * WebSocket处理http请求
 * 扩展 SimpleChannelInboundHandler以处理FullHttpRequest 消息
 * (Netty实战 12-1)
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;
    private static final File INDEX;

    //将index.html文件缓存
    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            //处理 100 Continue 请求以符合HTTP1.1规范
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            //读取index.html
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultHttpResponse(
                    request.protocolVersion(), HttpResponseStatus.OK);
            response.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

            boolean keepAlive = HttpUtil.isKeepAlive(request);
            //如果请求了 keep-alive,则添加所需要的 HTTP头信息
            if (keepAlive) {
                response.headers().
                        set(HttpHeaderNames.CONTENT_LENGTH, file.length());
                response.headers().
                        set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            //将HttpResponse写到客户端
            ctx.write(response);
            if (ctx.pipeline().get(SslHandler.class) == null) {
                //将index.html写到客户端
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            //写LastHttpContent并冲刷至客户端
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                //如果没有请求 keep-alive,则在写操作完成后关闭
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
