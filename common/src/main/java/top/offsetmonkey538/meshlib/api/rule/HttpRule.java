package top.offsetmonkey538.meshlib.api.rule;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.function.Function;

public interface HttpRule {
    boolean matches(final FullHttpRequest request);

    /**
     * Should return the uri as if this rule's first match is the root
     * <p>
     *     {@link top.offsetmonkey538.meshlib.api.rule.rules.PathHttpRule PathHttpRule} removes the matched path so {@link top.offsetmonkey538.meshlib.api.handler.handlers.StaticDirectoryHandler StaticDirectoryHandler} can correctly find the files based on the uri
     * </p>
     *
     * @param uri the uri to modify
     */
    default  String normalizeUri(final String uri) {
        // no-op
        return uri;
    }
}
