package top.offsetmonkey538.meshlib.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.handler.handlers.StaticFileHandler;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.util.HttpResponseUtil;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * An example {@link HttpHandler} implementation to learn from
 */
public record ExampleHttpHandler(String baseContent) implements HttpHandler {

    @Override
    public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request, @NotNull HttpRule rule) throws Exception {
        // Calculate response using super amazing and hard math™
        final String responseText = superCoolMethodForRunningTheHardAndAmazingCalculationForCalculationinatingTheResponseTM(request.uri());

        // You could also use this magical utility method for sending the response, but doing it manually makes the example longer and thus more betterer :P
        //  HttpResponseUtil.sendString(ctx, responseText);


        // Write the responseText to a buffer, encoded in UTF-8
        final ByteBuf content = Unpooled.copiedBuffer(responseText, StandardCharsets.UTF_8);
        // Create a response with said buffer
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);

        // Set the "CONTENT_TYPE" header to tell the browser that this is plain text encoded in UTF-8
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Send the response and close the connection
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
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
