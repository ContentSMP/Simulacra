package arathain.simulacra.client;

import arathain.simulacra.Simulacra;
import arathain.simulacra.SimulacraClient;
import arathain.simulacra.entity.StatueEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class StatueRenderer extends LivingEntityRenderer<StatueEntity, StatueModel> {
	private static final Identifier TEXTURE = new Identifier(Simulacra.MODID, "textures/entity/temp.png");
	public StatueRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new StatueModel(ctx.getPart(SimulacraClient.STATUE_MODEL_LAYER)), 0.4f);
	}

	@Override
	public void render(StatueEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Identifier getTexture(StatueEntity entity) {
		return TEXTURE;
	}
}
