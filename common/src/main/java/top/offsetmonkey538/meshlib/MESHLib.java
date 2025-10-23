package top.offsetmonkey538.meshlib;

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.handlers.StaticDirectoryHandler;
import top.offsetmonkey538.meshlib.api.handler.handlers.StaticFileHandler;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.rule.HttpRuleTypeRegistry;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.example.ExampleMain;
import top.offsetmonkey538.meshlib.api.rule.rules.DomainHttpRule;
import top.offsetmonkey538.meshlib.api.rule.rules.PathHttpRule;
import top.offsetmonkey538.meshlib.impl.router.HttpHandlerTypeRegistryImpl;
import top.offsetmonkey538.meshlib.platform.PlatformMain;
import top.offsetmonkey538.monkeylib538.api.log.MonkeyLibLogger;
import top.offsetmonkey538.offsetconfig538.api.event.OffsetConfig538Events;

import java.util.ServiceLoader;

// TODO: once I start working on allowing the netty server to run separately from minecraft, I should make the default exposed port value null to avoid 2 things:
//  - The port from server.properties being wrong: The network being behind something (like a firewall or Docker for example) can allow the exposed port to be different from what minecraft binds to.
//  - Tell people to use a separate port if at all possible. Proxies that some like running minecraft through (especially DDOS blockers like TcpShield) may block http traffic.
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


    public static void initialize() {
        PlatformMain.enableVanillaHandler();
        ExampleMain.onInitialize();


        OffsetConfig538Events.JANKSON_CONFIGURATION_EVENT.listen(builder -> {
            builder.registerSerializer(HttpRule.class, (httpRule, marshaller) -> {
                final JsonObject result = (JsonObject) marshaller.serialize(httpRule.getData());
                result.put("type", JsonPrimitive.of(httpRule.getType()));
                return result;
            });

            builder.registerDeserializer(JsonObject.class, HttpRule.class, (jsonObject, marshaller) -> {
                final String type = jsonObject.get(String.class, "type");

                @SuppressWarnings("unchecked") // It's proooobably a subclass of Object...
                HttpRule.HttpRuleDefinition<Object> ruleDefinition = (HttpRule.HttpRuleDefinition<Object>) HttpRuleTypeRegistry.get(type);

                final JsonObject dummyParent = new JsonObject();
                dummyParent.put("dataHolder", jsonObject);
                final Object dataHolder = dummyParent.get(ruleDefinition.dataType(), "dataHolder");

                return ruleDefinition.ruleInitializer().apply(dataHolder);
            });


            builder.registerSerializer(HttpHandler.class, (httpHandler, marshaller) -> {
                @SuppressWarnings("unchecked") // handler definition of ?,? extends HttpHandler should match ?,HttpHandler, no?
                HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?,HttpHandler> handlerDefinition = (HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?, HttpHandler>) ((HttpHandlerTypeRegistryImpl) HttpHandlerTypeRegistry.INSTANCE).get(httpHandler.getClass());

                final JsonObject result = (JsonObject) marshaller.serialize(handlerDefinition.handlerToData().apply(httpHandler));
                result.put("type", JsonPrimitive.of(handlerDefinition.type()));
                return result;
            });

            builder.registerDeserializer(JsonObject.class, HttpHandler.class, (jsonObject, marshaller) -> {
                final String type = jsonObject.get(String.class, "type");

                @SuppressWarnings("unchecked") // It's proooobably a subclass of Object...
                HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<Object,?> handlerDefinition = (HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<Object, ?>) ((HttpHandlerTypeRegistryImpl) HttpHandlerTypeRegistry.INSTANCE).get(type);

                final JsonObject dummyParent = new JsonObject();
                dummyParent.put("dataHolder", jsonObject);
                final Object dataHolder = dummyParent.get(handlerDefinition.dataType(), "dataHolder");

                return handlerDefinition.handlerInitializer().apply(dataHolder);
            });
        });

        HttpRuleTypeRegistry.register("domain", new HttpRule.HttpRuleDefinition<>(DomainHttpRule.Data.class, data -> new DomainHttpRule(data.value)));
        HttpRuleTypeRegistry.register("path", new HttpRule.HttpRuleDefinition<>(PathHttpRule.Data.class, data -> new PathHttpRule(data.value)));


        HttpHandlerTypeRegistry.register("static-file", StaticFileHandler.Data.class, StaticFileHandler.class, handler -> new StaticFileHandler.Data(handler.fileToServe), data -> new StaticFileHandler(data.fileToServe()));
        HttpHandlerTypeRegistry.register("static-directory", StaticDirectoryHandler.Data.class, StaticDirectoryHandler.class, handler -> new StaticDirectoryHandler.Data(handler.baseDir, handler.allowDirectoryList), data -> new StaticDirectoryHandler(data.baseDir(), data.allowDirectoryList()));
    }


    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz, clazz.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to load service for " + clazz.getName()));
    }
}
