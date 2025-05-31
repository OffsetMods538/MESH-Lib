package top.offsetmonkey538.meshlib;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import top.offsetmonkey538.meshlib.example.ExampleMain;

@Plugin(
        id = "mesh-lib",
        name = "MeshLib",
        version = "0.0.0"
)
public class MeshLibPlugin {

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        MeshLib.initialize();

        ExampleMain.onInitialize();
    }
}
