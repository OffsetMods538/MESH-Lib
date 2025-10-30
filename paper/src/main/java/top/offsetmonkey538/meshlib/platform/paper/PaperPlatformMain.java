package top.offsetmonkey538.meshlib.platform.paper;

import io.papermc.paper.network.ChannelInitializeListenerHolder;
import net.kyori.adventure.key.Key;
import top.offsetmonkey538.meshlib.MESHLib;
import top.offsetmonkey538.meshlib.impl.ProtocolHandler;
import top.offsetmonkey538.meshlib.platform.PlatformMain;

import java.nio.file.Path;

public final class PaperPlatformMain implements PlatformMain {
    private static final Key HANDLER_KEY = Key.key("meshlib", "meshlib_vanilla_handler");

    private static MeshLibPlugin plugin;

    @Override
    public void enableVanillaHandlerImpl() {
        if (ChannelInitializeListenerHolder.hasListener(HANDLER_KEY)) return;
        ChannelInitializeListenerHolder.addListener(HANDLER_KEY, channel -> channel.pipeline().addFirst(MESHLib.MOD_ID, new ProtocolHandler()));
    }

    @Override
    public void disableVanillaHandlerImpl() {
        ChannelInitializeListenerHolder.removeListener(HANDLER_KEY);
    }

    public static void setPlugin(MeshLibPlugin plugin) {
        PaperPlatformMain.plugin = plugin;
    }

    public static MeshLibPlugin getPlugin() {
        return plugin;
    }
}
