package top.offsetmonkey538.meshlib.platform.paper;

import org.bukkit.plugin.java.JavaPlugin;
import top.offsetmonkey538.meshlib.MESHLib;

public final class MeshLibPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperPlatformMain.setPlugin(this);
        MESHLib.initialize();
    }
}
