package top.offsetmonkey538.meshlib.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.router.HttpRouter;
import top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;
import static top.offsetmonkey538.meshlib.MESHLib.MOD_ID;
import static top.offsetmonkey538.meshlib.api.util.HttpResponseUtil.sendError;

/**
 * Main HTTP handler for MESH.
 * <p>
 * Forwards the requests to {@link HttpHandler HttpHandler}s registered in {@link HttpRouterRegistry}
 */
public class MainHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        HttpRouter router = null;
        List<String> matchedRouterIDs = new ArrayList<>(0);
        //noinspection deprecation
        for (Map.Entry<String,HttpRouter> possibleRouter : ((HttpRouterRegistryImpl) HttpRouterRegistry.INSTANCE).iterable()) {
            if (!possibleRouter.getValue().rule().matches(request)) continue;

            matchedRouterIDs.add(possibleRouter.getKey());
            if (router == null) router = possibleRouter.getValue();
        }
        if (router == null) {
            LOGGER.warn("No routers matched request for '%s%s'! Ignoring...", request.headers().get(HttpHeaderNames.HOST), request.uri());
            forward(ctx, request);
            return;
        }

        if (matchedRouterIDs.size() > 1) {
            LOGGER.error("More than one router matched request! Ignoring...");
            final StringBuilder builder = new StringBuilder("Matched routers: ").append(matchedRouterIDs.getFirst());
            for (int i = 1; i < matchedRouterIDs.size(); i++) {
                builder.append(", ");
                builder.append(matchedRouterIDs.get(i));
            }
            LOGGER.error(builder.toString());
            forward(ctx, request);
            return;
        }

        router.handler().handleRequest(ctx, request, router.rule());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        exceptionCaughtImpl(ctx, cause);
    }

    private static void exceptionCaughtImpl(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Failed to handle request", cause);

        if (!ctx.channel().isActive()) return;
        sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, cause);
    }

    private static void forward(ChannelHandlerContext ctx, FullHttpRequest request) {
        // These handlers can be removed from this context now
        ctx.pipeline().remove(MOD_ID + "/handler");

        // Forward to the next handler.
        // TODO: I don't think I need to retain anymore? ctx.fireChannelRead(ReferenceCountUtil.retain(request));
        ctx.fireChannelRead(request);
    }
}
