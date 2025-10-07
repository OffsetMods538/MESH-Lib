package top.offsetmonkey538.meshlib.platform.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import top.offsetmonkey538.meshlib.MESHLib;
import top.offsetmonkey538.meshlib.platform.PlatformMain;

import java.nio.file.Path;

import static top.offsetmonkey538.meshlib.MESHLib.MOD_ID;

@Mod("mesh_lib")
public final class NeoforgeInitializer {

    public NeoforgeInitializer(IEventBus modEventBus, ModContainer modContainer) {
        MESHLib.initialize();
    }
}
