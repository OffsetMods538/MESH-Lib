package top.offsetmonkey538.meshlib.common.api.handler.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.common.api.util.HttpResponseUtil;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import java.nio.file.Path;

public record StaticFileHandler(Path fileToServe) implements HttpHandler {
    public StaticFileHandler(final Path fileToServe) {
        this.fileToServe = fileToServe.normalize().toAbsolutePath();
    }

    @Override
    public void handleRequest(ChannelHandlerContext ctx, FullHttpRequest request, HttpRule rule) throws Exception {
        HttpResponseUtil.sendFile(ctx, request, fileToServe);
    }

    @Internal
    public static void register(final HttpHandlerTypeRegistry registry) {
        registry.register("static-file", Data.class, StaticFileHandler.class, handler -> new Data(handler.fileToServe), data -> new StaticFileHandler(Path.of(data.fileToServe)));
    }

    @Internal
    private static final class Data {
        private String fileToServe;

        @SuppressWarnings("unused")
        // Pretty sure this public no-args needs to exist cause jankson wants to create instances
        public Data() {

        }

        public Data(final Path fileToServe) {
            this.fileToServe = fileToServe.toString();
        }
    }
}
