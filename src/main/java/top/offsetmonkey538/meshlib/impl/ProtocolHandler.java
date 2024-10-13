package top.offsetmonkey538.meshlib.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import top.offsetmonkey538.meshlib.api.HttpHandlerRegistry;

import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;
import static top.offsetmonkey538.meshlib.MESHLib.MOD_ID;

/**
 * Checks if a request is HTTP and either forwards it to {@link MainHttpHandler} if it <i>is</i> an HTTP request
 * and to the Minecraft handler otherwise.
 */
public class ProtocolHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object request) throws Exception {
        if (!(request instanceof ByteBuf buf)) {
            LOGGER.warn("Received request '{}' that wasn't a ByteBuf", request);
            return;
        }

        // Read the first line to check if it's an http request
        //  todo: maybe there's a better way to check?
        final StringBuilder firstLine = new StringBuilder();
        for (int i = 0; i < buf.readableBytes(); i++) {
            char currentChar = (char) buf.getByte(i);
            firstLine.append(currentChar);
            if (currentChar == '\n') break;
        }
        final boolean isHttp = firstLine.toString().contains("HTTP");


        // If it's an http request, add the correct handlers
        if (isHttp) {
            final String uri = firstLine.toString().split(" ")[1];
            if (uri.equals("/")) {
                LOGGER.debug("Request was made to root domain! Passing on...");
                forward(ctx, request);
                return;
            }

            final String handlerId = uri.split("/")[1];
            if (!HttpHandlerRegistry.INSTANCE.has(handlerId)) {
                LOGGER.debug("Handler with id '{}' not registered! Passing on...", handlerId);
                forward(ctx, request);
                return;
            }

            final ChannelPipeline pipeline = ctx.pipeline();
            pipeline.addAfter(MOD_ID, MOD_ID + "/codec", new HttpServerCodec());
            pipeline.addAfter(MOD_ID + "/codec", MOD_ID + "/aggregator", new HttpObjectAggregator(65536));
            pipeline.addAfter(MOD_ID + "/aggregator", MOD_ID + "/handler", new MainHttpHandler());
        }

        forward(ctx, request);
    }

    private void forward(ChannelHandlerContext ctx, Object request) {
        // This handler can be removed from this context now
        ctx.pipeline().remove(MOD_ID);

        // Forward to the next handler. If this wasn't an http request and the http handlers weren't added above,
        //  it'll go to minecraft, otherwise it will go through the handlers added above, ending up in the MainHttpHandler
        ctx.fireChannelRead(request);
    }
}
