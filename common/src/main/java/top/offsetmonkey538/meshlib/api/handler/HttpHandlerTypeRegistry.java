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

    static <D, H extends HttpHandler> void register(@NotNull final String type, @NotNull final Class<D> dataType, @NotNull final Class<H> handlerType, @NotNull final Function<H, D> handlerToData, @NotNull final Function<D, H> dataToHandler) {
        INSTANCE.registerImpl(type, dataType, handlerType, handlerToData, dataToHandler);
    }

    <D, H extends HttpHandler> void registerImpl(@NotNull final String type, @NotNull final Class<D> dataType, @NotNull final Class<H> handlerType, @NotNull final Function<H, D> handlerToData, @NotNull final Function<D, H> dataToHandler);
}
