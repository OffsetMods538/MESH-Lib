package top.offsetmonkey538.meshlib.api.handler.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.api.handler.HttpHandler;
import top.offsetmonkey538.meshlib.api.rule.HttpRule;
import top.offsetmonkey538.meshlib.api.util.HttpResponseUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Locale;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static top.offsetmonkey538.meshlib.MESHLib.LOGGER;
import static top.offsetmonkey538.meshlib.api.util.HttpResponseUtil.*;


public class StaticDirectoryHandler implements HttpHandler {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public final Path baseDir;
    public final boolean allowDirectoryList;

    public StaticDirectoryHandler(final Path baseDir, final boolean allowDirectoryList) {
        this.baseDir = baseDir.normalize().toAbsolutePath();
        this.allowDirectoryList = allowDirectoryList;
    }

    @Override
    public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request, @NotNull HttpRule<?> rule) throws Exception {
        final String rawPath = new URI(rule.normalizeUri(request.uri())).getPath();
        final Path requestedPath;
        try {
            requestedPath = baseDir.resolve(rawPath.startsWith("/") ? rawPath.substring(1) : rawPath).normalize();
        } catch (InvalidPathException e) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, e);
            return;
        }

        if (!requestedPath.startsWith(baseDir)) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        if (!Files.exists(requestedPath)) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        if (!Files.isDirectory(requestedPath)) {
            sendFile(ctx, request, requestedPath);
            return;
        }

        // At this point we know it's a directory and that it exists
        if (!request.uri().endsWith("/")) {
            sendPermanentRedirect(ctx, request.uri() + "/");
            return;
        }

        if (allowDirectoryList) {
            sendDirectoryListing(ctx, request, rawPath, requestedPath);
            return;
        }

        // Try serving an index.html file
        sendFile(ctx, request, requestedPath.resolve("index.html"));
    }

    private static void sendDirectoryListing(ChannelHandlerContext ctx, FullHttpRequest request, String uriPath, Path directory) throws IOException {
        final boolean isKeepAlive = HttpUtil.isKeepAlive(request);

        final ByteBuf byteBuf = Unpooled.copiedBuffer(renderDirectoryListing(uriPath, directory), CharsetUtil.UTF_8);
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, byteBuf);

        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, byteBuf.readableBytes());
        response.headers().set(CONNECTION, isKeepAlive ? KEEP_ALIVE : CLOSE);


        ctx.write(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        final ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!isKeepAlive) lastContentFuture.addListener(ChannelFutureListener.CLOSE);
    }

    private static StringBuilder renderDirectoryListing(String uriPath, Path directory) throws IOException {
        final StringBuilder result = new StringBuilder();


        result.append("<!DOCTYPE html><html lang=\"en\"><head><title>Index of ").append(uriPath).append("</title>");
        result.append("<style>table{width:100%;border-collapse:collapse;border:2px solid #111}th{text-align:left}thead th.index{background:silver;font-size:1.2em;padding:8px}th.label{background-color:#dedede;border:1px solid #111;padding:6px 8px}td{padding:6px 8px;border-bottom:1px solid #ccc;verical-align:middle}tr:nth-child(odd){background-color:#efefef}tr:nth-child(even){background-color:#e2e2e2}a{color:#0078d7;text-decoration:none}a:hover{text-decoration:underline}tfoot th.provided{background:silver;border:1px solid #111;text-align:center;padding:8px;color:#2d2d2d;font-size:.8em}.back-icon::before{content:\"\uD83D\uDD19 \"}.dir-icon::before{content:\"\uD83D\uDCC1 \"}.file-icon::before{content:\"\uD83D\uDCC4 \"}</style></head>");
        result.append("<body><table><thead><tr><th class=\"index\" colspan=\"3\">Index of ");
        result.append(uriPath);
        result.append("</th></tr><tr><th class=\"label\">Name</th><th class=\"label\">Last Modified</th><th class=\"label\">Size</th></tr></thead><tbody>");

        if (!"/".equals(uriPath)) {
            result.append("<tr><td><a class=\"back-icon\" href=\"../\">Parent Directory</a></td><td>-</td><td>-</td></tr>");
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            stream.forEach(path -> {
                if (!Files.exists(path) || (!Files.isDirectory(path) && !Files.isRegularFile(path))) {
                    return;
                }

                String name = path.getFileName().toString();
                String icon = "file-icon";
                if (Files.isDirectory(path)) {
                    name += "/";
                    icon = "dir-icon";
                }
                String modifiedTime = "-";
                try {
                    modifiedTime = DATE_FORMATTER.format(Files.getLastModifiedTime(path).toInstant().atZone(ZoneId.of("UTC"))) + " UTC";
                } catch (IOException e) {
                    LOGGER.error("Failed to get modification time for file '%s'!", e, path);
                }

                result.append("<tr><td><a class=\"").append(icon).append("\" href=\"").append(name).append("\">").append(name).append("</a></td><td>").append(modifiedTime).append("</td><td>");
                formatFileSize(result, path);
                result.append("</td></tr>");
            });
        }

        result.append("</tbody><tfoot><tr><th class=\"provided\" colspan=\"3\">Provided by <a href=\"https://modrinth.com/plugin/mesh-lib\">MESH Lib</a></th></tr></tfoot></table></body></html>");

        return result;
    }

    private static void formatFileSize(StringBuilder builder, Path path) {
        if (Files.isDirectory(path)) {
            builder.append("-");
            return;
        }

        final long sizeBytes;
        try {
            sizeBytes = Files.size(path);
        } catch (IOException e) {
            LOGGER.error("Failed to get size for file '%s'!", e, path);
            builder.append("-");
            return;
        }

        // https://stackoverflow.com/a/3758880
        long absB = sizeBytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(sizeBytes);
        if (absB < 1024) {
            builder.append(sizeBytes).append(" B");
            return;
        }

        long value = absB;
        final CharacterIterator charIterator = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            charIterator.next();
        }
        value *= Long.signum(sizeBytes);
        builder.append("%.1f %ciB".formatted(value / 1024.0, charIterator.current()));
    }

    // todo: does this need to be mutable for jankson to do its magic?
    public record Data(Path baseDir, boolean allowDirectoryList) {

    }
}
