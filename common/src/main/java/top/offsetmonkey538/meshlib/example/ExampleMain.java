package top.offsetmonkey538.meshlib.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.HttpHandler;
import top.offsetmonkey538.meshlib.api.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.router.HttpRouter;
import top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.api.router.target.HttpTarget;
import top.offsetmonkey538.meshlib.impl.router.rule.DomainHttpRule;

import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;

/**
 * Initializer for the example handlers
 */
public final class ExampleMain {
    private ExampleMain() {

    }

    /**
     * Initializer for example handlers
     * <br>
     * Checks if the {@code meshEnableExamples} system property is enabled and registers the example handlers if so
     */
    public static void onInitialize() {
        HttpHandlerTypeRegistry.register(SimpleHttpHandler.class, new HttpHandler.HttpHandlerDefinition<>("simple-http", SimpleHttpHandler.Data.class, SimpleHttpHandler::new));


        // Ignore if "meshEnableExamples" isn't set
        if (!Boolean.getBoolean("meshEnableExamples")) return;


        LOGGER.warn("MESH examples enabled!");

        // Register
        HttpRouterRegistry.INSTANCE.register("simple-server", new HttpRouter(
                new DomainHttpRule(new DomainHttpRule.Data("localhost")),
                new SimpleHttpHandler(new SimpleHttpHandler.Data("Yellow!"))
        ));
    }
}
