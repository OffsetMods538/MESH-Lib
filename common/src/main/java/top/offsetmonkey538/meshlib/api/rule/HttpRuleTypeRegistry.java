package top.offsetmonkey538.meshlib.api.rule;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.impl.router.rule.HttpRuleTypeRegistryImpl;

public interface HttpRuleTypeRegistry {
    /**
     * Instance
     */
    @ApiStatus.Internal
    HttpRuleTypeRegistry INSTANCE = new HttpRuleTypeRegistryImpl();

    static void register(final String type, @NotNull final HttpRule.HttpRuleDefinition<?> rule) {
        INSTANCE.registerImpl(type, rule);
    }

    static @NotNull HttpRule.HttpRuleDefinition<?> get(final String type) throws IllegalStateException {
        return INSTANCE.getImpl(type);
    }

    void registerImpl(final String type, @NotNull final HttpRule.HttpRuleDefinition<?> rule);
    @NotNull HttpRule.HttpRuleDefinition<?> getImpl(final String type) throws IllegalStateException;
}
