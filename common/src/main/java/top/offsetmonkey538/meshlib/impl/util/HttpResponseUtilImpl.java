package top.offsetmonkey538.meshlib.impl.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.meshlib.api.util.HttpResponseUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;
import static top.offsetmonkey538.meshlib.api.util.HttpResponseUtil.sendError;

public final class HttpResponseUtilImpl implements HttpResponseUtil {

    @Override
    public void sendFileImpl(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request, @NotNull Path fileToSend) throws IOException {
        if (!Files.exists(fileToSend) || !Files.isRegularFile(fileToSend)) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        final boolean isKeepAlive = HttpUtil.isKeepAlive(request);
        final long fileLength = Files.size(fileToSend);


        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        response.headers().set(CONTENT_LENGTH, fileLength);
        response.headers().set(CONTENT_TYPE, getContentType(fileToSend));
        response.headers().set(CONNECTION, isKeepAlive ? KEEP_ALIVE : CLOSE);
        ctx.write(response);

        ctx.write(
                new ChunkedNioFile(fileToSend.toFile()),
                //new DefaultFileRegion(fileToServe.toFile(), 0, fileLength),
                ctx.newProgressivePromise()
        ).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        final ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (isKeepAlive) return;
        lastContentFuture.addListener(ChannelFutureListener.CLOSE);
    }

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

    private static String getContentType(final @NotNull Path file) {
        final String result;

        try {
            result = Files.probeContentType(file);
        } catch (IOException e) {
            return "text/plain";
        }

        if (result == null) return "text/plain";
        return result;
    }
}
