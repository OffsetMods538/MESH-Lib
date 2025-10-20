package top.offsetmonkey538.meshlib.api;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.example.SimpleHttpHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * An http handler for you to implement :D
 * <br>
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
