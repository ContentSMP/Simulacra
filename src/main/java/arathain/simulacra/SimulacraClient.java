package arathain.simulacra;

import arathain.simulacra.client.StatueModel;
import arathain.simulacra.client.StatueRenderer;
import arathain.simulacra.init.SimulacraEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class SimulacraClient implements ClientModInitializer {
	public static final EntityModelLayer STATUE_MODEL_LAYER = new EntityModelLayer(new Identifier(Simulacra.MODID, "statue"), "main");
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRendererRegistry.register(SimulacraEntities.STATUE, StatueRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(STATUE_MODEL_LAYER, () -> StatueModel.getTexturedModelData(Dilation.NONE));
	}
}
