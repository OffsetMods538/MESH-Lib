package top.offsetmonkey538.meshlib.common.api.rule;

import io.netty.handler.codec.http.FullHttpRequest;
import top.offsetmonkey538.meshlib.common.api.handler.handlers.StaticDirectoryHandler;
import top.offsetmonkey538.meshlib.common.api.rule.rules.PathHttpRule;

public interface HttpRule {
    boolean matches(final FullHttpRequest request);

    /**
     * Should return the uri as if this rule's first match is the root
     * <p>
     *     {@link PathHttpRule PathHttpRule} removes the matched path so {@link StaticDirectoryHandler StaticDirectoryHandler} can correctly find the files based on the uri
     * </p>
     *
     * @param uri the uri to modify
     */
    default  String normalizeUri(final String uri) {
        // no-op
        return uri;
    }
}
