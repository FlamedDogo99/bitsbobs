package flameddogo99.bitsbobs.item;

import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import flameddogo99.bitsbobs.BitsBobs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.function.Function;

public class ModItems {
  public static final Item SADDLE_ON_A_STICK = registerItem("saddle_on_a_stick", new SaddleOnAStickItem(
          new Item.Settings().maxCount(1).maxDamage(35).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(BitsBobs.MOD_ID, "saddle_on_a_stick")))));
  public static final Item SUSPICIOUS_FLINT_AND_STEEL = registerItem("suspicious_flint_and_steel", new SuspiciousFlintAndSteelItem(
          new Item.Settings().maxDamage(64).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(BitsBobs.MOD_ID, "suspicious_flint_and_steel")))));
  private static Item registerItem(String name, Item item) {
    return Registry.register(Registries.ITEM, Identifier.of(BitsBobs.MOD_ID, name), item);
  }
  private static Item registerPotion(Item potionItem, Potion potion) {
    return PotionContentsComponent.createStack(potionItem, Registries.POTION.getEntry(potion)).getItem();
  }
  public static void registerModItems() {
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
      entries.add(SADDLE_ON_A_STICK);
      entries.add(SUSPICIOUS_FLINT_AND_STEEL);
    });
  }
}
