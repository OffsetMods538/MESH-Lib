package top.offsetmonkey538.meshlib.common.api.rule.rules;

import io.netty.handler.codec.http.FullHttpRequest;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRuleTypeRegistry;

/**
 * @param path i.e. /map -> example.com/map
 */
public record PathHttpRule(String path) implements HttpRule {

    public PathHttpRule(final String path) {
        this.path = path.startsWith("/") ? path : "/" + path;
    }

    @Override
    public boolean matches(FullHttpRequest request) {
        return request.uri().startsWith(path);
    }

    @Override
    public String normalizeUri(String uri) {
        return uri.replaceFirst(path, "");
    }

    @ApiStatus.Internal
    public static void register(final HttpRuleTypeRegistry registry) {
        registry.register("path", Data.class, PathHttpRule.class, rule -> new Data(rule.path), data -> new PathHttpRule(data.path));
    }

    @ApiStatus.Internal
    private static final class Data {
        private String path;

        @SuppressWarnings("unused")
        // Pretty sure this public no-args needs to exist cause jankson wants to create instances
        public Data() {

        }

        public Data(final String path) {
            this.path = path;
        }
    }
}
