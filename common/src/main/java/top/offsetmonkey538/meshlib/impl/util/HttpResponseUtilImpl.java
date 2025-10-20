package top.offsetmonkey538.meshlib.impl.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.meshlib.api.util.HttpResponseUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;

public final class HttpResponseUtilImpl implements HttpResponseUtil {

    @Override
    public void sendRedirectImpl(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status, @NotNull String newLocation) {
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.EMPTY_BUFFER);

        response.headers().set(LOCATION, newLocation);

        sendResponseAndCloseImpl(ctx, response);
    }

    @Override
    public void sendStringImpl(@NotNull ChannelHandlerContext ctx, @NotNull String content) {
        final ByteBuf byteBuf = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, byteBuf);

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        sendResponseAndCloseImpl(ctx, response);
    }

    @Override
    public void sendErrorImpl(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status, @Nullable String reason) {
        final StringBuilder messageBuilder = new StringBuilder("Failure: ").append(status);
        if (reason != null && !reason.isBlank()) messageBuilder.append("\nReason: ").append(reason);

        final String message = messageBuilder.toString();


        LOGGER.error(message);

        final ByteBuf byteBuf = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, byteBuf);

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        sendResponseAndCloseImpl(ctx, response);
    }

    @Override
    public void sendResponseAndCloseImpl(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpResponse response) {
        response.headers().set(CONNECTION, "close");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
