package top.offsetmonkey538.meshlib.example;

import top.offsetmonkey538.meshlib.api.HttpHandlerRegistry;

import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;

/**
 * Mod initializer for the example handlers
 */
public class ExampleMain {

    /**
     * Initializer for example handlers
     * <br>
     * Checks if {@code meshEnableExamples} is enabled and registers the example handlers if so
     */
    public void onInitializeServer() {
        // Ignore if "meshEnableExamples" isn't set
        if (System.getProperty("meshEnableExamples", "").isEmpty()) return;


        LOGGER.warn("MESH examples enabled!");

        // Register
        HttpHandlerRegistry.INSTANCE.register("simple-server", new SimpleHttpHandler());
    }
}
