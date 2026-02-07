package top.offsetmonkey538.meshlib.common.api.rule;

import top.offsetmonkey538.meshlib.common.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.common.impl.router.rule.HttpRuleTypeRegistryImpl;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;
import top.offsetmonkey538.offsetutils538.api.event.Event;

import java.util.function.Function;

/**
 * Registry for {@link HttpRule}s, use the {@link #HTTP_RULE_REGISTRATION_EVENT} event for registering your rules.
 */
public interface HttpRuleTypeRegistry {
    /**
     * Instance
     */
    @Internal
    HttpRuleTypeRegistry INSTANCE = new HttpRuleTypeRegistryImpl();

    /**
     * Internal method for clearing the registry, no touch!
     */
    @Internal
    static void clear() {
        INSTANCE.clearImpl();
    }

    @Internal void clearImpl();
    <D, R extends HttpRule> void register(final String type, final Class<D> dataType, final Class<R> ruleType, final Function<R, D> ruleToData, final Function<D, R> dataToRule);


    /**
     * Event for registering http routing rules.
     * <p>
     *     The registry is cleared upon reloading, so to make your rules persist, you need to register them in this event.
     * </p>
     * <p>
     *     Initially called while the server is starting, so <strong>make sure to register your handler before that!</strong>
     * </p>
     * <p>
     *     Called before the {@link HttpRouterRegistry#HTTP_ROUTER_REGISTRATION_EVENT HTTP_ROUTER_REGISTRATION_EVENT} event.
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
        @Internal
        default void invoke() {
            register(INSTANCE);
        }

        /**
         * Registers {@link HttpRule}s to the provided registry
         *
         * @param registry the registry to register to
         */
        void register(final HttpRuleTypeRegistry registry);
    }
}
