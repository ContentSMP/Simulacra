package arathain.simulacra.mixin;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityType.class)
public class EntityTypeMixin {
	@ModifyVariable(method = "fromNbt(Lnet/minecraft/nbt/NbtCompound;)Ljava/util/Optional;", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static NbtCompound simulacra$bullshit(NbtCompound nbt) {
		if(nbt.contains("id") && nbt.getString("id").contains("mannequins:statue")) {
			nbt.remove("id");
			nbt.putString("id", "simulacra:statue");
			if(nbt.contains("Pose")) {
				NbtCompound yeag = nbt.getCompound("Pose");
				nbt.remove("Pose");
				nbt.put("rot", yeag);
			}
			if (nbt.contains("Items", NbtType.LIST)) {
				NbtList listTag = nbt.getList("Items", 10);
				for (int i = 0; i < listTag.size(); ++i) {
					NbtCompound compoundTag2 = listTag.getCompound(i);
					int j = compoundTag2.getByte("Slot") & 255;
					compoundTag2.remove("Slot");
					compoundTag2.putByte("Slot", (byte) mapSlot(j));
				}
			}
		}
		if(nbt.contains("id") && nbt.getString("id").contains("mannequins:mannequin")) {
			nbt.remove("id");
			nbt.putString("id", "simulacra:mannequin");
			if(nbt.contains("Pose")) {
				NbtCompound yeag = nbt.getCompound("Pose");
				nbt.remove("Pose");
				nbt.put("rot", yeag);
			}
		}
		return nbt;
	}
	@Unique
	private static int mapSlot(int i) {
		switch(i) {
			case 0 -> {
				return 4;
			}
			case 1 -> {
				return 3;
			}
			case 3 -> {
				return 5;
			}
			default -> {
				return 0;
			}
		}
	}
}
