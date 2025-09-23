package top.offsetmonkey538.meshlib.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.api.router.rule.HttpRule;
import top.offsetmonkey538.meshlib.example.SimpleHttpHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;

/**
 * An http handler for you to implement :D
 * <p>
 * This class also provides some utility methods for making your life easier.
 * <br>
 * For example {@link #sendError(ChannelHandlerContext, HttpResponseStatus)} {@link #sendError(ChannelHandlerContext, HttpResponseStatus, String)}
 * <p>
 * Look at {@link SimpleHttpHandler SimpleHttpHandler} for an example
 *
 * @see HttpRouterRegistry
 */
@FunctionalInterface
public interface HttpHandler {

    /**
     * This is called when an HTTP request is received for this handler.
     * <br>
     *
     *
     * @param ctx the current channel handler context
     * @param request the received request
     * @throws Exception when anything goes wrong
     */
    void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) throws Exception;

    /**
     * Sends the requester an error code.
     *
     * @param ctx the current channel handler context
     * @param status the received request
     * @see #sendError(ChannelHandlerContext, HttpResponseStatus, String)
     */
    static void sendError(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status) {
        sendError(ctx, status, null);
    }

    /**
     * Sends the requester an error code and optionally a reason with it.
     *
     * @param ctx the current channel handler context
     * @param status the received request
     * @param reason reason to display for the error, may be null or empty
     * @see #sendError(ChannelHandlerContext, HttpResponseStatus)
     */
    static void sendError(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status, @Nullable String reason) {
        final String message = String.format("Failure: %s\r\n%s",status, (reason == null || reason.isBlank() ? "" : "Reason: " + reason + "\r\n"));

        LOGGER.error(message);

        final ByteBuf byteBuf = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, byteBuf);

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    record HttpHandlerDefinition<T>(String type, Class<T> dataType, Function<T, HttpHandler> handlerInitializer, Function<HttpHandler, T> handlerToData) {

    }
}
