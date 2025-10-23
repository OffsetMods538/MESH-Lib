package top.offsetmonkey538.meshlib.impl.router.rule;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.rule.HttpRuleTypeRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation of {@link HttpRuleTypeRegistry}
 */
public class HttpRuleTypeRegistryImpl implements HttpRuleTypeRegistry {
    private final Map<String, HttpRuleDefinition<?,?>> rulesById = new HashMap<>();
    private final Map<Class<? extends HttpRule>, HttpRuleDefinition<?,?>> rulesByType = new HashMap<>();

    @Override
    public <D, R extends HttpRule> void registerImpl(@NotNull final String type, @NotNull final Class<D> dataType, @NotNull final Class<R> ruleType, @NotNull final Function<R, D> ruleToData, @NotNull final Function<D, R> dataToRule) {
        if (type.isEmpty()) throw new IllegalArgumentException("Id may not be empty!");
        if (rulesById.containsKey(type)) throw new IllegalArgumentException("Handler type with id '" + type + "' already registered!");
        if (rulesByType.containsKey(ruleType)) throw new IllegalArgumentException("Handler type for type '" + ruleType + "' already registered!");

        final HttpRuleDefinition<D,R> rule = new HttpRuleDefinition<>(type, dataType, ruleType, ruleToData, dataToRule);
        rulesById.put(type, rule);
        rulesByType.put(ruleType, rule);
    }

    public HttpRuleDefinition<?,?> get(final String type) throws IllegalArgumentException {
        if (rulesById.containsKey(type)) return rulesById.get(type);
        throw new IllegalArgumentException("Http rule with type '" + type + "' not registered!");
    }

    public HttpRuleDefinition<?,?> get(final Class<? extends HttpRule> type) throws IllegalArgumentException {
        if (rulesByType.containsKey(type)) return rulesByType.get(type);
        throw new IllegalArgumentException("Http rule with type '" + type + "' not registered!");
    }

    public record HttpRuleDefinition<D, R extends HttpRule>(String type, Class<D> dataType, Class<R> ruleType, Function<R, D> ruleToData, Function<D, R> dataToRule) {

    }
}
