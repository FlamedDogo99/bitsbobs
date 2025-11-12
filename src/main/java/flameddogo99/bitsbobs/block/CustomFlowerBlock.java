package flameddogo99.bitsbobs.block;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import flameddogo99.bitsbobs.BitsBobs;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class CustomFlowerBlock extends FlowerBlock implements PolymerTexturedBlock {
  private final BlockState polymerBlockState;
  public CustomFlowerBlock(String blockName, RegistryEntry<StatusEffect> stewEffect, float effectLengthInSeconds, Settings settings) {
    super(stewEffect, effectLengthInSeconds, settings);
    this.polymerBlockState = PolymerBlockResourceUtils.requestBlock(BlockModelType.PLANT_BLOCK, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/" + blockName)));
  }

  @Override
  public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
    return this.polymerBlockState;
  }
}