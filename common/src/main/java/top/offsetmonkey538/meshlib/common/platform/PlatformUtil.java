package top.offsetmonkey538.meshlib.common.platform;

import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

import static top.offsetmonkey538.meshlib.common.MESHLib.load;

public interface PlatformUtil {
    @Internal
    PlatformUtil INSTANCE = load(PlatformUtil.class);

    static void enableVanillaHandler() {
        INSTANCE.enableVanillaHandlerImpl();
    }

    static void disableVanillaHandler() {
        INSTANCE.disableVanillaHandlerImpl();
    }

    void enableVanillaHandlerImpl();
    void disableVanillaHandlerImpl();
}
