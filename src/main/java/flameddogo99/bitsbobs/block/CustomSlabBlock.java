package flameddogo99.bitsbobs.block;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import flameddogo99.bitsbobs.BitsBobs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class CustomSlabBlock extends SlabBlock implements PolymerTexturedBlock {
  private final BlockState customTopSlabState;
  private final BlockState customBottomSlabState;
  private final BlockState customTopSlabStateWaterlogged;
  private final BlockState customBottomSlabStateWaterlogged;
  public static final EnumProperty<SlabType> TYPE;
  public static final BooleanProperty WATERLOGGED;
  private final BlockState doubleSlabState;

  public CustomSlabBlock(String type, Settings settings, BlockState doubleSlabState) {
    super(settings);
    this.doubleSlabState = doubleSlabState;
    this.customTopSlabState = PolymerBlockResourceUtils.requestBlock(BlockModelType.TOP_SLAB, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/" + type + "_slab_top")));
    this.customBottomSlabState = PolymerBlockResourceUtils.requestBlock(BlockModelType.BOTTOM_SLAB, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/" + type + "_slab")));
    this.customTopSlabStateWaterlogged = PolymerBlockResourceUtils.requestBlock(BlockModelType.TOP_SLAB_WATERLOGGED, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/" + type + "_slab_top")));
    this.customBottomSlabStateWaterlogged = PolymerBlockResourceUtils.requestBlock(BlockModelType.BOTTOM_SLAB_WATERLOGGED, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/" + type + "_slab")));
    this.setDefaultState(this.getDefaultState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, false));

  }
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(TYPE, WATERLOGGED);
  }

  @Override
  public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
    SlabType slabType = state.get(TYPE);
    boolean isWaterlogged = state.get(WATERLOGGED);
    return switch(slabType) {
      case TOP -> isWaterlogged ? this.customTopSlabStateWaterlogged : this.customTopSlabState;
      case BOTTOM -> isWaterlogged ? this.customBottomSlabStateWaterlogged : this.customBottomSlabState;
      case DOUBLE -> this.doubleSlabState;
    };
  }

  static {
    TYPE = Properties.SLAB_TYPE;
    WATERLOGGED = Properties.WATERLOGGED;
  }
}
