package top.offsetmonkey538.meshlib;

import io.papermc.paper.network.ChannelInitializeListenerHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.java.JavaPlugin;
import top.offsetmonkey538.meshlib.example.ExampleMain;
import top.offsetmonkey538.meshlib.impl.ProtocolHandler;

public class MeshLibPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ChannelInitializeListenerHolder.addListener(Key.key("meshlib", "meshlib"), channel -> channel.pipeline().addFirst(MESHLib.MOD_ID, new ProtocolHandler()));

        ExampleMain.onInitialize();
    }
}
