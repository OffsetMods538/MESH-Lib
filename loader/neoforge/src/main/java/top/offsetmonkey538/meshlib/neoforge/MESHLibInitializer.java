package top.offsetmonkey538.meshlib.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import top.offsetmonkey538.meshlib.common.MESHLib;

@Mod("meshlib")
public final class MESHLibInitializer {
    public MESHLibInitializer(IEventBus modEventBus, ModContainer modContainer) {
        MESHLib.initialize();
    }
}
