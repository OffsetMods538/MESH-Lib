package top.offsetmonkey538.meshlib.api.router;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.HttpHandler;
import top.offsetmonkey538.meshlib.impl.HttpRouterRegistryImpl;

import java.util.Map;

/**
 * Registry for routers
 * todo;K commebnt
 * <p>
 * Each handler will only be able to listen to requests on *its* sub-path.
 * <br>
 * If your handler's id is {@code testmod}, then your handler will only receive requests on {@code server.com/testmod/}
 * @see HttpHandler
 */
public interface HttpRouterRegistry {
    /**
     * Instance
     */
    @ApiStatus.Internal
    HttpRouterRegistry INSTANCE = new HttpRouterRegistryImpl();

    /**
     * todo;: actual comment
     * Method for registering a {@link HttpHandler}
     * <br>
     * THE ID SHOULD NOT BE EMPTY
     *
     * @param id your handler or mod's id
     * @throws IllegalArgumentException when the provided id is empty or a handler with this id is already registered
     * @see HttpHandler
     */
    void register(@NotNull String id, @NotNull HttpRouter router) throws IllegalArgumentException;

    @NotNull
    Iterable<Map.Entry<String, HttpRouter>> iterable();
}
