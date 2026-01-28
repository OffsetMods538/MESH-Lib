package top.offsetmonkey538.meshlib.common.impl;

import com.google.common.base.Stopwatch;
import org.jspecify.annotations.Nullable;
import top.offsetmonkey538.meshlib.common.api.MESHLibApi;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.common.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRuleTypeRegistry;
import top.offsetmonkey538.meshlib.common.netty.NettyServer;
import top.offsetmonkey538.meshlib.common.platform.PlatformUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.offsetmonkey538.meshlib.common.MESHLib.CONFIG;
import static top.offsetmonkey538.meshlib.common.MESHLib.LOGGER;
import static top.offsetmonkey538.meshlib.common.MESHLib.disableAllHandlers;

public final class MESHLibApiImpl implements MESHLibApi {
    public void reloadImpl() {
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

    @Override
    public @Nullable Integer getExternalPortImpl() {
        if (CONFIG.get().exposedPort != null) return CONFIG.get().exposedPort;
        return CONFIG.get().httpPort;
    }
}
