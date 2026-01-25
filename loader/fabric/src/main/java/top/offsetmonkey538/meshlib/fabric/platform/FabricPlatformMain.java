package top.offsetmonkey538.meshlib.fabric.platform;

import net.fabricmc.api.DedicatedServerModInitializer;
import top.offsetmonkey538.meshlib.common.MESHLib;
import top.offsetmonkey538.meshlib.common.platform.PlatformMain;

public final class FabricPlatformMain implements PlatformMain, DedicatedServerModInitializer {
    public static boolean isVanillaHandlerEnabled = false;

    @Override
    public void enableVanillaHandlerImpl() {
        isVanillaHandlerEnabled = true;
    }

    @Override
    public void disableVanillaHandlerImpl() {
        isVanillaHandlerEnabled = false;
    }

    @Override
    public void onInitializeServer() {
        MESHLib.initialize();
    }
}
