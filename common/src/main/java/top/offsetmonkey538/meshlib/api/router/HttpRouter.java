package top.offsetmonkey538.meshlib.api.router;

import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.HttpHandler;
import top.offsetmonkey538.meshlib.api.router.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.router.target.HttpTarget;

public record HttpRouter(@NotNull HttpRule<?> rule, @NotNull HttpHandler<?> handler) {
}
