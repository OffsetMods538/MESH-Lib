package top.offsetmonkey538.meshlib.impl.router;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.HttpHandler;
import top.offsetmonkey538.meshlib.api.HttpHandlerTypeRegistry;
import top.offsetmonkey538.meshlib.api.router.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.router.rule.HttpRuleTypeRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link HttpRuleTypeRegistry}
 */
public class HttpHandlerTypeRegistryImpl implements HttpHandlerTypeRegistry {
    private final Map<String, HttpHandler.HttpHandlerDefinition<?>> handlersById = new HashMap<>();
    private final Map<Class<? extends HttpHandler>, HttpHandler.HttpHandlerDefinition<?>> handlersByType = new HashMap<>();

    @Override
    public void registerImpl(@NotNull final Class<? extends HttpHandler> type, @NotNull final HttpHandler.HttpHandlerDefinition<?> handler) {
        final String id = handler.type();
        if (id.isEmpty()) throw new IllegalArgumentException("Id may not be empty!");
        if (handlersById.containsKey(id)) throw new IllegalArgumentException("Handler type with id '" + type + "' already registered!");
        if (handlersByType.containsKey(type)) throw new IllegalArgumentException("Handler type for type '" + type + "' already registered!");

        handlersById.put(id, handler);
        handlersByType.put(type, handler);
    }

    @Override
    public HttpHandler.HttpHandlerDefinition<?> getImpl(final String type) throws IllegalStateException {
        if (handlersById.containsKey(type)) return handlersById.get(type);
        throw new IllegalStateException("Http rule with type '" + type + "' not registered!");
    }

    @Override
    public HttpHandler.HttpHandlerDefinition<?> getImpl(final Class<? extends HttpHandler> type) throws IllegalStateException {
        if (handlersByType.containsKey(type)) return handlersByType.get(type);
        throw new IllegalStateException("Http rule with type '" + type + "' not registered!");
    }
}
