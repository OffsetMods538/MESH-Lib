package top.offsetmonkey538.meshlib.config;

import blue.endless.jankson.Comment;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.platform.PlatformMain;
import top.offsetmonkey538.offsetconfig538.api.config.Config;

import java.nio.file.Path;

public class MESHLibConfig implements Config {

    @Comment("Used to figure out if mesh lib should inject into vanilla or not. Required as external port may differ from that's defined in server.properties")
    public String minecraftServerExternalPort = null;
    @Comment("Port the http server will bind to")
    public String httpPort = null;


    @Override
    public @NotNull Path getFilePath() {
        return PlatformMain.getConfigDir().resolve(getId() + ".json");
    }

    @Override
    public @NotNull String getId() {
        return "main";
    }
}
