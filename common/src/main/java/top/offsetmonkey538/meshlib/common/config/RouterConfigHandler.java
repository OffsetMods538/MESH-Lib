package top.offsetmonkey538.meshlib.common.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.DeserializationException;
import blue.endless.jankson.api.SyntaxError;
import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.common.api.handler.handlers.StaticContentHandler;
import top.offsetmonkey538.meshlib.common.api.handler.handlers.StaticDirectoryHandler;
import top.offsetmonkey538.meshlib.common.api.handler.handlers.StaticFileHandler;
import top.offsetmonkey538.meshlib.common.api.router.HttpRouter;
import top.offsetmonkey538.meshlib.common.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.common.api.rule.rules.DomainHttpRule;
import top.offsetmonkey538.meshlib.common.api.rule.rules.PathHttpRule;
import top.offsetmonkey538.monkeylib538.common.api.command.CommandAbstractionApi;
import top.offsetmonkey538.monkeylib538.common.api.platform.LoaderUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static top.offsetmonkey538.meshlib.common.MESHLib.LOGGER;
import static top.offsetmonkey538.meshlib.common.MESHLib.MOD_ID;
import static top.offsetmonkey538.monkeylib538.common.api.command.CommandAbstractionApi.literal;

public final class RouterConfigHandler {
    private RouterConfigHandler() {

    }

    private static final Path ROUTERS_DIR = LoaderUtil.getConfigDir().resolve(MOD_ID).resolve("routers").normalize().toAbsolutePath();

    public static LiteralArgumentBuilder<?> createExampleConfigCommand() {
        final LiteralArgumentBuilder<Object> exampleCommand = literal("example").requires(CommandAbstractionApi::isOp);
        Consumer<CommandContext<Object>> allHandler = context -> {};

        for (final Map.Entry<String, HttpRouter> exampleRouter : Map.of(
                "server-properties.json", new HttpRouter(new PathHttpRule("example/server-properties"), new StaticFileHandler(Path.of("server.properties"))),
                "ops.json", new HttpRouter(new PathHttpRule("example/ops"), new StaticFileHandler(Path.of("ops.json"))),
                "directory.json", new HttpRouter(new DomainHttpRule("directory.example.com"), new StaticDirectoryHandler(Path.of("."), true)),
                "index.json", new HttpRouter(new DomainHttpRule("docs.example.com"), new StaticDirectoryHandler(Path.of("/home/dave/Dev/Java/Minecraft/Mods/Loot-Table-Modifier/docs/dist/"), false)),
                "hello.json", new HttpRouter(new PathHttpRule("/example/hello"), new StaticContentHandler("""
                        Hello World!
                        ...
                        ...
                        Goodbye! :P
                        """))
        ).entrySet()) {
            allHandler = allHandler.andThen(context -> runCommand(exampleRouter, context));

            final LiteralArgumentBuilder<Object> routerCommand = literal(exampleRouter.getKey());
            routerCommand.executes(context -> runCommand(exampleRouter, context));
            exampleCommand.then(routerCommand);
        }

        final Consumer<CommandContext<Object>> finalAllHandler = allHandler;
        return literal(MOD_ID).then(exampleCommand.then(literal("all").executes(context -> {
            finalAllHandler.accept(context);
        return 1;
        })));
    }

    private static int runCommand(final Map.Entry<String, HttpRouter> exampleRouter, final CommandContext<Object> context) {
        final Path routerPath = ROUTERS_DIR.resolve("example").resolve(exampleRouter.getKey()).normalize().toAbsolutePath();

        boolean success;
        try {
            success = save(routerPath, exampleRouter.getValue());
        } catch (Exception e) {
            LOGGER.error("Failed to create example config at '%s'!", e);
            success = false;
        }

        if (!success) {
            CommandAbstractionApi.sendError(context, "Failed to create example config at '%s'! See log for more details", routerPath);
            return 0;
        }

        CommandAbstractionApi.sendMessage(context, "Created example config at '%s'!", routerPath);
        return 1;
    }

