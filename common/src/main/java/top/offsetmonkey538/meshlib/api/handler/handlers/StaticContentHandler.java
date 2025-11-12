package top.offsetmonkey538.meshlib.api.handler.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.util.HttpResponseUtil;


public record StaticContentHandler(String content) implements HttpHandler {

    @Override
    public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request, @NotNull HttpRule rule) throws Exception {
        HttpResponseUtil.sendString(ctx, request, content);
    }

    @ApiStatus.Internal
    public static void register(final HttpHandlerTypeRegistry registry) {
        registry.register("static-content", Data.class, StaticContentHandler.class, handler -> new Data(handler.content), data -> new StaticContentHandler(data.content));
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
