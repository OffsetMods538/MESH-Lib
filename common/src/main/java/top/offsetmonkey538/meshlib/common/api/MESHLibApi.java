package top.offsetmonkey538.meshlib.common.api;

import org.jspecify.annotations.Nullable;
import top.offsetmonkey538.meshlib.common.impl.MESHLibApiImpl;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

/**
 * Api for interacting with mesh lib
 */
public interface MESHLibApi {
    @Internal
    MESHLibApi INSTANCE = new MESHLibApiImpl();

    /**
     * Reloads MESH Lib.
     */
    static void reload() {
        INSTANCE.reloadImpl();
    }

    /**
     * Provides the external port as defined in the MESH Lib config.
     * <p>{@code null} value indicates that MESH Lib has not been set up correctly and isn't running.</p>
     *
     * @return the port the server is accessible from externally
     */
    static @Nullable Integer getExternalPort() {
        return INSTANCE.getExternalPortImpl();
    }


    @Internal void reloadImpl();
    @Internal @Nullable Integer getExternalPortImpl();
}
