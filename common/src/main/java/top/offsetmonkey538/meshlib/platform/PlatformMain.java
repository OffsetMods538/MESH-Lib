package top.offsetmonkey538.meshlib.platform;

import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;

import static top.offsetmonkey538.meshlib.MESHLib.load;

@ApiStatus.Internal
public interface PlatformMain {

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated // Only for use in this class
    PlatformMain INSTANCE = load(PlatformMain.class);

    static void enableVanillaHandler() {
        INSTANCE.enableVanillaHandlerImpl();
    }

    static void disableVanillaHandler() {
        INSTANCE.disableVanillaHandlerImpl();
    }

    static Path getConfigDir() {
        return INSTANCE.getConfigDirImpl();
    }

    void enableVanillaHandlerImpl();
    void disableVanillaHandlerImpl();
    Path getConfigDirImpl();
}
