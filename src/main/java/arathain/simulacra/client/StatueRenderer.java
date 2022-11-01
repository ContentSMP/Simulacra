package arathain.simulacra.client;

import arathain.simulacra.Simulacra;
import arathain.simulacra.SimulacraClient;
import arathain.simulacra.entity.StatueEntity;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class StatueRenderer extends LivingEntityRenderer<StatueEntity, StatueModel> {
	private static final Identifier TEXTURE = new Identifier(Simulacra.MODID, "textures/entity/temp.png");
	public StatueRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new StatueModel(ctx.getPart(SimulacraClient.STATUE_MODEL_LAYER)), 0.4f);
		this.addFeature(new ArmorFeatureRenderer<>(this, new StatueModel(ctx.getPart(SimulacraClient.STATUE_INNER_ARMOR)), new StatueModel(ctx.getPart(SimulacraClient.STATUE_OUTER_ARMOR))));
		this.addFeature(new HeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
		this.addFeature(new ElytraFeatureRenderer<>(this, ctx.getModelLoader()));
		this.addFeature(new HeadFeatureRenderer<>(this, ctx.getModelLoader(), ctx.getHeldItemRenderer()));
	}

	@Override
	protected boolean hasLabel(StatueEntity livingEntity) {
		return super.hasLabel(livingEntity) && (livingEntity.shouldRenderName() || livingEntity.hasCustomName() && livingEntity == this.dispatcher.targetedEntity);
	}

	@Override
	public Identifier getTexture(StatueEntity entity) {
		return TEXTURE;
	}
}
