package top.offsetmonkey538.meshlib.api.router;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;

public record HttpRouter(@NotNull HttpRule rule, @NotNull HttpHandler handler) {
}
