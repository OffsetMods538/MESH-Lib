package top.offsetmonkey538.meshlib.common.netty;

import blue.endless.jankson.annotation.Nullable;
import com.google.common.base.Suppliers;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import top.offsetmonkey538.meshlib.common.impl.ProtocolHandler;
import top.offsetmonkey538.monkeylib538.common.api.platform.LoaderUtil;

import java.net.InetAddress;
import java.util.function.Supplier;

import static top.offsetmonkey538.meshlib.common.MESHLib.CONFIG;
import static top.offsetmonkey538.meshlib.common.MESHLib.LOGGER;
import static top.offsetmonkey538.meshlib.common.MESHLib.MOD_ID;

public final class NettyServer {
    private NettyServer() {}

    public static final Supplier<NioEventLoopGroup> SERVER_EVENT_GROUP = Suppliers.memoize(() -> new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).setUncaughtExceptionHandler((thread, cause) -> LOGGER.error("Caught unhandled exception in thread %s:", cause, thread.getName())).build()));
    public static final Supplier<EpollEventLoopGroup> SERVER_EPOLL_EVENT_GROUP = Suppliers.memoize(() -> new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).setUncaughtExceptionHandler((thread, cause) -> LOGGER.error("Caught unhandled exception in thread %s:", cause, thread.getName())).build()));

    public static @Nullable ChannelFuture channelFuture;

    public static void start() {
        final Class<? extends ServerChannel> chennelClass;
        final EventLoopGroup eventLoopGroup;
        if (Epoll.isAvailable() && LoaderUtil.isEpollEnabled()) {
            chennelClass = EpollServerSocketChannel.class;
            eventLoopGroup = SERVER_EPOLL_EVENT_GROUP.get();
            LOGGER.info("Using epoll channel type");
        } else {
            chennelClass = NioServerSocketChannel.class;
            eventLoopGroup = SERVER_EVENT_GROUP.get();
            LOGGER.info("Using nio channel type");
        }

        final ServerBootstrap bootstrap = new ServerBootstrap();
        channelFuture = bootstrap.channel(chennelClass).group(eventLoopGroup).localAddress((InetAddress) null, CONFIG.get().httpPort).childHandler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addFirst(MOD_ID, new ProtocolHandler());
            }
        }).bind().syncUninterruptibly();
    }

    public static void stop() {
        if (channelFuture == null) return;
        try {
            channelFuture.channel().close().sync();
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while stopping Netty server");
        }
    }
}
