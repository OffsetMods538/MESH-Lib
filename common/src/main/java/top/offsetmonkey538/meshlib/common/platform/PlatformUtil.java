package top.offsetmonkey538.meshlib.common.platform;

import org.jetbrains.annotations.ApiStatus;

import static top.offsetmonkey538.meshlib.common.MESHLib.load;

@ApiStatus.Internal
public interface PlatformUtil {

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated // Only for use in this class
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
