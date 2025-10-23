package top.offsetmonkey538.meshlib.impl.router;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.handler.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.rule.HttpRuleTypeRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation of {@link HttpRuleTypeRegistry}
 */
public class HttpHandlerTypeRegistryImpl implements HttpHandlerTypeRegistry {
    private final Map<String, HttpHandlerDefinition<?,?>> handlersById = new HashMap<>();
    private final Map<Class<? extends HttpHandler>, HttpHandlerDefinition<?,?>> handlersByType = new HashMap<>();

    @Override
    public <T, H extends HttpHandler> void registerImpl(@NotNull final String type, @NotNull final Class<T> dataType, @NotNull final Class<H> handlerType, @NotNull final Function<H, T> handlerToData, @NotNull final Function<T, H> dataToHandler) {
        if (type.isEmpty()) throw new IllegalArgumentException("Id may not be empty!");
        if (handlersById.containsKey(type)) throw new IllegalArgumentException("Handler type with id '" + type + "' already registered!");
        if (handlersByType.containsKey(handlerType)) throw new IllegalArgumentException("Handler type for type '" + handlerType + "' already registered!");

        final HttpHandlerDefinition<T,H> handler = new HttpHandlerDefinition<>(type, dataType, handlerType, handlerToData, dataToHandler);
        handlersById.put(type, handler);
        handlersByType.put(handler.handlerType(), handler);
    }

    public HttpHandlerDefinition<?,?> get(final String type) throws IllegalStateException {
        if (handlersById.containsKey(type)) return handlersById.get(type);
        throw new IllegalStateException("Http rule with type '" + type + "' not registered!");
    }

    public HttpHandlerDefinition<?,?> get(final Class<? extends HttpHandler> type) throws IllegalStateException {
        if (handlersByType.containsKey(type)) return handlersByType.get(type);
        throw new IllegalStateException("Http rule with type '" + type + "' not registered!");
    }

    public record HttpHandlerDefinition<T, H extends HttpHandler>(String type, Class<T> dataType, Class<H> handlerType, Function<H, T> handlerToData, Function<T, H> handlerInitializer) {

    }
}