    private static boolean save(final Path path, final HttpRouter router) {
        final String routerId = ROUTERS_DIR.relativize(path).toString();
        final Jankson jankson = configureJankson();

        // Convert to json
        final JsonElement jsonAsElement = jankson.toJson(router);
        if (!(jsonAsElement instanceof final JsonObject json)) {
            LOGGER.error("Router '%s' could not be serialized to a 'JsonObject', got '%s' instead! Router will not be saved.", routerId, jsonAsElement.getClass().getName());
            return false;
        }

        // Convert to string
        final String result = json.toJson(false, true);

        // Save
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, result);
        } catch (IOException e) {
            LOGGER.error("Config file '%s' could not be saved!", e, routerId);
            return false;
        }

        return true;
    }

    public static void init(final HttpRouterRegistry registry) {
        if (!Files.exists(ROUTERS_DIR)) try {
            Files.createDirectories(ROUTERS_DIR);
        } catch (IOException e) {
            LOGGER.error("Failed to create routers directory at '%s'!", e, ROUTERS_DIR);
            return;
        }

        if (!Files.isDirectory(ROUTERS_DIR)) {
            LOGGER.error("'%s' is not a directory!", ROUTERS_DIR);
            return;
        }

        final List<Path> configFiles;
        try {
            configFiles = gatherConfigFiles();
        } catch (IOException e) {
            LOGGER.error("Failed to gather config files from '%s'!", e, ROUTERS_DIR);
            return;
        }

        // Load and register
        loadRouters(configFiles, configureJankson()).forEach(registry::register);
    }

    private static List<Path> gatherConfigFiles() throws IOException {
        try (final Stream<Path> files = Files.walk(ROUTERS_DIR)) {
            return files
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .toList();
        }
    }

    private static @NotNull Jankson configureJankson() {
        return HttpRouter.configureJankson(Jankson.builder()).build();
    }

    private static @Unmodifiable Map<String, HttpRouter> loadRouters(final @NotNull List<Path> configFiles, final @NotNull Jankson jankson) throws IllegalArgumentException {
        final ImmutableMap.Builder<String, HttpRouter> resultBuilder = ImmutableMap.builder();

        for (final Path path : configFiles) {
            final String id = ROUTERS_DIR.relativize(path).toString();

            try {
                resultBuilder.put(id, loadRouter(id, path, jankson));
            } catch (IOException e) {
                LOGGER.error("Router configuration file '%s' could not be read!!", e, id);
            } catch (SyntaxError e) {
                LOGGER.error("Router configuration file '%s' is malformed!", e, id);
                LOGGER.error(e.getMessage());
                LOGGER.error(e.getLineMessage());
            } catch (Exception e) {
                LOGGER.error("Failed to turn deserialized router configuration file '%s' into an HttpRouter!", e, id);
            }
        }

        try {
            return resultBuilder.buildOrThrow();
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to build map of id to HttpRouter, no routers will be loaded from config dir!", e);
            return Map.of();
        }
    }

    @SuppressWarnings("DuplicateThrows")
    private static HttpRouter loadRouter(final @NotNull String id, final @NotNull Path path, final @NotNull Jankson jankson) throws IOException, SyntaxError, Exception {
        final JsonObject json = jankson.load(Files.newInputStream(path));

        try {
            return jankson.fromJsonCarefully(json, JanksonHttpRouter.class).toRouter();
        } catch (DeserializationException e) {
            LOGGER.error("Failed to deserialize router configuration file '%s'!", e, id);
            return jankson.fromJson(json, JanksonHttpRouter.class).toRouter();
        }
    }

    /**
     * Exists because jankson requires a no-arg constructor to create an instance and then modify its fields, which wouldn't be possible with the record {@link HttpRouter}
     */
    @SuppressWarnings({"unused", "FieldMayBeFinal"})
    private static class JanksonHttpRouter {
        private HttpRule rule = null;
        private HttpHandler handler = null;

        public JanksonHttpRouter() {

        }

        public HttpRouter toRouter() throws Exception {
            if (rule == null) throw new Exception("rule is null");
            if (handler == null) throw new Exception("handler is null");
            return new HttpRouter(rule, handler);
        }
    }
}
