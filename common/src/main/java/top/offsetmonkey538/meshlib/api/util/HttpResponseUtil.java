package top.offsetmonkey538.meshlib.api.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.meshlib.impl.util.HttpResponseUtilImpl;

/**
 * Provides utils for responding to http requests
 */
public interface HttpResponseUtil {
    /**
     * Instance
     */
    @ApiStatus.Internal
    HttpResponseUtil INSTANCE = new HttpResponseUtilImpl();


    /**
     * Sends the requester a Permanent Redirect (308) response containing the new location.
     * <p>
     * Afaik clients may cache this redirect and automatically go to the new location when this url is requested? If that is not desired, use {@link #sendTemporaryRedirect(ChannelHandlerContext, String)}
     *
     * @param ctx the current channel handler context
     * @param newLocation the new location.
     */
    static void sendPermanentRedirect(@NotNull ChannelHandlerContext ctx, @NotNull String newLocation) {
        INSTANCE.sendRedirectImpl(ctx, HttpResponseStatus.PERMANENT_REDIRECT, newLocation);
    }

    /**
     * Sends the requester a Temporary Redirect (307) response containing the new location.
     *
     * @param ctx the current channel handler context
     * @param newLocation the new location.
     */
    static void sendTemporaryRedirect(@NotNull ChannelHandlerContext ctx, @NotNull String newLocation) {
        INSTANCE.sendRedirectImpl(ctx, HttpResponseStatus.TEMPORARY_REDIRECT, newLocation);
    }

    /**
     * Sends the requester a plain text 200 response
     *
     * @param ctx the current channel handler context
     * @param content the plain text response to send
     */
    static void sendString(@NotNull ChannelHandlerContext ctx, @NotNull String content) {
        INSTANCE.sendStringImpl(ctx, content);
    }

    /**
     * Logs and sends the requester an error code.
     *
     * @param ctx the current channel handler context
     * @param status the status to send
     * @see #sendError(ChannelHandlerContext, HttpResponseStatus, Throwable)
     * @see #sendError(ChannelHandlerContext, HttpResponseStatus, String)
     */
    static void sendError(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status) {
        sendError(ctx, status, (String) null);
    }

    /**
     * Logs and sends the requester an error code and a reason with it.
     *
     * @param ctx the current channel handler context
     * @param status the status to send
     * @param reason reason to display for the error, MUST NOT be null
     * @see #sendError(ChannelHandlerContext, HttpResponseStatus)
     * @see #sendError(ChannelHandlerContext, HttpResponseStatus, String)
     */
    static void sendError(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status, @NotNull Throwable reason) {
        sendError(ctx, status, reason.getMessage());
    }

    /**
     * Logs and sends the requester an error code and optionally a reason with it.
     *
     * @param ctx the current channel handler context
     * @param status the status to send
     * @param reason reason to display for the error, MAY be null or empty
     * @see #sendError(ChannelHandlerContext, HttpResponseStatus)
     * @see #sendError(ChannelHandlerContext, HttpResponseStatus, Throwable)
     */
    static void sendError(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status, @Nullable String reason) {
        INSTANCE.sendErrorImpl(ctx, status, reason);
    }

    /**
     * Sends the requester the provided {@link FullHttpResponse}, sets {@link io.netty.handler.codec.http.HttpHeaderNames#CONNECTION CONNECTION} to "close" and closes the connection.
     *
     * @param ctx the current channel handler context
     * @param response the response to send
     */
    static void sendResponseAndClose(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpResponse response) {
        INSTANCE.sendResponseAndCloseImpl(ctx, response);
    }


    void sendRedirectImpl(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status, @NotNull String newLocation);
    void sendStringImpl(@NotNull ChannelHandlerContext ctx, @NotNull String content);
    void sendErrorImpl(@NotNull ChannelHandlerContext ctx, @NotNull HttpResponseStatus status, @Nullable String reason);
    void sendResponseAndCloseImpl(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpResponse response);
}
