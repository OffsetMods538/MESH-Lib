package top.offsetmonkey538.meshlib.platform.neoforge;

import top.offsetmonkey538.meshlib.platform.PlatformMain;

public final class NeoforgePlatformMain implements PlatformMain {
    public static boolean isVanillaHandlerEnabled = false;

    @Override
    public void enableVanillaHandlerImpl() {
        isVanillaHandlerEnabled = true;
    }

    @Override
    public void disableVanillaHandlerImpl() {
        isVanillaHandlerEnabled = false;
    }

    // dis on for loading the PlatformMain service
    public NeoforgePlatformMain() {

    }
}
