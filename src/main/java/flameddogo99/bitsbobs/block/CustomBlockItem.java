package flameddogo99.bitsbobs.block;

import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import xyz.nucleoid.packettweaker.PacketContext;

public class CustomBlockItem extends PolymerBlockItem {
  public CustomBlockItem(Block block, Settings settings) {
    super(block, settings);
  }
  @Override
  public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
    return Items.STRUCTURE_VOID;
  }
}
