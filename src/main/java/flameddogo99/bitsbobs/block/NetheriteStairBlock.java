package flameddogo99.bitsbobs.block;

import com.mojang.authlib.properties.Property;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import flameddogo99.bitsbobs.BitsBobs;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.HashMap;

public class NetheriteStairBlock extends StairsBlock implements PolymerTexturedBlock {
  public HashMap<BlockState, BlockState> stateMap;
  public NetheriteStairBlock(BlockState baseBlockState, Settings settings) {
    super(baseBlockState, settings);
    this.stateMap = new HashMap<>();
  }

  @Override
  public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
    if(this.stateMap.containsKey(blockState)) {
      return this.stateMap.get(blockState);
    } else {
      String resourceName = "block/netherite_stairs" + getStairShape(blockState.get(SHAPE));
      int y = getYRot(blockState.get(FACING), blockState.get(HALF), blockState.get(SHAPE));
      int x = getXRot(blockState.get(HALF));
      BlockState stairState = PolymerBlockResourceUtils.requestBlock(BlockModelType.getStairs(
              blockState.get(FACING), blockState.get(HALF), blockState.get(SHAPE), blockState.get(WATERLOGGED)
      ), PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, resourceName), x, y, true));
      stateMap.put(blockState, stairState);
      return stairState;
    }
  }
  private String getStairShape(StairShape stairShape) {
    return switch(stairShape) {
      case INNER_LEFT, INNER_RIGHT -> "_inner";
      case OUTER_LEFT, OUTER_RIGHT -> "_outer";
      default -> "";
    };
  }
  private int getYRot(Direction direction, BlockHalf blockHalf, StairShape stairShape) {
    int yBase = switch(direction) {
      case EAST -> 0;
      case SOUTH -> 90;
      case WEST -> 180;
      case NORTH -> 270;
      default -> throw new RuntimeException("Unexpected Stair Type " + direction);
    };
    if(blockHalf == BlockHalf.BOTTOM && (stairShape == StairShape.INNER_LEFT || stairShape == StairShape.OUTER_LEFT)) {
      yBase += yBase == 0 ? 270 : -90;
    }
    if(blockHalf == BlockHalf.TOP && (stairShape == StairShape.INNER_RIGHT || stairShape == StairShape.OUTER_RIGHT)) {
      yBase += yBase == 270 ? -270 : 90;
    }
    return yBase;
  }
  private int getXRot(BlockHalf blockHalf) {
    return blockHalf == BlockHalf.BOTTOM ? 0 : 180;
  }
}
