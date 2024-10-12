package top.offsetmonkey538.meshlib.impl;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.HttpHandler;
import top.offsetmonkey538.meshlib.api.HttpHandlerRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link HttpHandlerRegistry}
 */
public class HttpHandlerRegistryImpl implements HttpHandlerRegistry {
    private final Map<String, HttpHandler> handlers = new HashMap<>();

    @Override
    public void register(@NotNull String id, @NotNull HttpHandler handler) throws IllegalArgumentException {
        if (id.isEmpty()) throw new IllegalArgumentException("Id may not be empty!");

        handlers.put(id, handler);
    }

    @Override
    public @NotNull HttpHandler get(@NotNull String id) throws IllegalStateException {
        if (handlers.containsKey(id)) return handlers.get(id);
        throw new IllegalStateException("Handler with id '" + id + "' not registered!");
    }

    @Override
    public boolean has(@NotNull String id) {
        return handlers.containsKey(id);
    }
}
