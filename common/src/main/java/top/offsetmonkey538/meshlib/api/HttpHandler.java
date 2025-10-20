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
public abstract class HttpHandler<T> {
    protected final T data;
    
    public HttpHandler(T data) {
        this.data = data;
    }
    public T getData() {
        return data;
    }

    /**
     * This is called when an HTTP request is received for this handler.
     * <br>
     *
     *
     * @param ctx the current channel handler context
     * @param request the received request
     * @throws Exception when anything goes wrong
     */
    public abstract void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) throws Exception;

    public record HttpHandlerDefinition<T>(String type, Class<T> dataType, Function<T, HttpHandler<T>> handlerInitializer) {

    }
}
