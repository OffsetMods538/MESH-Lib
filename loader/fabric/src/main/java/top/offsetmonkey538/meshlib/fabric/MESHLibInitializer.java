package top.offsetmonkey538.meshlib.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import top.offsetmonkey538.meshlib.common.MESHLib;

public final class MESHLibInitializer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        MESHLib.initialize();
    }
}
