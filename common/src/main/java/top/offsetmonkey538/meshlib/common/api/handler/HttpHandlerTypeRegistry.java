package top.offsetmonkey538.meshlib.common.api.handler;

import top.offsetmonkey538.meshlib.common.api.router.HttpRouterRegistry;
import top.offsetmonkey538.meshlib.common.impl.router.HttpHandlerTypeRegistryImpl;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;
import top.offsetmonkey538.offsetutils538.api.event.Event;

import java.util.function.Function;

/**
 * Registry for {@link HttpHandler}s, use the {@link #HTTP_HANDLER_REGISTRATION_EVENT} event for registering your handlers.
 */
public interface HttpHandlerTypeRegistry {

    /**
     * Instance
     */
    @Internal
    HttpHandlerTypeRegistry INSTANCE = new HttpHandlerTypeRegistryImpl();

    /**
     * Internal method for clearing the registry, no touch!
     */
    @Internal
    static void clear() {
        INSTANCE.clearImpl();
    }

    void clearImpl();
    <D, H extends HttpHandler> void register(final String type, final Class<D> dataType, final Class<H> handlerType, final Function<H, D> handlerToData, final Function<D, H> dataToHandler);
    HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?,?> get(final String type) throws IllegalArgumentException;
    HttpHandlerTypeRegistryImpl.HttpHandlerDefinition<?,?> get(final Class<? extends HttpHandler> type) throws IllegalArgumentException;


    /**
     * Event for registering http handlers.
     * <p>
     *     The registry is cleared upon reloading, so to make your handlers persist, you need to register them in this event.
     * </p>
     * <p>
     *     Initially called while the server is starting, so <strong>make sure to register your handler before that!</strong>
     * </p>
     * <p>
     *     Called before the {@link HttpRouterRegistry#HTTP_ROUTER_REGISTRATION_EVENT HTTP_ROUTER_REGISTRATION_EVENT} event.
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
        @Internal
        default void invoke() {
            register(INSTANCE);
        }

        /**
         * Registers {@link HttpHandler}s to the provided registry
         *
         * @param registry the registry to register to
         */
        void register(final HttpHandlerTypeRegistry registry);
    }
}
