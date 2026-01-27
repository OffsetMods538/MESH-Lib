package top.offsetmonkey538.meshlib.common;

import com.google.common.base.Stopwatch;
import top.offsetmonkey538.meshlib.common.api.example.ExampleMain;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.common.api.handler.handlers.StaticContentHandler;
import top.offsetmonkey538.meshlib.common.api.handler.handlers.StaticDirectoryHandler;
import top.offsetmonkey538.meshlib.common.api.handler.handlers.StaticFileHandler;
import top.offsetmonkey538.meshlib.common.api.router.HttpRouter;
import top.offsetmonkey538.meshlib.common.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRuleTypeRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.rules.DomainHttpRule;
import top.offsetmonkey538.meshlib.common.api.rule.rules.PathHttpRule;
import top.offsetmonkey538.meshlib.common.config.MESHLibConfig;
import top.offsetmonkey538.meshlib.common.config.RouterConfigHandler;
import top.offsetmonkey538.meshlib.common.netty.NettyServer;
import top.offsetmonkey538.meshlib.common.platform.PlatformUtil;
import top.offsetmonkey538.monkeylib538.common.api.command.CommandRegistrationApi;
import top.offsetmonkey538.monkeylib538.common.api.command.ConfigCommandApi;
import top.offsetmonkey538.monkeylib538.common.api.lifecycle.ServerLifecycleApi;
import top.offsetmonkey538.offsetutils538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetutils538.api.config.ConfigManager;
import top.offsetmonkey538.offsetutils538.api.config.event.JanksonConfigurationEvent;
import top.offsetmonkey538.offsetutils538.api.log.OffsetLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

public final class MESHLib {
	/**
	 * Private constructor as this class shouldn't be instanced
	 */
	private MESHLib() {}

	/**
	 * String modid for this mod
	 */
	public static final String MOD_ID = "meshlib";
	/**
	 * Logger instance used by this mod
	 */
	public static final OffsetLogger LOGGER = OffsetLogger.create(MOD_ID);

    public static final ConfigHolder<MESHLibConfig> CONFIG = ConfigManager.init(ConfigHolder.create(MESHLibConfig::new, LOGGER));


    public static void initialize() {
        PlatformUtil.enableVanillaHandler();
        ExampleMain.onInitialize();

        ConfigCommandApi.registerConfigCommand(CONFIG, MESHLib::reload, MOD_ID, "config");
        CommandRegistrationApi.registerCommand(RouterConfigHandler.createExampleConfigCommand());

        JanksonConfigurationEvent.JANKSON_CONFIGURATION_EVENT.listen(HttpRouter::configureJankson);

        HttpRuleTypeRegistry.HTTP_RULE_REGISTRATION_EVENT.listen(DomainHttpRule::register);
        HttpRuleTypeRegistry.HTTP_RULE_REGISTRATION_EVENT.listen(PathHttpRule::register);

        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticContentHandler::register);
        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticFileHandler::register);
        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticDirectoryHandler::register);

        HttpRouterRegistry.HTTP_ROUTER_REGISTRATION_EVENT.listen(RouterConfigHandler::init);

        ServerLifecycleApi.STARTING.listen(MESHLib::reload);
    }

    // TODO: move somewhere under api so others can invoke a reload? For example git pack manager after reloading its config cause that's where the rule for it will be stored.
    public static void reload() {
        LOGGER.info("Reloading MESH Lib...");
        final Stopwatch fullStopwatch = Stopwatch.createStarted();
        disableAllHandlers();

        final Stopwatch stageStopwatch = Stopwatch.createStarted();
        HttpHandlerTypeRegistry.clear();
        HttpRuleTypeRegistry.clear();
        HttpRouterRegistry.clear();
        LOGGER.info("Registries cleared in %s", stageStopwatch.stop());
        stageStopwatch.reset().start();


        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.getInvoker().invoke();
        HttpRuleTypeRegistry.HTTP_RULE_REGISTRATION_EVENT.getInvoker().invoke();
        HttpRouterRegistry.HTTP_ROUTER_REGISTRATION_EVENT.getInvoker().invoke();
        LOGGER.info("Registries repopulated in %s", stageStopwatch.stop());
        stageStopwatch.reset().start();

        // Check if config is OK
        final List<String> errors = new ArrayList<>();
        if (CONFIG.get().httpPort == null) errors.add("Field 'httpPort' not set!");
        if (CONFIG.get().minecraftServerExternalPort == null) errors.add("Field 'minecraftServerExternalPort' not set!");
        if (!errors.isEmpty()) {
            LOGGER.error("There were problems with the config for MESH Lib, mod will be disabled, see below for more details!");
            errors.stream().map(string -> "    " + string).forEach(LOGGER::error);
            return;
        }

        // Initialize
        if (Objects.equals(CONFIG.get().minecraftServerExternalPort, CONFIG.get().httpPort)) {
            LOGGER.info("Initializing MESH Lib on vanilla port %s...", CONFIG.get().minecraftServerExternalPort);
            PlatformUtil.enableVanillaHandler();
            LOGGER.info("MESH Lib initialized on vanilla port %s!", CONFIG.get().minecraftServerExternalPort);
        } else {
            LOGGER.info("Initializing MESH Lib on custom port %s...", CONFIG.get().httpPort);
            NettyServer.start();
            LOGGER.info("MESH Lib initialized on custom port %s!", CONFIG.get().httpPort);
        }

        LOGGER.info("MESH Lib reloaded in %s!", fullStopwatch.stop());
    }

    private static void disableAllHandlers() {
        PlatformUtil.disableVanillaHandler();
        NettyServer.stop();
    }


    public static <T> T load(Class<T> clazz) {
        LOGGER.info("Loading service for: %s", clazz);
        return ServiceLoader.load(clazz, MESHLib.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to load service for " + clazz.getName()));
    }
}
