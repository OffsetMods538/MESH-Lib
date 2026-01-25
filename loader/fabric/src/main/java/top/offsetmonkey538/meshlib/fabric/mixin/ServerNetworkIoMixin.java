package top.offsetmonkey538.meshlib.fabric.mixin;

import io.netty.channel.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.offsetmonkey538.meshlib.common.impl.ProtocolHandler;
import top.offsetmonkey538.meshlib.fabric.platform.FabricPlatformMain;

import static top.offsetmonkey538.meshlib.common.MESHLib.MOD_ID;

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
        // This method is executed every time a new connection is started. Thus, I can just not add to the vanilla server when that's disabled
        if (!FabricPlatformMain.isVanillaHandlerEnabled) return;
        channel.pipeline().addFirst(MOD_ID, new ProtocolHandler());
    }
}
