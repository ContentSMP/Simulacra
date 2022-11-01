package arathain.simulacra.client;

import arathain.simulacra.entity.StatueEntity;
import arathain.simulacra.mixin.BipedEntityModelAccessor;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Pair;
import net.minecraft.util.math.EulerAngle;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

public class StatueModel extends BipedEntityModel<StatueEntity> {
	public StatueModel(ModelPart root) {
		super(root);
		BipedEntityModelAccessor access = (BipedEntityModelAccessor) this;
		access.setRightArm(root.getChild("body").getChild("right_arm"));
		access.setLeftArm(root.getChild("body").getChild("left_arm"));
		access.setHead(root.getChild("body").getChild("head"));
		access.setHat(root.getChild("body").getChild("head").getChild("hat"));
	}
	public static TexturedModelData getTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(0.0F, 12.0F, 0.0F));
		modelPartData.getChild("body").addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(0.0F, -12.0F, 0.0F));
		modelPartData.getChild("body").getChild("head").addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.getChild("body").addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, -9.5F, 0.0F));
		modelPartData.getChild("body").addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, -9.5F, 0.0F));
		modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

		modelPartData.getChild("body").getChild("left_arm").addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0F, 0F, 0.0F));
		modelPartData.getChild("body").getChild("right_arm").addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0F, 0F, 0.0F));

		modelPartData.getChild("left_leg").addChild("left_pants", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0F, 0F, 0.0F));
		modelPartData.getChild("right_leg").addChild("right_pants", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0F, 0F, 0.0F));
		modelPartData.getChild("body").addChild("jacket", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.251F)), ModelTransform.NONE);

		return TexturedModelData.of(modelData, 64, 64);
	}
	public static TexturedModelData getArmourTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(0.0F, 12.0F, 0.0F));
		modelPartData.getChild("body").addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(0.0F, -12.0F, 0.0F));
		modelPartData.getChild("body").getChild("head").addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.getChild("body").addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, -9.5F, 0.0F));
		modelPartData.getChild("body").addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, -9.5F, 0.0F));
		modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

		return TexturedModelData.of(modelData, 64, 32);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		this.body.rotate(matrices);
		super.setArmAngle(arm, matrices);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return Collections.emptyList();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.body, this.rightLeg, this.leftLeg);
	}

	@Override
	public void setAngles(StatueEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		yea((soop) -> soop.getRight().setAngles(((float) Math.PI / 180F) * soop.getLeft().getWrappedPitch(), ((float) Math.PI / 180F) * soop.getLeft().getWrappedYaw(), ((float) Math.PI / 180F) * soop.getLeft().getWrappedRoll()), new Pair<>(entity.getRightArmRot(), this.rightArm), new Pair<>(entity.getLeftArmRot(), this.leftArm),new Pair<>(entity.getRightLegRot(), this.rightLeg), new Pair<>(entity.getLeftLegRot(), this.leftLeg), new Pair<>(entity.getHeadRot(), this.head), new Pair<>(entity.getBodyRot(), this.body));
	}
	@SafeVarargs
	private static void yea(Consumer<Pair<EulerAngle, ModelPart>> soup, Pair<EulerAngle, ModelPart>... iterable) {
		Arrays.stream(iterable).toList().forEach(soup);
	}
}
