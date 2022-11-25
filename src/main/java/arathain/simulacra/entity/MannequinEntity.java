package arathain.simulacra.entity;

import arathain.simulacra.client.screen.MannequinScreenHandler;
import arathain.simulacra.init.SimulacraItems;
import arathain.simulacra.network.FlingMannequinPacket;
import arathain.simulacra.network.SyncStatueRotPacket;
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
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Arrays;
import java.util.function.Consumer;

import static arathain.simulacra.entity.StatueEntity.ZERO_ROT;

public class MannequinEntity extends LivingEntity {
	public static final TrackedData<EulerAngle> HEAD_ROT = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
	private static final TrackedData<Boolean> RETAIN = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<EulerAngle> BODY_ROT = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> LEFT_ARM_ROT = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
	public static final TrackedData<EulerAngle> RIGHT_ARM_ROT = DataTracker.registerData(MannequinEntity.class, TrackedDataHandlerRegistry.ROTATION);
	private float attackAnimationX;
	private float attackAnimationZ;
	private int attackAnimation;
	public long lastHitTime;

	public final SimpleInventory inventory = new SimpleInventory(4);
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
	public void setRetain(boolean bl) {
		this.dataTracker.set(RETAIN, bl);
	}
	public boolean getRetain() {
		return this.dataTracker.get(RETAIN);
	}

	public MannequinEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public Iterable<ItemStack> getItemsHand() {
		return ImmutableList.of(this.inventory.getStack(2), this.inventory.getStack(3));
	}

	public void onAttack(float attackYaw) {
		float rotation = attackYaw - (float) (this.bodyYaw / 180.0 * Math.PI);
		this.knockbackVelocity = attackYaw;
		this.attackAnimation = 40;
		this.attackAnimationX = MathHelper.cos(rotation);
		this.attackAnimationZ = MathHelper.sin(rotation);
	}


	@Override
	public Iterable<ItemStack> getArmorItems() {
		return ImmutableList.of(this.inventory.getStack(0), this.inventory.getStack(1));
	}
	@Override
	public void tick() {
		super.tick();
		if (this.world.isClient()) {
			if (this.attackAnimation <= 0)
				return;
			this.attackAnimation--;
		}
	}
	private float getAttackAnimation(float partialTicks) {
		float x = this.attackAnimation - partialTicks;
		return MathHelper.cos(x) / 2F * MathHelper.sqrt(x) / (50 - x);
	}

	public boolean hasAnimation() {
		return this.attackAnimation > 0;
	}

	public float getAnimationRotationX(float partialTicks) {
		return this.attackAnimationX * this.getAttackAnimation(partialTicks);
	}

	public float getAnimationRotationZ(float partialTicks) {
		return this.attackAnimationZ * this.getAttackAnimation(partialTicks);
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		if (slot == null)
			return ItemStack.EMPTY;

		switch (slot) {
			case HEAD -> {
				return this.inventory.getStack(0);
			}
			case CHEST -> {
				return this.inventory.getStack(1);
			}
			case MAINHAND -> {
				return this.inventory.getStack(2);
			}
			case OFFHAND -> {
				return this.inventory.getStack(3);
			}
			default -> {
				return ItemStack.EMPTY;
			}
		}
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
		if (slot == null)
			return;

		switch (slot) {
			case HEAD ->
				this.inventory.setStack(0, stack);
			case CHEST ->
				this.inventory.setStack(1, stack);
			case MAINHAND ->
				this.inventory.setStack(2, stack);
			case OFFHAND ->
				this.inventory.setStack(3, stack);
			default -> {}
		}
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
		this.dataTracker.startTracking(LEFT_ARM_ROT, ZERO_ROT);
		this.dataTracker.startTracking(RIGHT_ARM_ROT, ZERO_ROT);
		this.dataTracker.startTracking(RETAIN, false);
	}

	private void readRot(NbtCompound tag) {
		yea((soop) -> {
			NbtList yea = tag.getList(soop.getRight(), 5);
			this.dataTracker.set(soop.getLeft(), yea.isEmpty() ? ZERO_ROT : new EulerAngle(yea));
		}, new Pair<>(RIGHT_ARM_ROT, "RightArm"), new Pair<>(LEFT_ARM_ROT, "LeftArm"), new Pair<>(HEAD_ROT, "Head"), new Pair<>(BODY_ROT, "Body"));
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
		}, new Pair<>(RIGHT_ARM_ROT, "RightArm"), new Pair<>(LEFT_ARM_ROT, "LeftArm"), new Pair<>(HEAD_ROT, "Head"), new Pair<>(BODY_ROT, "Body"));

		return pose;
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
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if(player.getStackInHand(hand).isEmpty()) {
			this.openInventory(player);
			return ActionResult.CONSUME;
		}
		return super.interact(player, hand);
	}

	private void spawnBreakParticles() {
		if (this.world instanceof ServerWorld) {
			((ServerWorld)this.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.getDefaultState()), this.getX(), this.getBodyY(0.6666666666666666), this.getZ(), 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05);
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
						if(amount < 10 && !(source.getAttacker() instanceof PlayerEntity plr && plr.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AxeItem)) {
							long l = this.world.getTime();
							Vec3d pos = source.getPosition();
							this.world.sendEntityStatus(this, (byte)32);
							this.knockbackVelocity = (float) -MathHelper.atan2(pos.x - this.getX(), pos.z - this.getZ());
							FlingMannequinPacket.send(this, this.knockbackVelocity);
							this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
							this.lastHitTime = l;
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
							Vec3d pos = source.getPosition();
							this.world.sendEntityStatus(this, (byte)32);
							if(pos == null) {
								pos = this.getPos();
							}
							this.knockbackVelocity = (float) -MathHelper.atan2(pos.x - this.getX(), pos.z - this.getZ());
							FlingMannequinPacket.send(this, this.knockbackVelocity);
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

	public void kill() {
		this.remove(RemovalReason.KILLED);
		this.emitGameEvent(GameEvent.ENTITY_DIE);
	}
	public void openInventory(PlayerEntity player) {
		if (!this.world.isClient()) {;
			player.openHandledScreen(new MannequinScreenHandlerFactory());
		}
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}
	private class MannequinScreenHandlerFactory implements ExtendedScreenHandlerFactory {
		private MannequinEntity getMannequin() {
			return MannequinEntity.this;
		}

		@Override
		public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
			buf.writeVarInt(this.getMannequin().getId());
		}

		@Override
		public Text getDisplayName() {
			return this.getMannequin().getDisplayName();
		}

		@Override
		public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
			return MannequinScreenHandler.mannequinMenu(syncId, inv, this.getMannequin());
		}
	}
}
