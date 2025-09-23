package top.offsetmonkey538.meshlib.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.router.rule.HttpRuleTypeRegistry;
import top.offsetmonkey538.meshlib.impl.router.HttpHandlerTypeRegistryImpl;
import top.offsetmonkey538.meshlib.impl.router.rule.HttpRuleTypeRegistryImpl;

public interface HttpHandlerTypeRegistry {
    /**
     * Instance
     */
    @ApiStatus.Internal
    HttpHandlerTypeRegistry INSTANCE = new HttpHandlerTypeRegistryImpl();

    static void register(@NotNull final Class<? extends HttpHandler> type, @NotNull final HttpHandler.HttpHandlerDefinition<?> handler) {
        INSTANCE.registerImpl(type, handler);
    }

    static HttpHandler.HttpHandlerDefinition<?> get(final String type) throws IllegalStateException {
        return INSTANCE.getImpl(type);
    }
    static HttpHandler.HttpHandlerDefinition<?> get(final Class<? extends HttpHandler> type) throws IllegalStateException {
        return INSTANCE.getImpl(type);
    }

    void registerImpl(@NotNull final Class<? extends HttpHandler> type, @NotNull final HttpHandler.HttpHandlerDefinition<?> handler);
    HttpHandler.HttpHandlerDefinition<?> getImpl(final String type) throws IllegalStateException;
    HttpHandler.HttpHandlerDefinition<?> getImpl(final Class<? extends HttpHandler> type) throws IllegalStateException;
}
