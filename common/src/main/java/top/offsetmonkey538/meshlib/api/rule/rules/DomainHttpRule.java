package top.offsetmonkey538.meshlib.api.rule.rules;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;

public class DomainHttpRule implements HttpRule<DomainHttpRule.Data> {
    private final String value; // i.e. map.example.com

    public DomainHttpRule(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "domain";
    }

    @Override
    public Data getData() {
        return new Data(value);
    }

    @Override
    public boolean matches(FullHttpRequest request) {
        String host = request.headers().get(HttpHeaderNames.HOST);
        if (host == null) return false;

        final int portIndex = host.indexOf(':');
        if (portIndex != -1) host = host.substring(0, portIndex);

        return host.equals(value);
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
