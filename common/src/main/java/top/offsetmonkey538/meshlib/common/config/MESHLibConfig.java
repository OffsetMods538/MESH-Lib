package top.offsetmonkey538.meshlib.common.config;

import blue.endless.jankson.Comment;
import top.offsetmonkey538.monkeylib538.common.api.platform.LoaderUtil;
import top.offsetmonkey538.offsetutils538.api.config.Config;

import java.nio.file.Path;

import static top.offsetmonkey538.meshlib.common.MESHLib.MOD_ID;

public final class MESHLibConfig implements Config {
    @Comment("Used to figure out if mesh lib should inject into vanilla or not. Required as external port may differ from that's defined in server.properties")
    public String minecraftServerExternalPort = null;
    @Comment("Port the http server will bind to")
    public String httpPort = null;


    @Override
    public Path getConfigDirPath() {
        return LoaderUtil.getConfigDir();
    }

    @Override
    public String getId() {
        return MOD_ID + "/main";
    }
}
