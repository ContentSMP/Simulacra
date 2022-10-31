package arathain.simulacra.entity;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.function.Consumer;

public class StatueEntity extends LivingEntity {
	public static final TrackedData<EulerAngle> HEAD_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> BODY_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> LEFT_ARM_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> RIGHT_ARM_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> LEFT_LEG_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> RIGHT_LEG_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	private static final EulerAngle ZERO_ROT = new EulerAngle(0.0F, 0.0F, 0.0F);
	private static final EulerAngle TEST_ROT = new EulerAngle(0.0F, 0.0F, 5.0F);

	private final SimpleInventory inventory = new SimpleInventory(6);

	public StatueEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	public EulerAngle getHeadRot() {
		return this.dataTracker.get(HEAD_ROT);
	}
	public EulerAngle getBodyRot() {
		return this.dataTracker.get(BODY_ROT);
	}
	public EulerAngle getRightArmRot() {
		return this.dataTracker.get(RIGHT_ARM_ROT);
	}
	public EulerAngle getLeftArmRot() {
		return this.dataTracker.get(LEFT_ARM_ROT);
	}
	public EulerAngle getRightLegRot() {
		return this.dataTracker.get(RIGHT_LEG_ROT);
	}
	public EulerAngle getLeftLegRot() {
		return this.dataTracker.get(LEFT_LEG_ROT);
	}

	@Override
	public Iterable<ItemStack> getItemsHand() {
		return ImmutableList.of(this.inventory.getStack(0), this.inventory.getStack(5));
	}


	@Override
	public Iterable<ItemStack> getArmorItems() {
		return ImmutableList.of(this.inventory.getStack(4), this.inventory.getStack(3), this.inventory.getStack(2), this.inventory.getStack(1));
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return this.inventory.getStack(slot.getArmorStandSlotId());
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
		this.inventory.setStack(slot.getArmorStandSlotId(), stack);
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		this.dataTracker.set(HEAD_ROT, TEST_ROT);
		return super.interact(player, hand);
	}

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(HEAD_ROT, TEST_ROT);
		this.dataTracker.startTracking(BODY_ROT, TEST_ROT);
		this.dataTracker.startTracking(LEFT_LEG_ROT, ZERO_ROT);
		this.dataTracker.startTracking(RIGHT_LEG_ROT, ZERO_ROT);
		this.dataTracker.startTracking(LEFT_ARM_ROT, ZERO_ROT);
		this.dataTracker.startTracking(RIGHT_ARM_ROT, TEST_ROT);
	}

	private void readRot(NbtCompound tag) {
		yea((soop) -> {
			NbtList yea = tag.getList(soop.getRight(), 5);
			this.dataTracker.set(soop.getLeft(), yea.isEmpty() ? ZERO_ROT : new EulerAngle(yea));
		}, new Pair<>(RIGHT_ARM_ROT, "RightArm"), new Pair<>(LEFT_ARM_ROT, "LeftArm"),new Pair<>(RIGHT_LEG_ROT, "RightLeg"), new Pair<>(LEFT_ARM_ROT, "LeftLeg"), new Pair<>(HEAD_ROT, "Head"), new Pair<>(BODY_ROT, "Body"));
	}
	@SafeVarargs
	private static void yea(Consumer<Pair<TrackedData<EulerAngle>, String>> soup, Pair<TrackedData<EulerAngle>, String>... iterable) {
		Arrays.stream(iterable).toList().forEach(soup);
	}

	private NbtCompound writeRot() {
		NbtCompound pose = new NbtCompound();
		yea((soop) -> {
			if(!ZERO_ROT.equals(this.dataTracker.get(soop.getLeft()))) {
				pose.put(soop.getRight(), this.dataTracker.get(soop.getLeft()).toNbt());
			}
		}, new Pair<>(RIGHT_ARM_ROT, "RightArm"), new Pair<>(LEFT_ARM_ROT, "LeftArm"),new Pair<>(RIGHT_LEG_ROT, "RightLeg"), new Pair<>(LEFT_ARM_ROT, "LeftLeg"), new Pair<>(HEAD_ROT, "Head"), new Pair<>(BODY_ROT, "Body"));

		return pose;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		this.readRot(nbt.getCompound("rot"));

		if (nbt.contains("Items", NbtType.LIST)) {
			NbtList listTag = nbt.getList("Items", 10);
			for (int i = 0; i < listTag.size(); ++i) {
				NbtCompound compoundTag2 = listTag.getCompound(i);
				int j = compoundTag2.getByte("Slot") & 255;
				if (j < this.inventory.size()) {
					this.inventory.setStack(j, ItemStack.fromNbt(compoundTag2));
				}
			}
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.put("rot", this.writeRot());
		NbtList list = new NbtList();
		for (int i = 0; i < this.inventory.size(); ++i) {
			ItemStack itemStack = this.inventory.getStack(i);
			if (!itemStack.isEmpty()) {
				NbtCompound compoundTag2 = new NbtCompound();
				compoundTag2.putByte("Slot", (byte) i);
				itemStack.writeNbt(compoundTag2);
				list.add(compoundTag2);
			}
		}
		nbt.put("Items", list);
	}
}
