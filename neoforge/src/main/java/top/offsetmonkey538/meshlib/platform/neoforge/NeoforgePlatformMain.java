package top.offsetmonkey538.meshlib.platform.neoforge;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import top.offsetmonkey538.meshlib.MESHLib;
import top.offsetmonkey538.meshlib.platform.PlatformMain;
import top.offsetmonkey538.monkeylib538.neoforge.impl.command.CommandRegistrationImpl;

import java.nio.file.Path;

import static top.offsetmonkey538.meshlib.MESHLib.MOD_ID;

public final class NeoforgePlatformMain implements PlatformMain {
    @Override
    public void enableVanillaHandlerImpl() {
        // TODO
    }

    @Override
    public void disableVanillaHandlerImpl() {
        // TODO
    }

    @Override
    public Path getConfigDirImpl() {
        return FMLPaths.CONFIGDIR.get().resolve(MOD_ID);
    }

    // dis on for loading the PlatformMain service
    public NeoforgePlatformMain() {

    }
}
