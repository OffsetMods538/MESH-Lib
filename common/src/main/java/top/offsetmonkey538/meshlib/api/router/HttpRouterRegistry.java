package top.offsetmonkey538.meshlib.api.router;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.impl.HttpRouterRegistryImpl;
import top.offsetmonkey538.offsetconfig538.api.event.Event;

/**
 * Registry for {@link HttpRouter}s, use the {@link #HTTP_ROUTER_REGISTRATION_EVENT_EVENT} event for registering your routers.
 */
public interface HttpRouterRegistry {
    /**
     * Instance
     */
    @ApiStatus.Internal
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated // Marking it as internal isn't enough cause I also need to prevent usage from other places in my code
    HttpRouterRegistry INSTANCE = new HttpRouterRegistryImpl();

    /**
     * Internal method for clearing the registry, no touch!
     */
    @ApiStatus.Internal
    static void clear() {
        INSTANCE.clearImpl();
    }

    void clearImpl();
    void register(@NotNull final String id, @NotNull final HttpRouter router);


    /**
     * Event for registering http routers.
     * <p>
     *     The registry is cleared upon reloading, so to make your routers persist, you need to register them in this event.
     * </p>
     * <p>
     *     Initially called while the server is starting, so <strong>make sure to register your handler before that!</strong>
     * </p>
     */
    Event<HttpRouterRegistrationEvent> HTTP_ROUTER_REGISTRATION_EVENT_EVENT = Event.createEvent(HttpRouterRegistrationEvent.class, handlers -> registry -> {
        for (HttpRouterRegistrationEvent handler : handlers) handler.register(registry);
    });

    /**
     * Handler for {@link #HTTP_ROUTER_REGISTRATION_EVENT_EVENT}.
     */
    @FunctionalInterface
    interface HttpRouterRegistrationEvent {

        /**
         * Internal method for invoking the event without providing the registry, no touch!
         */
        @ApiStatus.Internal
        default void invoke() {
            register(INSTANCE);
        }

        /**
         * Registers {@link HttpRouter}s to the provided registry
         *
         * @param registry the registry to register to
         */
        void register(final @NotNull HttpRouterRegistry registry);
    }
}
