package top.offsetmonkey538.meshlib.common.api.rule.rules;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.common.api.rule.HttpRuleTypeRegistry;
import top.offsetmonkey538.offsetutils538.api.annotation.Internal;

/**
 * @param domain i.e. map.example.com
 */
public record DomainHttpRule(String domain) implements HttpRule {

    @Override
    public boolean matches(FullHttpRequest request) {
        String host = request.headers().get(HttpHeaderNames.HOST);
        if (host == null) return false;

        final int portIndex = host.indexOf(':');
        if (portIndex != -1) host = host.substring(0, portIndex);

        return host.equals(domain);
    }

    @Internal
    public static void register(final HttpRuleTypeRegistry registry) {
        registry.register("domain", Data.class, DomainHttpRule.class, rule -> new Data(rule.domain), data -> new DomainHttpRule(data.domain));
    }

    @Internal
    private static final class Data {
        private String domain = "";

        @SuppressWarnings("unused")
        // Pretty sure this public no-args needs to exist cause jankson wants to create instances
        public Data() {

        }

        public Data(final String domain) {
            this.domain = domain;
        }
    }
}
