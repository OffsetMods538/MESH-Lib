package top.offsetmonkey538.meshlib.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import top.offsetmonkey538.meshlib.api.HttpHandlerRegistry;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
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

        final String handlerId = request.uri().split("/")[1];

        HttpHandlerRegistry.INSTANCE.get(handlerId).handleRequest(ctx, request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Failed to handle request", cause);

        if (!ctx.channel().isActive()) return;
        sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, cause.getMessage());
    }
}
