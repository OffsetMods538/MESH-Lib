package top.offsetmonkey538.meshlib.impl;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import top.offsetmonkey538.meshlib.api.HttpHandlerRegistry;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;
import static top.offsetmonkey538.meshlib.api.HttpHandler.sendError;

/**
 * Main HTTP handler for MESH.
 * <p>
 * Forwards the requests to {@link top.offsetmonkey538.meshlib.api.HttpHandler HttpHandler}s registered in {@link HttpHandlerRegistry}
 */
public class MainHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        // Get first part of URI, which is the id for a handler
        final String uri = request.uri();
        if ("/".equals(uri)) {
            LOGGER.warn("Request was made to root domain! Ignoring...");
            ctx.writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND)).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        final String handlerId = uri.split("/")[1];

        // Call handler if found
        if (!HttpHandlerRegistry.INSTANCE.has(handlerId)) {
            LOGGER.warn("Handler with id '{}' not registered! Ignoring...", handlerId);
            ctx.writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND)).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        HttpHandlerRegistry.INSTANCE.get(handlerId).handleRequest(ctx, request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Failed to handle request", cause);

        if (!ctx.channel().isActive()) return;
        sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, cause.getMessage());
    }
}
