package top.offsetmonkey538.meshlib.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.util.HttpResponseUtil;

/**
 * An example {@link HttpHandler} implementation to learn from
 */
public record ExampleHttpHandler(String baseContent) implements HttpHandler {

    @Override
    public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request, @NotNull HttpRule rule) throws Exception {
        // Calculate response using super amazing and hard math™
        final String responseText = superCoolMethodForRunningTheHardAndAmazingCalculationForCalculationinatingTheResponseTM(request.uri());

        // Magical utility for sending a plain-text string
        HttpResponseUtil.sendString(ctx, request, responseText);
    }

    private String superCoolMethodForRunningTheHardAndAmazingCalculationForCalculationinatingTheResponseTM(String requestUri) {
        final String requestedPath = requestUri.substring(requestUri.indexOf('/'));

        return this.baseContent + requestedPath;
    }

    public static void register(final HttpHandlerTypeRegistry registry) {
        registry.register("example-http", Data.class, ExampleHttpHandler.class, handler -> new Data(handler.baseContent), data -> new ExampleHttpHandler(data.content));
    }

    @ApiStatus.Internal
    private static final class Data {
        private String content;

        @SuppressWarnings("unused")
        // Pretty sure this public no-args needs to exist cause jankson wants to create instances
        public Data() {

        }

        public Data(final String content) {
            this.content = content;
        }
    }
}
