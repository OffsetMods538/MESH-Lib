package top.offsetmonkey538.meshlib.fabric.platform;

import top.offsetmonkey538.meshlib.common.platform.PlatformUtil;

public final class PlatformUtilImpl implements PlatformUtil {
    public static boolean isVanillaHandlerEnabled = false;

    @Override
    public void enableVanillaHandlerImpl() {
        isVanillaHandlerEnabled = true;
    }

    @Override
    public void disableVanillaHandlerImpl() {
        isVanillaHandlerEnabled = false;
    }
}
