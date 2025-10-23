package top.offsetmonkey538.meshlib.example;

import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.handler.handlers.StaticDirectoryHandler;
import top.offsetmonkey538.meshlib.api.handler.handlers.StaticFileHandler;
import top.offsetmonkey538.meshlib.api.router.HttpRouter;
import top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.api.rule.rules.DomainHttpRule;
import top.offsetmonkey538.meshlib.api.rule.rules.PathHttpRule;

import java.nio.file.Path;

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
        // Ignore if "meshEnableExamples" isn't set
        if (!Boolean.getBoolean("meshEnableExamples")) return;


        LOGGER.warn("MESH examples enabled!");

        // Register
        record SimpleHttpHandlerData(String content) {

        }
        HttpHandlerTypeRegistry.register("simple-http", SimpleHttpHandlerData.class, SimpleHttpHandler.class, handler -> new SimpleHttpHandlerData(handler.content), data -> new SimpleHttpHandler(data.content()));

        //HttpRouterRegistry.INSTANCE.register("simple-server", new HttpRouter(
        //        new DomainHttpRule("localhost"),
        //        new SimpleHttpHandler("Yellow!")
        //));
        HttpRouterRegistry.INSTANCE.register("simple-server2", new HttpRouter(
                new PathHttpRule("/hi"),
                new SimpleHttpHandler("Goodbye!")
        ));

        HttpRouterRegistry.INSTANCE.register("static-file-test", new HttpRouter(
                new PathHttpRule("/static/file"),
                new StaticFileHandler(Path.of("usercache.json"))
        ));

        HttpRouterRegistry.INSTANCE.register("static-directory-test", new HttpRouter(
                new PathHttpRule("/static/directory"),
                new StaticDirectoryHandler(Path.of("/home/dave"), true)
        ));

        HttpRouterRegistry.INSTANCE.register("static-directory-test2", new HttpRouter(
                new DomainHttpRule("localhost"),
                new StaticDirectoryHandler(Path.of("/home/dave"), false)
        ));
    }
}
