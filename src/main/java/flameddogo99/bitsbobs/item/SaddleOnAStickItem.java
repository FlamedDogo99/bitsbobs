package flameddogo99.bitsbobs.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import xyz.nucleoid.packettweaker.PacketContext;

public class SaddleOnAStickItem extends Item implements PolymerItem {

  public SaddleOnAStickItem(Settings settings) {
    super(settings);
  }
  @Override
  public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
    user.startRiding(entity, true, true);
    stack.damage(1, user, hand.getEquipmentSlot());
    return super.useOnEntity(stack, user, entity, hand);
  }

  @Override
  public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
    return Items.TRIAL_KEY;
  }
}
