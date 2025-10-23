package top.offsetmonkey538.meshlib.api.handler;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.impl.router.HttpHandlerTypeRegistryImpl;
import top.offsetmonkey538.offsetconfig538.api.event.Event;

import java.util.function.Function;

/**
 * Registry for {@link HttpHandler}s, use the {@link #HTTP_HANDLER_REGISTRATION_EVENT} event for registering your handlers.
 */
public interface HttpHandlerTypeRegistry {

    /**
     * Instance
     */
    @ApiStatus.Internal
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated // Marking it as internal isn't enough cause I also need to prevent usage from other places in my code
    HttpHandlerTypeRegistry INSTANCE = new HttpHandlerTypeRegistryImpl();

    /**
     * Internal method for clearing the registry, no touch!
     */
    @ApiStatus.Internal
    static void clear() {
        INSTANCE.clearImpl();
    }

    void clearImpl();
    <D, H extends HttpHandler> void register(@NotNull final String type, @NotNull final Class<D> dataType, @NotNull final Class<H> handlerType, @NotNull final Function<H, D> handlerToData, @NotNull final Function<D, H> dataToHandler);


    /**
     * Event for registering http handlers.
     * <p>
     *     The registry is cleared upon reloading, so to make your handlers persist, you need to register them in this event.
     * </p>
     * <p>
     *     Initially called while the server is starting, so <strong>make sure to register your handler before that!</strong>
     * </p>
     * <p>
     *     Called before the {@link top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry#HTTP_ROUTER_REGISTRATION_EVENT HTTP_ROUTER_REGISTRATION_EVENT} event.
     * </p>
     */
    Event<HttpHandlerRegistrationEvent> HTTP_HANDLER_REGISTRATION_EVENT = Event.createEvent(HttpHandlerRegistrationEvent.class, handlers -> registry -> {
        for (HttpHandlerRegistrationEvent handler : handlers) handler.register(registry);
    });

    /**
     * Handler for {@link #HTTP_HANDLER_REGISTRATION_EVENT}.
     */
    @FunctionalInterface
    interface HttpHandlerRegistrationEvent {

        /**
         * Internal method for invoking the event without providing the registry, no touch!
         */
        @ApiStatus.Internal
        default void invoke() {
            register(INSTANCE);
        }

        /**
         * Registers {@link HttpHandler}s to the provided registry
         *
         * @param registry the registry to register to
         */
        void register(final @NotNull HttpHandlerTypeRegistry registry);
    }
}
