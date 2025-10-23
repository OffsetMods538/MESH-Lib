package top.offsetmonkey538.meshlib.api.rule;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.impl.router.rule.HttpRuleTypeRegistryImpl;
import top.offsetmonkey538.offsetconfig538.api.event.Event;

import java.util.function.Function;

/**
 * Registry for {@link HttpRule}s, use the {@link #HTTP_RULE_REGISTRATION_EVENT} event for registering your rules.
 */
public interface HttpRuleTypeRegistry {
    /**
     * Instance
     */
    @ApiStatus.Internal
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated // Marking it as internal isn't enough cause I also need to prevent usage from other places in my code
    HttpRuleTypeRegistry INSTANCE = new HttpRuleTypeRegistryImpl();

    /**
     * Internal method for clearing the registry, no touch!
     */
    @ApiStatus.Internal
    static void clear() {
        INSTANCE.clearImpl();
    }

    void clearImpl();
    <D, R extends HttpRule> void register(@NotNull final String type, @NotNull final Class<D> dataType, @NotNull final Class<R> ruleType, @NotNull final Function<R, D> ruleToData, @NotNull final Function<D, R> dataToRule);


    /**
     * Event for registering http routing rules.
     * <p>
     *     The registry is cleared upon reloading, so to make your rules persist, you need to register them in this event.
     * </p>
     * <p>
     *     Initially called while the server is starting, so <strong>make sure to register your handler before that!</strong>
     * </p>
     * <p>
     *     Called before the {@link top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry#HTTP_ROUTER_REGISTRATION_EVENT HTTP_ROUTER_REGISTRATION_EVENT} event.
     * </p>
     */
    Event<HttpRuleRegistrationEvent> HTTP_RULE_REGISTRATION_EVENT = Event.createEvent(HttpRuleRegistrationEvent.class, handlers -> registry -> {
        for (HttpRuleRegistrationEvent handler : handlers) handler.register(registry);
    });

    /**
     * Handler for {@link #HTTP_RULE_REGISTRATION_EVENT}.
     */
    @FunctionalInterface
    interface HttpRuleRegistrationEvent {

        /**
         * Internal method for invoking the event without providing the registry, no touch!
         */
        @ApiStatus.Internal
        default void invoke() {
            register(INSTANCE);
        }

        /**
         * Registers {@link HttpRule}s to the provided registry
         *
         * @param registry the registry to register to
         */
        void register(final @NotNull HttpRuleTypeRegistry registry);
    }
}
