package flameddogo99.bitsbobs;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import flameddogo99.bitsbobs.block.ModBlocks;
import flameddogo99.bitsbobs.entity.ModEntities;
import flameddogo99.bitsbobs.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitsBobs implements ModInitializer {
	public static final String MOD_ID = "bitsbobs";
  public static final String TAG_VERSION = "bitsbobs:runtimeVersion";
  public static final int RUNTIME_VERSION = 1;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
    PolymerResourcePackUtils.markAsRequired();
    PolymerResourcePackUtils.addModAssets(MOD_ID);
    ModItems.registerModItems();
    ModBlocks.registerModBlocks();
    ModEntities.registerModEntities();
	}
}