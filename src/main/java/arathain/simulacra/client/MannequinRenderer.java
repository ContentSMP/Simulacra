package arathain.simulacra.client;

import arathain.simulacra.Simulacra;
import arathain.simulacra.SimulacraClient;
import arathain.simulacra.entity.MannequinEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Unique;

public class MannequinRenderer extends LivingEntityRenderer<MannequinEntity, MannequinModel> {
	private static final Identifier TEXTURE = new Identifier(Simulacra.MODID, "textures/entity/mannequin.png");
	public MannequinRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new MannequinModel(ctx.getPart(SimulacraClient.MANNEQUIN_MODEL_LAYER)), 0.4f);
		this.addFeature(new ArmorFeatureRenderer<>(this, new MannequinModel(ctx.getPart(SimulacraClient.MANNEQUIN_INNER_ARMOUR)), new MannequinModel(ctx.getPart(SimulacraClient.MANNEQUIN_OUTER_ARMOUR))));
		this.addFeature(new HeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
		this.addFeature(new ElytraFeatureRenderer<>(this, ctx.getModelLoader()));
		this.addFeature(new HeadFeatureRenderer<>(this, ctx.getModelLoader(), ctx.getHeldItemRenderer()));
	}

	@Override
	protected boolean hasLabel(MannequinEntity livingEntity) {
		return super.hasLabel(livingEntity) && (livingEntity.shouldRenderName() || livingEntity.hasCustomName() && livingEntity == this.dispatcher.targetedEntity);
	}
	@Override
	public Identifier getTexture(MannequinEntity entity) {
		return TEXTURE;
	}
}

