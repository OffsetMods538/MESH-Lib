package top.offsetmonkey538.meshlib.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.HttpHandler;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * An example {@link HttpHandler} implementation to learn from
 */
public class SimpleHttpHandler implements HttpHandler {

    @Override
    public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) throws Exception {
        // Write "Hello, World!" to a buffer, encoded in UTF-8
        final ByteBuf content = Unpooled.copiedBuffer("Hello, World!", StandardCharsets.UTF_8);
        // Create a response with said buffer
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);

        // Set the "CONTENT_TYPE" header to tell the browser that this is plain text encoded in UTF-8
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");


        // Send the response and close the connection
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
