package arathain.simulacra.client;

import arathain.simulacra.entity.MannequinEntity;
import arathain.simulacra.mixin.BipedEntityModelAccessor;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Pair;
import net.minecraft.util.math.EulerAngle;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

public class MannequinModel extends BipedEntityModel<MannequinEntity> {
	private final ModelPart base;
	private final ModelPart spring;
	public MannequinModel(ModelPart root) {
		super(root);
		this.base = root.getChild("mannequin").getChild("base");
		this.spring = base.getChild("spring");
		BipedEntityModelAccessor access = (BipedEntityModelAccessor) this;
		access.setBody(root.getChild("mannequin").getChild("base").getChild("spring").getChild("body"));
		access.setHead(root.getChild("mannequin").getChild("base").getChild("spring").getChild("body").getChild("head"));
		access.setRightArm(root.getChild("mannequin").getChild("base").getChild("spring").getChild("body").getChild("right_arm"));
		access.setLeftArm(root.getChild("mannequin").getChild("base").getChild("spring").getChild("body").getChild("left_arm"));
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData meshdefinition = new ModelData();
		ModelPartData root = meshdefinition.getRoot();

		root.addChild("head", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("body", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.NONE);

		ModelPartData mannequin = root.addChild("mannequin", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.0F, 0.0F));

		ModelPartData base = mannequin.addChild("base", ModelPartBuilder.create().uv(17, 53).cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));
		base.addChild("cursed", ModelPartBuilder.create(), ModelTransform.NONE);
		ModelPartData spring = base.addChild("spring", ModelPartBuilder.create().uv(0, 53).cuboid(-2.0F, -7.0F, -2.0F, 4.0F, 7.0F, 4.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData body = spring.addChild("body", ModelPartBuilder.create().uv(4, 17).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

		ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(33, 0).cuboid(-2.0F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0F)), ModelTransform.pivot(-5.0F, -11.0F, 0.0F));

		ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create().uv(33, 0).mirrored().cuboid(-1.0F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0F)).mirrored(false), ModelTransform.pivot(5.0F, -11.0F, 0.0F));

		return TexturedModelData.of(meshdefinition, 64, 64);
	}
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return Collections.emptyList();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.base);
	}

	public static TexturedModelData getArmourTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.NONE);
		modelPartData.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.NONE);
		ModelPartData mannequin = modelPartData.addChild("mannequin", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.0F, 0.0F));

		ModelPartData base = mannequin.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

		ModelPartData spring = base.addChild("spring", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		spring.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(0.0F, 12.0F, 0.0F));
		spring.getChild("body").addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(0.0F, -12.0F, 0.0F));
		spring.getChild("body").getChild("head").addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		spring.getChild("body").addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, -9.5F, 0.0F));
		spring.getChild("body").addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, -9.5F, 0.0F));
		return TexturedModelData.of(modelData, 64, 32);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
//		if(!this.base.containsPart("cursed")) {
//			matrices.push();
//			this.base.rotate(matrices);
//			this.spring.rotate(matrices);
//			this.body.rotate(matrices);
//			this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
//			matrices.pop();
//		}
		this.getBodyParts().forEach((bodyPart) -> {
			bodyPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		});
	}

	public void rotate(MatrixStack matrices) {
		this.base.rotate(matrices);
		this.spring.rotate(matrices);
		this.body.rotate(matrices);
	}
	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		rotate(matrices);
		super.setArmAngle(arm, matrices);
	}

	@Override
	public void setAttributes(BipedEntityModel<MannequinEntity> model) {
		super.setAttributes(model);
		if(model instanceof MannequinModel man) {
			man.spring.copyTransform(this.spring);
		}
	}

	@Override
	public void setAngles(MannequinEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		yea((soop) -> soop.getRight().setAngles(((float) Math.PI / 180F) * soop.getLeft().getWrappedPitch(), ((float) Math.PI / 180F) * soop.getLeft().getWrappedYaw(), ((float) Math.PI / 180F) * soop.getLeft().getWrappedRoll()), new Pair<>(entity.getRightArmRot(), this.rightArm), new Pair<>(entity.getLeftArmRot(), this.leftArm),new Pair<>(entity.getHeadRot(), this.head), new Pair<>(entity.getBodyRot(), this.body));
		float bodyPitch = this.body.pitch;
		float bodyRoll = this.body.roll;
		if (entity.hasAnimation()) {
			MinecraftClient mc = MinecraftClient.getInstance();
			this.spring.pitch = -(entity.getAnimationRotationX(mc.getTickDelta()) * 30F) * ((float) Math.PI / 180F);
			this.spring.roll = (entity.getAnimationRotationZ(mc.getTickDelta()) * 30F) * ((float) Math.PI / 180F);
			this.body.pitch += -(entity.getAnimationRotationX(mc.getTickDelta()) * 30F) * ((float) Math.PI / 180F);
			this.body.roll += (entity.getAnimationRotationZ(mc.getTickDelta()) * 30F) * ((float) Math.PI / 180F);
		} else {
			this.spring.pitch = 0;
			this.spring.roll = 0;
			this.body.pitch = bodyPitch;
			this.body.roll = bodyRoll;
		}
	}

	@SafeVarargs
	private static void yea(Consumer<Pair<EulerAngle, ModelPart>> soup, Pair<EulerAngle, ModelPart>... iterable) {
		Arrays.stream(iterable).toList().forEach(soup);
	}
}
