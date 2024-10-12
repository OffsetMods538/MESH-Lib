package top.offsetmonkey538.meshlib.api;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.impl.HttpHandlerRegistryImpl;

/**
 * Registry for the handlers
 * <p>
 * Each handler will only be able to listen to requests on *its* sub-path.
 * <br>
 * If your handler's id is {@code testmod}, then your handler will only receive requests on {@code server.com/testmod/}
 * @see HttpHandler
 */
public interface HttpHandlerRegistry {
    /**
     * Instance
     */
    HttpHandlerRegistry INSTANCE = new HttpHandlerRegistryImpl();

    /**
     * Method for registering a {@link HttpHandler}
     * <br>
     * THE ID SHOULD NOT BE EMPTY
     *
     * @param id your handler or mod's id
     * @param handler the {@link HttpHandler} to be registered
     * @throws IllegalArgumentException when the provided id is empty
     * @see HttpHandler
     */
    void register(@NotNull String id, @NotNull HttpHandler handler) throws IllegalArgumentException;

    /**
     * Method for getting a registered {@link HttpHandler}
     *
     * @param id the handler's id
     * @return the {@link HttpHandler} for the provided id
     * @throws IllegalStateException when there is no {@link HttpHandler} registered for the provided id
     */
    @NotNull
    HttpHandler get(@NotNull String id) throws IllegalStateException;

    /**
     * Returns true if handler with provided id is registered, false otherwise
     *
     * @param id the id to check
     * @return true if handler with provided id is registered, false otherwise
     */
    boolean has(@NotNull String id);
}
