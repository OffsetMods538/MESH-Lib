package top.offsetmonkey538.meshlib.impl.router.target;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.HttpHandler;
import top.offsetmonkey538.meshlib.api.router.target.HttpTarget;
import top.offsetmonkey538.meshlib.example.SimpleHttpHandler;
import top.offsetmonkey538.meshlib.impl.router.rule.DomainHttpRule;

public class WebserverHttpTarget implements HttpHandler {

    @Override
    public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) throws Exception {

    }
}
