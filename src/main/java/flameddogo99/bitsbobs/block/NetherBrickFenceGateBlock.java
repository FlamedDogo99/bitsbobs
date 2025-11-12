package flameddogo99.bitsbobs.block;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import flameddogo99.bitsbobs.BitsBobs;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import xyz.nucleoid.packettweaker.PacketContext;

public class NetherBrickFenceGateBlock extends FenceGateBlock implements PolymerTexturedBlock {
  private final BlockState NORTH_SOUTH_INWALL_OPEN_GATE;
  private final BlockState EAST_WEST_INWALL_OPEN_GATE;
  private final BlockState NORTH_SOUTH_OPEN_GATE;
  private final BlockState EAST_WEST_OPEN_GATE;
  private final BlockState NORTH_SOUTH_INWALL_GATE;
  private final BlockState EAST_WEST_INWALL_GATE;
  private final BlockState NORTH_SOUTH_GATE;
  private final BlockState EAST_WEST_GATE;

  public NetherBrickFenceGateBlock(Settings settings) {
    super(WoodType.OAK, settings);
    this.NORTH_SOUTH_INWALL_OPEN_GATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.NORTH_SOUTH_INWALL_OPEN_GATE, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/nether_brick_fence_gate_wall_open"), 0, 0, true));
    this.EAST_WEST_INWALL_OPEN_GATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.EAST_WEST_INWALL_OPEN_GATE, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/nether_brick_fence_gate_wall_open"), 0, 270, true));
    this.NORTH_SOUTH_OPEN_GATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.NORTH_SOUTH_OPEN_GATE, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/nether_brick_fence_gate_open"), 0, 0, true));
    this.EAST_WEST_OPEN_GATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.EAST_WEST_OPEN_GATE, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/nether_brick_fence_gate_open"), 0, 270, true));
    this.NORTH_SOUTH_INWALL_GATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.NORTH_SOUTH_INWALL_GATE, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/nether_brick_fence_gate_wall"), 0, 0, true));
    this.EAST_WEST_INWALL_GATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.EAST_WEST_INWALL_GATE, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/nether_brick_fence_gate_wall"), 0, 270, true));
    this.NORTH_SOUTH_GATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.NORTH_SOUTH_GATE, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/nether_brick_fence_gate"), 0, 0, true));
    this.EAST_WEST_GATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.EAST_WEST_GATE, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/nether_brick_fence_gate"), 0, 270, true));
  }

  private BlockState getFenceState(BlockState state) {
    boolean open = state.get(OPEN);
    boolean inwall = state.get(IN_WALL);
    Direction.Axis axis = state.get(FACING).getAxis();
    if (open) {
      if (inwall) {
        return switch (axis) {
          case Z -> NORTH_SOUTH_INWALL_OPEN_GATE;
          case X -> EAST_WEST_INWALL_OPEN_GATE;
          default -> throw new IllegalArgumentException("Only horizontal axis are supported!");
        };
      }
      return switch (axis) {
        case Z -> NORTH_SOUTH_OPEN_GATE;
        case X -> EAST_WEST_OPEN_GATE;
        default -> throw new IllegalArgumentException("Only horizontal axis are supported!");
      };
    }

    if (inwall) {
      return switch (axis) {
        case Z -> NORTH_SOUTH_INWALL_GATE;
        case X -> EAST_WEST_INWALL_GATE;
        default -> throw new IllegalArgumentException("Only horizontal axis are supported!");
      };
    }
    return switch (axis) {
      case Z -> NORTH_SOUTH_GATE;
      case X -> EAST_WEST_GATE;
      default -> throw new IllegalArgumentException("Only horizontal axis are supported!");
    };

  }

  @Override
  public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
    return getFenceState(blockState);
  }
}
