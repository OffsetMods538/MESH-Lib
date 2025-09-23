package top.offsetmonkey538.meshlib.api.router.rule;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.function.Function;

public interface HttpRule<T> {
    String getType();
    T getData();

    boolean matches(final FullHttpRequest httpRequest);

    record HttpRuleDefinition<T>(Class<T> dataType, Function<T, HttpRule<T>> ruleInitializer) {

    }
}
