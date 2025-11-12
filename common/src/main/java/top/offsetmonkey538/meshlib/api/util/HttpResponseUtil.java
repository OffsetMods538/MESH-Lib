package top.offsetmonkey538.meshlib.api.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.meshlib.impl.util.HttpResponseUtilImpl;

import java.io.IOException;
import java.nio.file.Path;

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
     * Sends the requester the file at the provided file
     *
     * @param ctx the current channel handler context
     * @param request the client request. Used to determine if keep-alive is to be used. Setting to null implies a non-keep-alive connection.
     * @param fileToSend the path to the file to send
     * @throws IOException when io go wrong :(
     */
    static void sendFile(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull Path fileToSend) throws IOException {
        INSTANCE.sendFileImpl(ctx, request, fileToSend);
    }

    /**
     * Sends the requester a Permanent Redirect (308) response containing the new location.
     * <p>
     * Clients may cache this and automatically redirect when the url is requested. If that is not desired, use {@link #sendTemporaryRedirect(ChannelHandlerContext, FullHttpRequest, String)}
     *
     * @param ctx the current channel handler context
     * @param request the client request. Used to determine if keep-alive is to be used. Setting to null implies a non-keep-alive connection.
     * @param newLocation the new location.
     */
    static void sendPermanentRedirect(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull String newLocation) {
        INSTANCE.sendRedirectImpl(ctx, request, HttpResponseStatus.PERMANENT_REDIRECT, newLocation);
    }

    /**
     * Sends the requester a Temporary Redirect (307) response containing the new location.
     *
     * @param ctx the current channel handler context
     * @param request the client request. Used to determine if keep-alive is to be used. Setting to null implies a non-keep-alive connection.
     * @param newLocation the new location.
     */
    static void sendTemporaryRedirect(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull String newLocation) {
        INSTANCE.sendRedirectImpl(ctx, request, HttpResponseStatus.TEMPORARY_REDIRECT, newLocation);
    }

    /**
     * Sends the requester a plain text 200 response
     *
     * @param ctx the current channel handler context
     * @param request the client request. Used to determine if keep-alive is to be used. Setting to null implies a non-keep-alive connection.
     * @param content the plain text response to send
     */
    static void sendString(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull String content) {
        INSTANCE.sendStringImpl(ctx, request, content);
    }

    /**
     * Logs and sends the requester an error code.
     *
     * @param ctx the current channel handler context
     * @param request the client request. Used to determine if keep-alive is to be used. Setting to null implies a non-keep-alive connection.
     * @param status the status to send
     * @see #sendError(ChannelHandlerContext, FullHttpRequest, HttpResponseStatus, Throwable)
     * @see #sendError(ChannelHandlerContext, FullHttpRequest, HttpResponseStatus, String)
     */
    static void sendError(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull HttpResponseStatus status) {
        sendError(ctx, request, status, (String) null);
    }

    /**
     * Logs and sends the requester an error code and a reason with it.
     *
     * @param ctx the current channel handler context
     * @param request the client request. Used to determine if keep-alive is to be used. Setting to null implies a non-keep-alive connection.
     * @param status the status to send
     * @param reason reason to display for the error, MUST NOT be null
     * @see #sendError(ChannelHandlerContext, FullHttpRequest, HttpResponseStatus)
     * @see #sendError(ChannelHandlerContext, FullHttpRequest, HttpResponseStatus, String)
     */
    static void sendError(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull HttpResponseStatus status, @NotNull Throwable reason) {
        sendError(ctx, request, status, reason.getMessage());
    }

    /**
     * Logs and sends the requester an error code and optionally a reason with it.
     *
     * @param ctx the current channel handler context
     * @param request the client request. Used to determine if keep-alive is to be used. Setting to null implies a non-keep-alive connection.
     * @param status the status to send
     * @param reason reason to display for the error, MAY be null or empty
     * @see #sendError(ChannelHandlerContext, FullHttpRequest, HttpResponseStatus)
     * @see #sendError(ChannelHandlerContext, FullHttpRequest, HttpResponseStatus, Throwable)
     */
    static void sendError(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull HttpResponseStatus status, @Nullable String reason) {
        INSTANCE.sendErrorImpl(ctx, request, status, reason);
    }

    /**
     * Sends the requester the provided {@link FullHttpResponse}. If request is keep-alive, sets {@link io.netty.handler.codec.http.HttpHeaderNames#CONNECTION CONNECTION} "keep-alive" and returns. Otherwise sets {@link io.netty.handler.codec.http.HttpHeaderNames#CONNECTION CONNECTION} to "close" and closes the connection.
     *
     * @param ctx the current channel handler context
     * @param request the client request. Used to determine if keep-alive is to be used. Setting to null implies a non-keep-alive connection.
     * @param response the response to send
     */
    static void sendResponse(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull FullHttpResponse response) {
        INSTANCE.sendResponseImpl(ctx, request, response);
    }


    void sendFileImpl(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull Path fileToSend) throws IOException;
    void sendRedirectImpl(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull HttpResponseStatus status, @NotNull String newLocation);
    void sendStringImpl(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull String content);
    void sendErrorImpl(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull HttpResponseStatus status, @Nullable String reason);
    void sendResponseImpl(@NotNull ChannelHandlerContext ctx, @Nullable FullHttpRequest request, @NotNull FullHttpResponse response);
}
