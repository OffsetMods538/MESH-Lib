package top.offsetmonkey538.meshlib.common.api.handler.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.common.api.util.HttpResponseUtil;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

public record StaticContentHandler(String content) implements HttpHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, FullHttpRequest request, HttpRule rule) throws Exception {
        HttpResponseUtil.sendString(ctx, request, content);
    }

    @Internal
    public static void register(final HttpHandlerTypeRegistry registry) {
        registry.register("static-content", Data.class, StaticContentHandler.class, handler -> new Data(handler.content), data -> new StaticContentHandler(data.content));
    }

    @Internal
    private static final class Data {
        private String content = "";

        @SuppressWarnings("unused")
        // Pretty sure this public no-args needs to exist cause jankson wants to create instances
        public Data() {

        }

        public Data(final String content) {
            this.content = content;
        }
    }
}
