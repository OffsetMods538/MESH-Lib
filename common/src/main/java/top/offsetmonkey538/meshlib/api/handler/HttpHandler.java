package top.offsetmonkey538.meshlib.api.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
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
public interface HttpHandler {

    /**
     * This is called when an HTTP request is received for this handler.
     * <br>
     *
     *
     * @param ctx the current channel handler context
     * @param request the received request
     * @param rule the rule used to match this handler
     * @throws Exception when anything goes wrong
     */
    void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request, @NotNull HttpRule rule) throws Exception;
}
