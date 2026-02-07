package top.offsetmonkey538.meshlib.common.api.router;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.common.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRuleTypeRegistry;
import top.offsetmonkey538.meshlib.common.impl.router.HttpHandlerTypeRegistryImpl;
import top.offsetmonkey538.meshlib.common.impl.router.rule.HttpRuleTypeRegistryImpl;

public record HttpRouter(HttpRule rule, HttpHandler handler) {

    /**
     * Configures the provided {@link Jankson.Builder} with serializers and deserializers for {@link HttpRule}s and {@link HttpHandler}s.
     * <p>
     *     When using my own config library, OffsetUtils538, its {@link top.offsetmonkey538.offsetutils538.api.config.event.JanksonConfigurationEvent#JANKSON_CONFIGURATION_EVENT JANKSON_CONFIGURATION_EVENT} will have this configurator registered already and there's no need to call this method.
     *     <br>
     *     For other config libraries... idk try to understand whatever the fuck I'm doing in this method I guess.....
     * </p>
     *
     * @param janksonBuilder the builder to configure
     * @return the builder instance
     */
    public static Jankson.Builder configureJankson(final Jankson.Builder janksonBuilder) {
        janksonBuilder.registerSerializer(HttpRule.class, (httpRule, marshaller) -> {
            @SuppressWarnings({"unchecked"})
            // rule definition of ?,? extends HttpRule should match ?,HttpHandler, no?
            final HttpRuleTypeRegistryImpl.HttpRuleDefinition<?, HttpRule> ruleDefinition = (HttpRuleTypeRegistryImpl.HttpRuleDefinition<?, HttpRule>) ((HttpRuleTypeRegistryImpl) HttpRuleTypeRegistry.INSTANCE).get(httpRule.getClass());

            final JsonObject result = (JsonObject) marshaller.serialize(ruleDefinition.ruleToData().apply(httpRule));
            result.put("type", JsonPrimitive.of(ruleDefinition.type()));
            return result;
        });

        janksonBuilder.registerDeserializer(JsonObject.class, HttpRule.class, (jsonObject, marshaller) -> {
            final String type = jsonObject.get(String.class, "type");
            if (type == null) throw new RuntimeException("HttpRule doesn't contain 'type' field!");

            @SuppressWarnings({"unchecked"}) // It's proooobably a subclass of Object...
            final HttpRuleTypeRegistryImpl.HttpRuleDefinition<Object, ?> ruleDefinition = (HttpRuleTypeRegistryImpl.HttpRuleDefinition<Object, ?>) ((HttpRuleTypeRegistryImpl) HttpRuleTypeRegistry.INSTANCE).get(type);

            final JsonObject dummyParent = new JsonObject();
            jsonObject.remove("type");
            dummyParent.put("dataHolder", jsonObject);
            final Object dataHolder = dummyParent.get(ruleDefinition.dataType(), "dataHolder");

            assert dataHolder != null;
            return ruleDefinition.dataToRule().apply(dataHolder);
        });


        janksonBuilder.registerSerializer(HttpHandler.class, (httpHandler, marshaller) -> {
            @SuppressWarnings({"unchecked"})
            // handler definition of ?,? extends HttpHandler should match ?,HttpHandler, no?
            final HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?, HttpHandler> handlerDefinition = (HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?, HttpHandler>) HttpHandlerTypeRegistry.INSTANCE.get(httpHandler.getClass());

            final JsonObject result = (JsonObject) marshaller.serialize(handlerDefinition.handlerToData().apply(httpHandler));
            result.put("type", JsonPrimitive.of(handlerDefinition.type()));
            return result;
        });

        janksonBuilder.registerDeserializer(JsonObject.class, HttpHandler.class, (jsonObject, marshaller) -> {
            final String type = jsonObject.get(String.class, "type");
            if (type == null) throw new RuntimeException("HttpRule doesn't contain 'type' field!");

            @SuppressWarnings({"unchecked"}) // It's proooobably a subclass of Object...
            final HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<Object, ?> handlerDefinition = (HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<Object, ?>) HttpHandlerTypeRegistry.INSTANCE.get(type);

            final JsonObject dummyParent = new JsonObject();
            jsonObject.remove("type");
            dummyParent.put("dataHolder", jsonObject);
            final Object dataHolder = dummyParent.get(handlerDefinition.dataType(), "dataHolder");

            assert dataHolder != null;
            return handlerDefinition.dataToHandler().apply(dataHolder);
        });

        return janksonBuilder;
    }
}
