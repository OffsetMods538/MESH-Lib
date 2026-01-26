package top.offsetmonkey538.meshlib.common.api.router;

import top.offsetmonkey538.meshlib.common.impl.HttpRouterRegistryImpl;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;
import top.offsetmonkey538.offsetutils538.api.event.Event;

/**
 * Registry for {@link HttpRouter}s, use the {@link #HTTP_ROUTER_REGISTRATION_EVENT} event for registering your routers.
 */
public interface HttpRouterRegistry {
    /**
     * Instance
     */
    @Internal
    HttpRouterRegistry INSTANCE = new HttpRouterRegistryImpl();

    /**
     * Internal method for clearing the registry, no touch!
     */
    @Internal
    static void clear() {
        INSTANCE.clearImpl();
    }

    @Internal void clearImpl();
    void register(final String id, final HttpRouter router);


    /**
     * Event for registering http routers.
     * <p>
     *     The registry is cleared upon reloading, so to make your routers persist, you need to register them in this event.
     * </p>
     * <p>
     *     Initially called while the server is starting, so <strong>make sure to register your handler before that!</strong>
     * </p>
     */
    Event<HttpRouterRegistrationEvent> HTTP_ROUTER_REGISTRATION_EVENT = Event.createEvent(HttpRouterRegistrationEvent.class, handlers -> registry -> {
        for (HttpRouterRegistrationEvent handler : handlers) handler.register(registry);
    });

    /**
     * Handler for {@link #HTTP_ROUTER_REGISTRATION_EVENT}.
     */
    @FunctionalInterface
    interface HttpRouterRegistrationEvent {

        /**
         * Internal method for invoking the event without providing the registry, no touch!
         */
        @Internal
        default void invoke() {
            register(INSTANCE);
        }

        /**
         * Registers {@link HttpRouter}s to the provided registry
         *
         * @param registry the registry to register to
         */
        void register(final HttpRouterRegistry registry);
    }
}
