package flameddogo99.bitsbobs.block;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import flameddogo99.bitsbobs.BitsBobs;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import xyz.nucleoid.packettweaker.PacketContext;


public class BloodFireBlock extends AbstractFireBlock implements PolymerTexturedBlock {
  private final BlockState polymerState;
  public static final MapCodec<BloodFireBlock> CODEC = BloodFireBlock.createCodec(BloodFireBlock::new);


  public BloodFireBlock(Settings settings) {
    super(settings, 3.0f);
    this.polymerState = PolymerBlockResourceUtils.requestBlock(BlockModelType.TRIPWIRE_BLOCK_FLAT, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/blood_fire")));
  }


  @Override
  public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
    return polymerState;
  }

  @Override
  protected MapCodec<? extends AbstractFireBlock> getCodec() {
    return CODEC;
  }

  @Override
  protected boolean isFlammable(BlockState state) {
    return true;
  }
  @Override
  protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
    if (oldState.isOf(state.getBlock())) {
      return;
    }
    if (!state.canPlaceAt(world, pos)) {
      world.removeBlock(pos, false);
    }
  }
  @Override
  protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    BlockPos blockPos = pos.down();
    return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP);
  }

  @Override
  protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
    if (this.canPlaceAt(state, world, pos)) {
      return this.getDefaultState();
    }
    return Blocks.AIR.getDefaultState();
  }
}
