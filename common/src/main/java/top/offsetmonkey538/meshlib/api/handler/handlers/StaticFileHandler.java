package top.offsetmonkey538.meshlib.api.handler.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.util.HttpResponseUtil;

import java.nio.file.Path;


public record StaticFileHandler(Path fileToServe) implements HttpHandler {
    public StaticFileHandler(final Path fileToServe) {
        this.fileToServe = fileToServe.normalize().toAbsolutePath();
    }

    @Override
    public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request, @NotNull HttpRule rule) throws Exception {
        HttpResponseUtil.sendFile(ctx, request, fileToServe);
    }

    @ApiStatus.Internal
    public static void register(final HttpHandlerTypeRegistry registry) {
        registry.register("static-file", Data.class, StaticFileHandler.class, handler -> new Data(handler.fileToServe), data -> new StaticFileHandler(data.fileToServe));
    }

    @ApiStatus.Internal
    private static final class Data {
        private Path fileToServe;

        @SuppressWarnings("unused")
        // Pretty sure this public no-args needs to exist cause jankson wants to create instances
        public Data() {

        }

        public Data(final Path fileToServe) {
            this.fileToServe = fileToServe;
        }
    }
}
