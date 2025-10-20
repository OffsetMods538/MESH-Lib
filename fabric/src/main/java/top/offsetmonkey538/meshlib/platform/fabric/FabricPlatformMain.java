package top.offsetmonkey538.meshlib.platform.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import top.offsetmonkey538.meshlib.MESHLib;
import top.offsetmonkey538.meshlib.platform.PlatformMain;

import java.nio.file.Path;

import static top.offsetmonkey538.meshlib.MESHLib.MOD_ID;

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
    public Path getConfigDirImpl() {
        return FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);
    }

    @Override
    public void onInitializeServer() {
        MESHLib.initialize();
    }
}
