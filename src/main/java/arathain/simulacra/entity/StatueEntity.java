package arathain.simulacra.entity;

import arathain.simulacra.client.screen.StatueScreenHandler;
import arathain.simulacra.init.SimulacraItems;
import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Arrays;
import java.util.function.Consumer;

public class StatueEntity extends LivingEntity {
	public static final TrackedData<EulerAngle> HEAD_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	private static final TrackedData<Boolean> RETAIN = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<EulerAngle> BODY_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> LEFT_ARM_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> RIGHT_ARM_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> LEFT_LEG_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> RIGHT_LEG_ROT = DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final EulerAngle ZERO_ROT = new EulerAngle(0.0F, 0.0F, 0.0F);


	public long lastHitTime;

	public final SimpleInventory inventory = new SimpleInventory(6);

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

	public void setHeadRot(EulerAngle ang) {
		this.dataTracker.set(HEAD_ROT, ang);
	}
	public void setBodyRot(EulerAngle ang) {
		this.dataTracker.set(BODY_ROT, ang);
	}
	public void setRightArmRot(EulerAngle ang) {
		this.dataTracker.set(RIGHT_ARM_ROT, ang);
	}
	public void setLeftArmRot(EulerAngle ang) {
		this.dataTracker.set(LEFT_ARM_ROT, ang);
	}
	public void setRightLegRot(EulerAngle ang) {
		this.dataTracker.set(RIGHT_LEG_ROT, ang);
	}
	public void setLeftLegRot(EulerAngle ang) {
		this.dataTracker.set(LEFT_LEG_ROT, ang);
	}

	public void setRetain(boolean bl) {
		this.dataTracker.set(RETAIN, bl);
	}
	public boolean getRetain() {
		return this.dataTracker.get(RETAIN);
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
	public void openInventory(PlayerEntity player) {
		if (!this.world.isClient()) {;
			player.openHandledScreen(new StatueScreenHandlerFactory());
		}
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if(player.getStackInHand(hand).isEmpty()) {
			this.openInventory(player);
			return ActionResult.CONSUME;
		}
		return super.interact(player, hand);
	}

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(HEAD_ROT, ZERO_ROT);
		this.dataTracker.startTracking(BODY_ROT, ZERO_ROT);
		this.dataTracker.startTracking(LEFT_LEG_ROT, ZERO_ROT);
		this.dataTracker.startTracking(RIGHT_LEG_ROT, ZERO_ROT);
		this.dataTracker.startTracking(LEFT_ARM_ROT, ZERO_ROT);
		this.dataTracker.startTracking(RIGHT_ARM_ROT, ZERO_ROT);
		this.dataTracker.startTracking(RETAIN, false);
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
		this.setRetain(nbt.getBoolean("retain"));

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

	private void spawnBreakParticles() {
		if (this.world instanceof ServerWorld) {
			((ServerWorld)this.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState()), this.getX(), this.getBodyY(0.6666666666666666), this.getZ(), 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05);
		}

	}

	private void playBreakSound() {
		this.world.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, this.getSoundCategory(), 1.0F, 1.0F);
	}
	private void onBreak(DamageSource damageSource) {
		this.playBreakSound();
		this.drop(damageSource);

		int i;
		ItemStack itemStack;
		for(i = 0; i < this.inventory.size(); ++i) {
			itemStack = this.inventory.getStack(i);
			if (!itemStack.isEmpty()) {
				Block.dropStack(this.world, this.getBlockPos().up(), itemStack);
				this.inventory.setStack(i, ItemStack.EMPTY);
			}
		}

	}

	public void kill() {
		this.remove(RemovalReason.KILLED);
		this.emitGameEvent(GameEvent.ENTITY_DIE);
	}

	public boolean damage(DamageSource source, float amount) {
		if (!this.world.isClient && !this.isRemoved()) {
			if (DamageSource.OUT_OF_WORLD.equals(source)) {
				this.kill();
				return false;
			} else if (!this.isInvulnerableTo(source)) {
				if (source.isExplosive()) {
					this.onBreak(source);
					this.kill();
					return false;
				} else {
					if(source.getAttacker() instanceof LivingEntity) {
						if(amount < 10 && !(source.getAttacker() instanceof PlayerEntity plr && plr.getStackInHand(Hand.MAIN_HAND).getItem() instanceof PickaxeItem)) {
							return false;
						}
					}
					if (source.isSourceCreativePlayer()) {
						this.playBreakSound();
						this.spawnBreakParticles();
						this.kill();
					} else {
						long l = this.world.getTime();
						if (l - this.lastHitTime > 5L) {
							this.world.sendEntityStatus(this, (byte)32);
							this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
							this.lastHitTime = l;
						} else {
							this.breakAndDropItem(source);
							this.spawnBreakParticles();
							this.kill();
						}

					}
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.put("rot", this.writeRot());
		nbt.putBoolean("retain", this.getRetain());
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
	private void breakAndDropItem(DamageSource damageSource) {
		ItemStack stack = new ItemStack(SimulacraItems.STATUE);
		if(this.getRetain()) {
			NbtCompound nbt = new NbtCompound();
			this.writeNbt(nbt);
			stack.getOrCreateNbt().put("rots", nbt);
		}
		Block.dropStack(this.world, this.getBlockPos(), stack);
		this.onBreak(damageSource);
	}
	private class StatueScreenHandlerFactory implements ExtendedScreenHandlerFactory {
		private StatueEntity getStatue() {
			return StatueEntity.this;
		}

		@Override
		public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
			buf.writeVarInt(this.getStatue().getId());
		}

		@Override
		public Text getDisplayName() {
			return this.getStatue().getDisplayName();
		}

		@Override
		public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			return StatueScreenHandler.statueMenu(syncId, inv, this.getStatue());
		}
	}
}
