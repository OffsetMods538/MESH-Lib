package top.offsetmonkey538.meshlib.api.rule;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.impl.router.rule.HttpRuleTypeRegistryImpl;

import java.util.function.Function;

public interface HttpRuleTypeRegistry {
    /**
     * Instance
     */
    @ApiStatus.Internal
    HttpRuleTypeRegistry INSTANCE = new HttpRuleTypeRegistryImpl();

    static <D, R extends HttpRule> void register(@NotNull final String type, @NotNull final Class<D> dataType, @NotNull final Class<R> ruleType, @NotNull final Function<R, D> ruleToData, @NotNull final Function<D, R> dataToRule) {
        INSTANCE.registerImpl(type, dataType, ruleType, ruleToData, dataToRule);
    }

    <D, R extends HttpRule> void registerImpl(@NotNull final String type, @NotNull final Class<D> dataType, @NotNull final Class<R> ruleType, @NotNull final Function<R, D> ruleToData, @NotNull final Function<D, R> dataToRule);
}
