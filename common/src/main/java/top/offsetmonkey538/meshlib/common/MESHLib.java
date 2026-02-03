package top.offsetmonkey538.meshlib.common;

import top.offsetmonkey538.meshlib.common.api.MESHLibApi;
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
import top.offsetmonkey538.monkeylib538.common.api.telemetry.TelemetryRegistry;
import top.offsetmonkey538.offsetutils538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetutils538.api.config.ConfigManager;
import top.offsetmonkey538.offsetutils538.api.config.event.JanksonConfigurationEvent;
import top.offsetmonkey538.offsetutils538.api.log.OffsetLogger;

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
        TelemetryRegistry.register(MOD_ID);

        PlatformUtil.enableVanillaHandler();
        ExampleMain.onInitialize();

        ConfigCommandApi.registerConfigCommand(CONFIG, MESHLibApi::reload, MOD_ID, "config");
        CommandRegistrationApi.registerCommand(RouterConfigHandler.createExampleConfigCommand());

        JanksonConfigurationEvent.JANKSON_CONFIGURATION_EVENT.listen(HttpRouter::configureJankson);

        HttpRuleTypeRegistry.HTTP_RULE_REGISTRATION_EVENT.listen(DomainHttpRule::register);
        HttpRuleTypeRegistry.HTTP_RULE_REGISTRATION_EVENT.listen(PathHttpRule::register);

        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticContentHandler::register);
        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticFileHandler::register);
        HttpHandlerTypeRegistry.HTTP_HANDLER_REGISTRATION_EVENT.listen(StaticDirectoryHandler::register);

        HttpRouterRegistry.HTTP_ROUTER_REGISTRATION_EVENT.listen(RouterConfigHandler::init);

        ServerLifecycleApi.STARTING.listen(MESHLibApi::reload);
    }

    public static void disableAllHandlers() {
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
