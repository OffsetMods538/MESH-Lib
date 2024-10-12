package top.offsetmonkey538.meshlib;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MESHLib implements ModInitializer {
	public static final String MOD_ID = "mesh-lib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
