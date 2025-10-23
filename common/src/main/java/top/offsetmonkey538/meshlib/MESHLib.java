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
import top.offsetmonkey538.meshlib.impl.router.rule.HttpRuleTypeRegistryImpl;
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
                @SuppressWarnings("unchecked") // rule definition of ?,? extends HttpRule should match ?,HttpHandler, no?
                final HttpRuleTypeRegistryImpl.HttpRuleDefinition<?,HttpRule> ruleDefinition = (HttpRuleTypeRegistryImpl.HttpRuleDefinition<?, HttpRule>) ((HttpRuleTypeRegistryImpl) HttpHandlerTypeRegistry.INSTANCE).get(httpRule.getClass());

                final JsonObject result = (JsonObject) marshaller.serialize(ruleDefinition.ruleToData().apply(httpRule));
                result.put("type", JsonPrimitive.of(ruleDefinition.type()));
                return result;
            });

            builder.registerDeserializer(JsonObject.class, HttpRule.class, (jsonObject, marshaller) -> {
                final String type = jsonObject.get(String.class, "type");

                @SuppressWarnings("unchecked") // It's proooobably a subclass of Object...
                final HttpRuleTypeRegistryImpl.HttpRuleDefinition<Object,?> ruleDefinition = (HttpRuleTypeRegistryImpl.HttpRuleDefinition<Object,?>) ((HttpRuleTypeRegistryImpl) HttpRuleTypeRegistry.INSTANCE).get(type);

                final JsonObject dummyParent = new JsonObject();
                dummyParent.put("dataHolder", jsonObject);
                final Object dataHolder = dummyParent.get(ruleDefinition.dataType(), "dataHolder");

                return ruleDefinition.dataToRule().apply(dataHolder);
            });


            builder.registerSerializer(HttpHandler.class, (httpHandler, marshaller) -> {
                @SuppressWarnings("unchecked") // handler definition of ?,? extends HttpHandler should match ?,HttpHandler, no?
                final HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?,HttpHandler> handlerDefinition = (HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?, HttpHandler>) ((HttpHandlerTypeRegistryImpl) HttpHandlerTypeRegistry.INSTANCE).get(httpHandler.getClass());

                final JsonObject result = (JsonObject) marshaller.serialize(handlerDefinition.handlerToData().apply(httpHandler));
                result.put("type", JsonPrimitive.of(handlerDefinition.type()));
                return result;
            });

            builder.registerDeserializer(JsonObject.class, HttpHandler.class, (jsonObject, marshaller) -> {
                final String type = jsonObject.get(String.class, "type");

                @SuppressWarnings("unchecked") // It's proooobably a subclass of Object...
                final HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<Object,?> handlerDefinition = (HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<Object, ?>) ((HttpHandlerTypeRegistryImpl) HttpHandlerTypeRegistry.INSTANCE).get(type);

                final JsonObject dummyParent = new JsonObject();
                dummyParent.put("dataHolder", jsonObject);
                final Object dataHolder = dummyParent.get(handlerDefinition.dataType(), "dataHolder");

                return handlerDefinition.dataToHandler().apply(dataHolder);
            });
        });

        DomainHttpRule.register();
        PathHttpRule.register();

        StaticFileHandler.register();
        StaticDirectoryHandler.register();
    }


    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz, clazz.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to load service for " + clazz.getName()));
    }
}
