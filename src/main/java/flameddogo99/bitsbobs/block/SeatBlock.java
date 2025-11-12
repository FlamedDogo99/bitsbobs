package flameddogo99.bitsbobs.block;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import flameddogo99.bitsbobs.BitsBobs;
import flameddogo99.bitsbobs.entity.SittableEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

public class SeatBlock extends Block implements PolymerTexturedBlock {
  private final BlockState polymerState;
  public static final BooleanProperty OCCUPIED = Properties.OCCUPIED;
  private static final VoxelShape SHAPE = Block.createColumnShape(16.0, 0.0, 8.0);

  public SeatBlock(String color, Settings settings) {
    super(settings);
    this.setDefaultState(this.getDefaultState().with(OCCUPIED, false));
    this.polymerState = PolymerBlockResourceUtils.requestBlock(BlockModelType.SCULK_SENSOR_BLOCK, PolymerBlockModel.of(Identifier.of(BitsBobs.MOD_ID, "block/seat_" + color)));
  }
  @Override
  public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
    return polymerState;
  }
  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(OCCUPIED);
  }

  @Override
  protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE;
  }

  @Override
  public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
    super.onLandedUpon(world, state, pos, entity, fallDistance * 0.5);
  }

  @Override
  protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    if (world.isClient()) {
      return ActionResult.SUCCESS_SERVER;
    }
    if(state.get(OCCUPIED)) {
      player.sendMessage(Text.translatable("block.minecraft.bed.occupied"), true);
      return ActionResult.SUCCESS_SERVER;
    }
    SittableEntity sittable = new SittableEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);

    if (sittable.isDiscardable()) {
      sittable.discard();
      return ActionResult.SUCCESS_SERVER;
    }
    if (!world.spawnEntity(sittable)) {
      sittable.discard();
      return ActionResult.SUCCESS_SERVER;
    }
    player.startRiding(sittable);
    return ActionResult.SUCCESS_SERVER;
  }


  @Override
  public void onEntityLand(BlockView world, Entity entity) {
    if (entity.bypassesLandingEffects()) {
      super.onEntityLand(world, entity);
    } else {
      this.bounceEntity(entity);
    }
  }
  private void bounceEntity(Entity entity) {
    Vec3d vec3d = entity.getVelocity();
    if (vec3d.y < 0.0) {
      double d = entity instanceof LivingEntity ? 1.0 : 0.8;
      Vec3d velocity = new Vec3d(vec3d.x, -vec3d.y * (double)0.66f * d, vec3d.z);
      entity.setVelocity(velocity);
      entity.velocityModified = true;
    }
  }
}


