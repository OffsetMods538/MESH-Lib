package top.offsetmonkey538.meshlib.impl;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.router.HttpRouter;
import top.offsetmonkey538.meshlib.api.router.HttpRouterRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link HttpRouterRegistry}
 */
public class HttpRouterRegistryImpl implements HttpRouterRegistry {
    private final Map<String, HttpRouter> routers = new HashMap<>();

    @Override
    public void register(@NotNull String id, @NotNull HttpRouter router) {
        if (id.isEmpty()) throw new IllegalArgumentException("Id may not be empty!");
        if (routers.containsKey(id)) throw new IllegalArgumentException("Handler with id '" + id + "' already registered!");

        routers.put(id, router);
    }

    @Override
    public void clearImpl() {
        routers.clear();
    }

    public @NotNull Iterable<Map.Entry<String, HttpRouter>> iterable() {
        return this.routers.entrySet();
    }
}
