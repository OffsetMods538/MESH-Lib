package top.offsetmonkey538.meshlib.api.rule.rules;

import io.netty.handler.codec.http.FullHttpRequest;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;

public class PathHttpRule implements HttpRule<PathHttpRule.Data> {
    private final String value; // i.e. /map -> example.com/map

    public PathHttpRule(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "path";
    }

    @Override
    public Data getData() {
        return new Data(value);
    }

    @Override
    public boolean matches(FullHttpRequest request) {
        return request.uri().startsWith(value);
    }

    @Override
    public String normalizeUri(String uri) {
        return uri.replaceFirst(value, "");
    }

    public static final class Data {
        @SuppressWarnings("FieldMayBeFinal") // Think it needs to be non-final cause jankson
        public String value;

        @SuppressWarnings("unused") // Public no-args used by jankson i think
        public Data() {

        }

        public Data(String value) {
            this.value = value;
        }
    }
}
