package arathain.simulacra.entity;

import arathain.simulacra.init.SimulacraEntities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class MannequinItem extends Item {
	public MannequinItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		Direction yeah = context.getSide();
		MannequinEntity statue = new MannequinEntity(SimulacraEntities.MANNEQUIN, context.getWorld());
		if(context.getStack().getOrCreateNbt().contains("rots")) {
			statue.readCustomDataFromNbt(context.getStack().getOrCreateNbt().getCompound("rots"));
		}
		statue.setPosition(Vec3d.ofBottomCenter(context.getBlockPos().offset(yeah)));
		if(yeah == Direction.DOWN || !context.getWorld().doesNotIntersectEntities(statue))
			return ActionResult.FAIL;
		if(context.getWorld() instanceof ServerWorld server) {
			float yaw = context.getPlayer().getYaw() + 180f;
			yaw += 45/2f;
			yaw /= 45;
			int truYaw = (int) yaw;
			truYaw *= 45;
			statue.setYaw(truYaw);
			server.spawnEntityAndPassengers(statue);
		}
		context.getStack().decrement(1);
		return ActionResult.success(context.getWorld().isClient);
	}
}

