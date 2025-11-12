package flameddogo99.bitsbobs.block;

import flameddogo99.bitsbobs.BitsBobs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class ModBlocks {
  public static final Block DIRT_SLAB = registerBlock("dirt_slab",
          new CustomSlabBlock("dirt", Block.Settings.copy(Blocks.DIRT).sounds(BlockSoundGroup.GRAVEL).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(BitsBobs.MOD_ID, "dirt_slab"))), Blocks.DIRT.getDefaultState()));
  public static final Block NETHER_BRICK_FENCE_GATE = registerBlock("nether_brick_fence_gate",
          new NetherBrickFenceGateBlock(Block.Settings.copy(Blocks.NETHER_BRICK_FENCE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(BitsBobs.MOD_ID, "nether_brick_fence_gate")))));
  public static final Block BLOOD_FIRE = registerBlockNoItem("blood_fire",
          new BloodFireBlock(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).replaceable().noCollision().breakInstantly().luminance(state -> 15).sounds(BlockSoundGroup.WOOL).pistonBehavior(PistonBehavior.DESTROY).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(BitsBobs.MOD_ID, "blood_fire")))));

  public static final Block SEAT_RED = registerSeat("red", DyeColor.RED);
  public static final Block SEAT_ORANGE = registerSeat("orange", DyeColor.ORANGE);
  public static final Block SEAT_YELLOW = registerSeat("yellow", DyeColor.YELLOW);
  public static final Block SEAT_LIME = registerSeat("lime", DyeColor.LIME);
  public static final Block SEAT_GREEN = registerSeat("green", DyeColor.GREEN);
  public static final Block SEAT_CYAN = registerSeat("cyan", DyeColor.CYAN);
  public static final Block SEAT_LIGHT_BLUE = registerSeat("light_blue", DyeColor.LIGHT_BLUE);
  public static final Block SEAT_BLUE = registerSeat("blue", DyeColor.BLUE);
  public static final Block SEAT_PURPLE = registerSeat("purple", DyeColor.PURPLE);
  public static final Block SEAT_MAGENTA = registerSeat("magenta", DyeColor.MAGENTA);
  public static final Block SEAT_PINK = registerSeat("pink", DyeColor.PINK);
  public static final Block SEAT_WHITE = registerSeat("white", DyeColor.WHITE);
  public static final Block SEAT_LIGHT_GRAY = registerSeat("light_gray", DyeColor.LIGHT_GRAY);
  public static final Block SEAT_GRAY = registerSeat("gray", DyeColor.GRAY);
  public static final Block SEAT_BLACK = registerSeat("black", DyeColor.BLACK);
  public static final Block SEAT_BROWN = registerSeat("brown", DyeColor.BROWN);

  public static final Block BLUE_ROSE = registerBlock("blue_rose",
          new CustomFlowerBlock("blue_rose", StatusEffects.LEVITATION, 10, AbstractBlock.Settings.copy(Blocks.BLUE_ORCHID).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(BitsBobs.MOD_ID, "blue_rose")))));
  public static final Block ROSE = registerBlock("rose",
          new CustomFlowerBlock("rose",StatusEffects.HEALTH_BOOST, 10, AbstractBlock.Settings.copy(Blocks.POPPY).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(BitsBobs.MOD_ID, "rose")))));

  public static final Block NETHERITE_STAIRS = registerBlock("netherite_stairs", new NetheriteStairBlock(Blocks.NETHERITE_BLOCK.getDefaultState(), Block.Settings.copy(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(BitsBobs.MOD_ID, "netherite_stairs")))));

  private static Block registerSeat(String color, DyeColor dyeColor) {
    return registerBlock("seat_" + color,
            new SeatBlock(color, AbstractBlock.Settings.create().mapColor(state -> dyeColor.getMapColor()).sounds(BlockSoundGroup.WOOD).strength(0.2f).nonOpaque().burnable().pistonBehavior(PistonBehavior.DESTROY).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(BitsBobs.MOD_ID, "seat_" + color)))));
  }
  private static void registerBlockItem(String name, Block block) {
    Registry.register(Registries.ITEM, Identifier.of(BitsBobs.MOD_ID, name),
            new CustomBlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(BitsBobs.MOD_ID, name)))));
  }
  private static Block registerBlockNoItem(String name, Block block) {
    return Registry.register(Registries.BLOCK, Identifier.of(BitsBobs.MOD_ID, name), block);
  }
  private static Block registerBlock(String name, Block block) {
    registerBlockItem(name, block);
    return Registry.register(Registries.BLOCK, Identifier.of(BitsBobs.MOD_ID, name), block);
  }
  public static void registerModBlocks() {
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
      entries.add(ModBlocks.DIRT_SLAB);
      entries.add(ModBlocks.NETHER_BRICK_FENCE_GATE);
      entries.add(ModBlocks.SEAT_RED);
      entries.add(ModBlocks.SEAT_ORANGE);
      entries.add(ModBlocks.SEAT_YELLOW);
      entries.add(ModBlocks.SEAT_LIME);
      entries.add(ModBlocks.SEAT_GREEN);
      entries.add(ModBlocks.SEAT_CYAN);
      entries.add(ModBlocks.SEAT_LIGHT_BLUE);
      entries.add(ModBlocks.SEAT_BLUE);
      entries.add(ModBlocks.SEAT_PURPLE);
      entries.add(ModBlocks.SEAT_MAGENTA);
      entries.add(ModBlocks.SEAT_PINK);
      entries.add(ModBlocks.SEAT_WHITE);
      entries.add(ModBlocks.SEAT_LIGHT_GRAY);
      entries.add(ModBlocks.SEAT_GRAY);
      entries.add(ModBlocks.SEAT_BLACK);
      entries.add(ModBlocks.SEAT_BROWN);
      entries.add(ModBlocks.ROSE);
      entries.add(ModBlocks.BLUE_ROSE);
      entries.add(ModBlocks.NETHERITE_STAIRS);


    });

  }
}
