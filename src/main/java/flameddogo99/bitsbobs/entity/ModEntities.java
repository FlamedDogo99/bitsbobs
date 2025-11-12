package flameddogo99.bitsbobs.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import flameddogo99.bitsbobs.BitsBobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class ModEntities {
  public static final EntityType<Entity> SITTABLE = registerEntity(
          Identifier.of(BitsBobs.MOD_ID, "sittable_entity"),
          EntityType.Builder.create(SittableEntity::new, SpawnGroup.MISC).dimensions(0.0f, 0.0f)
                  .maxTrackingRange(10).disableSummon().makeFireImmune());
  public static void registerModEntities() {

  }
  public static <T extends Entity> EntityType<T> registerEntity(Identifier id, EntityType.Builder<T> type) {
    var built = type.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, id));
    Registry.register(Registries.ENTITY_TYPE, id, built);
    PolymerEntityUtils.registerType(built);
    return built;
  }
}
