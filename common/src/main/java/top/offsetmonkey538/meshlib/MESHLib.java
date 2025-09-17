package top.offsetmonkey538.meshlib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.offsetmonkey538.meshlib.config.TestConfig;
import top.offsetmonkey538.meshlib.example.ExampleMain;
import top.offsetmonkey538.meshlib.platform.PlatformMain;
import top.offsetmonkey538.monkeylib538.api.command.ConfigCommandApi;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigManager;

import java.util.ServiceLoader;

public final class MESHLib {
	/**
	 * Private constructor as this class shouldn't be instanced
	 */
	private MESHLib() {}

	/**
	 * String modid for this mod
	 */
	public static final String MOD_ID = "mesh-lib";
	/**
	 * Logger instance used by this mod
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    public static void initialize() {
        PlatformMain.enableVanillaHandler();
        ExampleMain.onInitialize();


        final ConfigHolder<TestConfig> config = ConfigManager.INSTANCE.init(ConfigHolder.create(TestConfig::new, LOGGER::error));
        System.out.println(config.get().thingy);
        ConfigCommandApi.registerConfigCommand(config, () -> System.out.println(config.get().thingy), "test");
    }


    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz, clazz.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to load service for " + clazz.getName()));
    }
}
