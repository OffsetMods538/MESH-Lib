package top.offsetmonkey538.meshlib;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import com.google.common.base.Stopwatch;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.handlers.StaticContentHandler;
import top.offsetmonkey538.meshlib.api.handler.handlers.StaticDirectoryHandler;
import top.offsetmonkey538.meshlib.api.handler.handlers.StaticFileHandler;
import top.offsetmonkey538.meshlib.api.router.HttpRouter;
import top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.rule.HttpRuleTypeRegistry;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.config.MESHLibConfig;
import top.offsetmonkey538.meshlib.config.RouterConfigHandler;
import top.offsetmonkey538.meshlib.example.ExampleMain;
import top.offsetmonkey538.meshlib.api.rule.rules.DomainHttpRule;
import top.offsetmonkey538.meshlib.api.rule.rules.PathHttpRule;
import top.offsetmonkey538.meshlib.impl.router.HttpHandlerTypeRegistryImpl;
import top.offsetmonkey538.meshlib.impl.router.rule.HttpRuleTypeRegistryImpl;
import top.offsetmonkey538.meshlib.platform.PlatformMain;
import top.offsetmonkey538.monkeylib538.api.command.CommandRegistrationApi;
import top.offsetmonkey538.monkeylib538.api.command.ConfigCommandApi;
import top.offsetmonkey538.monkeylib538.api.lifecycle.ServerLifecycleApi;
import top.offsetmonkey538.monkeylib538.api.log.MonkeyLibLogger;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigManager;
import top.offsetmonkey538.offsetconfig538.api.event.OffsetConfig538Events;

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
	public static final MonkeyLibLogger LOGGER = MonkeyLibLogger.create(MOD_ID);

    public static final ConfigHolder<MESHLibConfig> CONFIG = ConfigManager.init(ConfigHolder.create(MESHLibConfig::new, LOGGER::error));


    public static void initialize() {
        PlatformMain.enableVanillaHandler();
        ExampleMain.onInitialize();

        ConfigCommandApi.registerConfigCommand(CONFIG, MESHLib::reload, MOD_ID, "config");
        CommandRegistrationApi.registerCommand(RouterConfigHandler.createExampleConfigCommand());

        OffsetConfig538Events.JANKSON_CONFIGURATION_EVENT.listen(HttpRouter::configureJankson);

        HttpRuleTypeRegistry.HTTP_RULE_REGISTRATION_EVENT.listen(DomainHttpRule::register);
        HttpRuleTypeRegistry.HTTP_RULE_REGISTRATION_EVENT.listen(PathHttpRule::register);

        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticContentHandler::register);
        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticFileHandler::register);
        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticDirectoryHandler::register);

        HttpRouterRegistry.HTTP_ROUTER_REGISTRATION_EVENT.listen(RouterConfigHandler::init);

        ServerLifecycleApi.runOnServerStarting(MESHLib::reload);
    }

    // TODO: move somewhere under api so others can invoke a reload? For example git pack manager after reloading its config cause that's where the rule for it will be stored.
    public static void reload() {
        LOGGER.info("Reloading MESH Lib...");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        HttpHandlerTypeRegistry.clear();
        HttpRuleTypeRegistry.clear();
        HttpRouterRegistry.clear();
        LOGGER.info("Registries cleared");


        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.getInvoker().invoke();
        HttpRuleTypeRegistry.HTTP_RULE_REGISTRATION_EVENT.getInvoker().invoke();
        HttpRouterRegistry.HTTP_ROUTER_REGISTRATION_EVENT.getInvoker().invoke();
        LOGGER.info("Registries repopulated");

        LOGGER.info("MESH Lib reloaded in %s!", stopwatch.stop());
    }


    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz, clazz.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to load service for " + clazz.getName()));
    }
}
