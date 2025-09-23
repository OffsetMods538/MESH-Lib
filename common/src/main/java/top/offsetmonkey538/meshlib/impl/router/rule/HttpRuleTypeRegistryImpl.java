package top.offsetmonkey538.meshlib.impl.router.rule;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.router.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.router.rule.HttpRuleTypeRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link HttpRuleTypeRegistry}
 */
public class HttpRuleTypeRegistryImpl implements HttpRuleTypeRegistry {
    private final Map<String, HttpRule.HttpRuleDefinition<?>> handlers = new HashMap<>();

    @Override
    public void registerImpl(String type, HttpRule.@NotNull HttpRuleDefinition<?> rule) {
        if (type.isEmpty()) throw new IllegalArgumentException("Id may not be empty!");
        if (handlers.containsKey(type)) throw new IllegalArgumentException("Handler with id '" + type + "' already registered!");

        handlers.put(type, rule);
    }

    @Override
    public @NotNull HttpRule.HttpRuleDefinition<?> getImpl(String type) throws IllegalStateException {
        if (handlers.containsKey(type)) return handlers.get(type);
        throw new IllegalStateException("Http rule with type '" + type + "' not registered!");
    }
}
