package top.offsetmonkey538.meshlib.platform.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import top.offsetmonkey538.meshlib.MESHLib;
import top.offsetmonkey538.meshlib.platform.PlatformMain;

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
