package top.offsetmonkey538.meshlib;

import org.bukkit.plugin.java.JavaPlugin;
import top.offsetmonkey538.meshlib.example.ExampleMain;

public class MeshLibPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        MeshLib.initialize();

        ExampleMain.onInitialize();
    }
}
