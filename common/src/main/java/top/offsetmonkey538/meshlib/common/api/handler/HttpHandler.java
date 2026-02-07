package top.offsetmonkey538.meshlib.common.api.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import top.offsetmonkey538.meshlib.common.api.example.ExampleHttpHandler;
import top.offsetmonkey538.meshlib.common.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRule;

/**
 * An http handler for you to implement :D
 * <br>
 * Look at {@link ExampleHttpHandler ExampleHttpHandler} for an example
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
    void handleRequest(ChannelHandlerContext ctx, FullHttpRequest request, HttpRule rule) throws Exception;
}
