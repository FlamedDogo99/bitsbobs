package flameddogo99.bitsbobs.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import flameddogo99.bitsbobs.BitsBobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static net.minecraft.entity.decoration.ArmorStandEntity.ARMOR_STAND_FLAGS;

public class SittableEntity extends Entity implements PolymerEntity {
  private static final EntityAttributeInstance MAX_HEALTH_NULL = new EntityAttributeInstance(
          EntityAttributes.MAX_HEALTH, discard -> {
  });
  private static final Collection<EntityAttributeInstance> MAX_HEALTH_NULL_SINGLE = Collections
          .singleton(MAX_HEALTH_NULL);
  static {
    MAX_HEALTH_NULL.setBaseValue(0D);
  }

  public SittableEntity(EntityType<?> type, World world) {
    super(type, world);
    this.setInvisible(true);
    this.setNoGravity(true);
  }


  public SittableEntity(World world, double x, double y, double z) {
    this(ModEntities.SITTABLE, world);
    this.setPosition(x, y, z);
    this.resetPosition();
  }

  @Override
  public EntityType<?> getPolymerEntityType(PacketContext packetContext) {
    return EntityType.ARMOR_STAND;
  }


  @Override
  public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
    data.add(new DataTracker.Entry<>(ARMOR_STAND_FLAGS, (byte) 16).toSerialized());
    if (player != null) {
      player.networkHandler.sendPacket(new EntityAttributesS2CPacket(getId(), MAX_HEALTH_NULL_SINGLE));
    }
  }

  @Override
  protected void initDataTracker(DataTracker.Builder builder) {

  }

  @Override
  protected void readCustomData(ReadView view) {
    final int version = view.getInt(BitsBobs.TAG_VERSION, -1);
    if(version == BitsBobs.RUNTIME_VERSION) {
      this.setPos(this.getX(), this.getY(), this.getZ());
      this.resetPosition();
    }
  }

  @Override
  protected void writeCustomData(WriteView view) {
    view.putInt(BitsBobs.TAG_VERSION, BitsBobs.RUNTIME_VERSION);
  }

  /** Only save if being ridden. */
  @Override
  public boolean shouldSave() {
    var reason = getRemovalReason();
    if (reason != null && !reason.shouldSave()) {
      return false;
    }
    return hasPassengers();
  }

  @Override
  public void removeAllPassengers() {
    super.removeAllPassengers();
    discard();
  }

  @Override
  protected void removePassenger(Entity passenger) {
    super.removePassenger(passenger);
    discard();
  }

  @Override
  public void tick() {
    // There's absolutely no reason for this entity to even move.
    super.tick();
    var passenger = getFirstPassenger();
    if (passenger == null || isDiscardable()) {
      discard();
      return;
    }
    setYaw(passenger.getYaw());
  }

  @Override
  public boolean damage(ServerWorld world, DamageSource source, float amount) {
    if(isAlwaysInvulnerableTo(source)) {
      return false;
    }
    this.remove(RemovalReason.KILLED);
    return true;
  }

  public boolean isDiscardable() {
    return this.getEntityWorld().getBlockState(BlockPos.ofFloored(getEntityPos())).isAir();
  }
}