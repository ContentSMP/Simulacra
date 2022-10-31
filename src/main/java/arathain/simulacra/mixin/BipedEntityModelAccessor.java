package arathain.simulacra.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BipedEntityModel.class)
public interface BipedEntityModelAccessor {

	@Mutable
	@Accessor
	void setHead(ModelPart part);

	@Mutable
	@Accessor
	void setHat(ModelPart part);

	@Mutable
	@Accessor
	void setLeftArm(ModelPart part);

	@Mutable
	@Accessor
	void setRightArm(ModelPart part);
	@Mutable
	@Accessor
	void setLeftLeg(ModelPart part);

	@Mutable
	@Accessor
	void setRightLeg(ModelPart part);
}
