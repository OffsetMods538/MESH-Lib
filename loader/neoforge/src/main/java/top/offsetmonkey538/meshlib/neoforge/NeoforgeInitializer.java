package top.offsetmonkey538.meshlib.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import top.offsetmonkey538.meshlib.common.MESHLib;

@Mod("mesh_lib")
public final class NeoforgeInitializer {

    public NeoforgeInitializer(IEventBus modEventBus, ModContainer modContainer) {
        MESHLib.initialize();
    }
}
