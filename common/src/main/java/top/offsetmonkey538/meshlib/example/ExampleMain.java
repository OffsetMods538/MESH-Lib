package top.offsetmonkey538.meshlib.example;

import top.offsetmonkey538.meshlib.api.HttpHandlerRegistry;

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
        HttpHandlerRegistry.INSTANCE.register("simple-server", new SimpleHttpHandler());
    }
}
