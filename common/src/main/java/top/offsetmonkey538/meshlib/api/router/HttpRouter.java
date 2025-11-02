package top.offsetmonkey538.meshlib.api.router;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.rule.HttpRuleTypeRegistry;
import top.offsetmonkey538.meshlib.impl.router.HttpHandlerTypeRegistryImpl;
import top.offsetmonkey538.meshlib.impl.router.rule.HttpRuleTypeRegistryImpl;

import java.net.URI;
import java.nio.file.Path;

public record HttpRouter(@NotNull HttpRule rule, @NotNull HttpHandler handler) {

    /**
     * Configures the provided {@link Jankson.Builder} with serializers and deserializers for {@link HttpRule}s and {@link HttpHandler}s.
     * <p>
     *     When using my own config library, OffsetConfig538, its {@link top.offsetmonkey538.offsetconfig538.api.event.OffsetConfig538Events#JANKSON_CONFIGURATION_EVENT JANKSON_CONFIGURATION_EVENT} will have this configurator registered already and there's no need to call this method.
     *     <br>
     *     For other config libraries... idk try to understand whatever the fuck I'm doing in this method I guess.....
     * </p>
     *
     * @param janksonBuilder the builder to configure
     * @return the builder instance
     */
    public static Jankson.Builder configureJankson(final @NotNull Jankson.Builder janksonBuilder) {
        janksonBuilder.registerSerializer(HttpRule.class, (httpRule, marshaller) -> {
            @SuppressWarnings({"unchecked", "deprecation"})
            // rule definition of ?,? extends HttpRule should match ?,HttpHandler, no?
            final HttpRuleTypeRegistryImpl.HttpRuleDefinition<?, HttpRule> ruleDefinition = (HttpRuleTypeRegistryImpl.HttpRuleDefinition<?, HttpRule>) ((HttpRuleTypeRegistryImpl) HttpRuleTypeRegistry.INSTANCE).get(httpRule.getClass());

            final JsonObject result = (JsonObject) marshaller.serialize(ruleDefinition.ruleToData().apply(httpRule));
            result.put("type", JsonPrimitive.of(ruleDefinition.type()));
            return result;
        });

        janksonBuilder.registerDeserializer(JsonObject.class, HttpRule.class, (jsonObject, marshaller) -> {
            final String type = jsonObject.get(String.class, "type");

            @SuppressWarnings({"unchecked", "deprecation"}) // It's proooobably a subclass of Object...
            final HttpRuleTypeRegistryImpl.HttpRuleDefinition<Object, ?> ruleDefinition = (HttpRuleTypeRegistryImpl.HttpRuleDefinition<Object, ?>) ((HttpRuleTypeRegistryImpl) HttpRuleTypeRegistry.INSTANCE).get(type);

            final JsonObject dummyParent = new JsonObject();
            jsonObject.remove("type");
            dummyParent.put("dataHolder", jsonObject);
            final Object dataHolder = dummyParent.get(ruleDefinition.dataType(), "dataHolder");

            return ruleDefinition.dataToRule().apply(dataHolder);
        });


        janksonBuilder.registerSerializer(HttpHandler.class, (httpHandler, marshaller) -> {
            @SuppressWarnings({"unchecked", "deprecation"})
            // handler definition of ?,? extends HttpHandler should match ?,HttpHandler, no?
            final HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?, HttpHandler> handlerDefinition = (HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?, HttpHandler>) ((HttpHandlerTypeRegistryImpl) HttpHandlerTypeRegistry.INSTANCE).get(httpHandler.getClass());

            final JsonObject result = (JsonObject) marshaller.serialize(handlerDefinition.handlerToData().apply(httpHandler));
            result.put("type", JsonPrimitive.of(handlerDefinition.type()));
            return result;
        });

        janksonBuilder.registerDeserializer(JsonObject.class, HttpHandler.class, (jsonObject, marshaller) -> {
            final String type = jsonObject.get(String.class, "type");

            @SuppressWarnings({"unchecked", "deprecation"}) // It's proooobably a subclass of Object...
            final HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<Object, ?> handlerDefinition = (HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<Object, ?>) ((HttpHandlerTypeRegistryImpl) HttpHandlerTypeRegistry.INSTANCE).get(type);

            final JsonObject dummyParent = new JsonObject();
            jsonObject.remove("type");
            dummyParent.put("dataHolder", jsonObject);
            final Object dataHolder = dummyParent.get(handlerDefinition.dataType(), "dataHolder");

            return handlerDefinition.dataToHandler().apply(dataHolder);
        });

        return janksonBuilder;
    }
}
