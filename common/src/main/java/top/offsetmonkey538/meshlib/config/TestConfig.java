package top.offsetmonkey538.meshlib.config;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.example.SimpleHttpHandler;
import top.offsetmonkey538.meshlib.api.rule.rules.DomainHttpRule;
import top.offsetmonkey538.meshlib.platform.PlatformMain;
import top.offsetmonkey538.offsetconfig538.api.config.Config;

import java.nio.file.Path;

public class TestConfig implements Config {

    public HttpRule<?> rule = new DomainHttpRule("map.example.com");
    public HttpHandler target = new SimpleHttpHandler("Goodbye, World!");


    @Override
    public @NotNull Path getFilePath() {
        return PlatformMain.getConfigDir().resolve(getId() + ".json");
    }

    @Override
    public @NotNull String getId() {
        return "test";
    }
}
