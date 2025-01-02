package top.offsetmonkey538.meshlib;

import io.papermc.paper.network.ChannelInitializeListenerHolder;
import net.kyori.adventure.key.Key;
import top.offsetmonkey538.meshlib.impl.ProtocolHandler;

public final class MeshLib {
    private MeshLib() {

    }

    public static void initialize() {
        ChannelInitializeListenerHolder.addListener(Key.key("meshlib", "meshlib"), channel -> channel.pipeline().addFirst(MESHLib.MOD_ID, new ProtocolHandler()));
    }
}
