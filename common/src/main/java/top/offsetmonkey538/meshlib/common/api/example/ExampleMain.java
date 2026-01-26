package top.offsetmonkey538.meshlib.common.api.example;

import top.offsetmonkey538.meshlib.common.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.common.api.router.HttpRouter;
import top.offsetmonkey538.meshlib.common.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.rules.DomainHttpRule;

import static top.offsetmonkey538.meshlib.common.MESHLib.LOGGER;

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
        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(ExampleHttpHandler::register);

        HttpRouterRegistry.HTTP_ROUTER_REGISTRATION_EVENT.listen(registry -> {
            registry.register("example/example-http-handler", new HttpRouter(
                    new DomainHttpRule("site.example.com"),
                    new ExampleHttpHandler("Requested path: ")
            ));
        });
    }
}
