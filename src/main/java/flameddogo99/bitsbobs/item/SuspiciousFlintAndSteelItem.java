package flameddogo99.bitsbobs.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import flameddogo99.bitsbobs.block.ModBlocks;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import xyz.nucleoid.packettweaker.PacketContext;

public class SuspiciousFlintAndSteelItem extends Item implements PolymerItem {
  public SuspiciousFlintAndSteelItem(Settings settings) {
    super(settings);
  }
  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    BlockPos blockPos = context.getBlockPos();
    PlayerEntity playerEntity = context.getPlayer();
    World world = context.getWorld();
    BlockPos blockPos2 = blockPos.offset(context.getSide());
    if (AbstractFireBlock.canPlaceAt(world, blockPos2, context.getHorizontalPlayerFacing())) {
      world.playSound(playerEntity, blockPos2, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
      BlockState blockState2 = ModBlocks.BLOOD_FIRE.getDefaultState();
      world.setBlockState(blockPos2, blockState2, Block.NOTIFY_ALL_AND_REDRAW);
      world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
      ItemStack itemStack = context.getStack();
      if (playerEntity instanceof ServerPlayerEntity) {
        Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos2, itemStack);
        itemStack.damage(1, playerEntity, context.getHand().getEquipmentSlot());
      }
      return ActionResult.SUCCESS;
    }
    return ActionResult.FAIL;
  }
  @Override
  public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
    return Items.FLINT_AND_STEEL;
  }
}
