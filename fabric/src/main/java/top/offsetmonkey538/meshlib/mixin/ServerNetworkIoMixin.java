package top.offsetmonkey538.meshlib.mixin;

import io.netty.channel.Channel;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.offsetmonkey538.meshlib.impl.ProtocolHandler;

import static top.offsetmonkey538.meshlib.MESHLib.MOD_ID;

/**
 * Mixin adding the {@link ProtocolHandler} to the minecraft netty pipeline
 */
@Mixin(targets = "net/minecraft/server/ServerNetworkIo$1")
public abstract class ServerNetworkIoMixin {

    @Inject(
            method = "initChannel",
            at = @At("TAIL")
    )
    private void meshlib$addHttpHandler(Channel channel, CallbackInfo ci) {
        channel.pipeline().addFirst(MOD_ID, new ProtocolHandler());
        System.out.println(channel.pipeline().names());
    }
}
