package top.offsetmonkey538.meshlib.paper.platform;

import io.papermc.paper.network.ChannelInitializeListenerHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.java.JavaPlugin;
import top.offsetmonkey538.meshlib.common.MESHLib;
import top.offsetmonkey538.meshlib.common.impl.ProtocolHandler;
import top.offsetmonkey538.meshlib.common.platform.PlatformUtil;

public final class PlatformUtilImpl implements PlatformUtil {
    private static final Key HANDLER_KEY = Key.key("meshlib", "meshlib_vanilla_handler");

    @Override
    public void enableVanillaHandlerImpl() {
        if (ChannelInitializeListenerHolder.hasListener(HANDLER_KEY)) return;
        ChannelInitializeListenerHolder.addListener(HANDLER_KEY, channel -> channel.pipeline().addFirst(MESHLib.MOD_ID, new ProtocolHandler()));
    }

    @Override
    public void disableVanillaHandlerImpl() {
        ChannelInitializeListenerHolder.removeListener(HANDLER_KEY);
    }


    public static final class MESHLibInitializer extends JavaPlugin {
        @Override
        public void onEnable() {
            MESHLib.initialize();
        }
    }
}
