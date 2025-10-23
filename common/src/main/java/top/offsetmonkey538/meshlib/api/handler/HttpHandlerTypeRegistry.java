package top.offsetmonkey538.meshlib.api.handler;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.impl.router.HttpHandlerTypeRegistryImpl;

import java.util.function.Function;

public interface HttpHandlerTypeRegistry {
    /**
     * Instance
     */
    @ApiStatus.Internal
    HttpHandlerTypeRegistry INSTANCE = new HttpHandlerTypeRegistryImpl();

    static <T, H extends HttpHandler> void register(@NotNull final String type, @NotNull final Class<T> dataType, @NotNull final Class<H> handlerType, @NotNull final Function<H, T> handlerToData, @NotNull final Function<T, H> dataToHandler) {
        INSTANCE.registerImpl(type, dataType, handlerType, handlerToData, dataToHandler);
    }

    <T, H extends HttpHandler> void registerImpl(@NotNull final String type, @NotNull final Class<T> dataType, @NotNull final Class<H> handlerType, @NotNull final Function<H, T> handlerToData, @NotNull final Function<T, H> dataToHandler);
}
