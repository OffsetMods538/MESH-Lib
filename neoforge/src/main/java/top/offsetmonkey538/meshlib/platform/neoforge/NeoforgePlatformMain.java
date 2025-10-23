package top.offsetmonkey538.meshlib.platform.neoforge;

import net.neoforged.fml.loading.FMLPaths;
import top.offsetmonkey538.meshlib.platform.PlatformMain;

import java.nio.file.Path;

import static top.offsetmonkey538.meshlib.MESHLib.MOD_ID;

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

    @Override
    public Path getConfigDirImpl() {
        return FMLPaths.CONFIGDIR.get().resolve(MOD_ID);
    }

    // dis on for loading the PlatformMain service
    public NeoforgePlatformMain() {

    }
}
