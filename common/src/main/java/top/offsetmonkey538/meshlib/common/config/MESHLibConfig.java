package top.offsetmonkey538.meshlib.common.config;

import blue.endless.jankson.Comment;
import org.jspecify.annotations.Nullable;
import top.offsetmonkey538.monkeylib538.common.api.platform.LoaderUtil;
import top.offsetmonkey538.offsetutils538.api.config.Config;

import java.nio.file.Path;

import static top.offsetmonkey538.meshlib.common.MESHLib.MOD_ID;

public final class MESHLibConfig implements Config {
    @Comment("Used to figure out if mesh lib should inject into vanilla or not. Required as external port may differ from that's defined in server.properties")
    public @Nullable Integer minecraftServerExternalPort = null;
    @Comment("Port the http server will bind to")
    public @Nullable Integer httpPort = null;
    @Comment("Port the http server will be accessed from externally. Used by for example Git Pack Manager when generating the download url. The HTTP server will still be hosted on the 'httpPort'. Useful when running the server behind some sort of proxy like docker, nginx, traefik, cloudflare tunnel, etc.")
    public @Nullable Integer exposedPort = null;


    @Override
    public Path getConfigDirPath() {
        return LoaderUtil.getConfigDir();
    }

    @Override
    public String getId() {
        return MOD_ID + "/main";
    }
}
