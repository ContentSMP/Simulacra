package arathain.simulacra;

import arathain.simulacra.client.MannequinModel;
import arathain.simulacra.client.MannequinRenderer;
import arathain.simulacra.client.StatueModel;
import arathain.simulacra.client.StatueRenderer;
import arathain.simulacra.client.screen.MannequinScreen;
import arathain.simulacra.client.screen.StatueScreen;
import arathain.simulacra.init.SimulacraEntities;
import arathain.simulacra.init.SimulacraScreenHandlers;
import arathain.simulacra.network.FlingMannequinPacket;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import static arathain.simulacra.Simulacra.MODID;

public class SimulacraClient implements ClientModInitializer {
	public static final EntityModelLayer MANNEQUIN_MODEL_LAYER = new EntityModelLayer(new Identifier(MODID, "mannequin"), "main");
	public static final EntityModelLayer MANNEQUIN_INNER_ARMOUR = new EntityModelLayer(new Identifier(MODID, "mannequin_inner"), "main");
	public static final EntityModelLayer MANNEQUIN_OUTER_ARMOUR = new EntityModelLayer(new Identifier(MODID, "mannequin_outer"), "main");
	public static final EntityModelLayer STATUE_INNER_ARMOUR = new EntityModelLayer(new Identifier(Simulacra.MODID, "statue_inner"), "main");
	public static final EntityModelLayer STATUE_OUTER_ARMOUR = new EntityModelLayer(new Identifier(Simulacra.MODID, "statue_outer"), "main");
	public static final EntityModelLayer STATUE_MODEL_LAYER = new EntityModelLayer(new Identifier(Simulacra.MODID, "statue"), "main");
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRendererRegistry.register(SimulacraEntities.STATUE, StatueRenderer::new);
		EntityRendererRegistry.register(SimulacraEntities.MANNEQUIN, MannequinRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(STATUE_INNER_ARMOUR, () -> StatueModel.getArmourTexturedModelData(new Dilation(0.5f)));
		EntityModelLayerRegistry.registerModelLayer(STATUE_OUTER_ARMOUR, () -> StatueModel.getArmourTexturedModelData(new Dilation(1f)));
		EntityModelLayerRegistry.registerModelLayer(MANNEQUIN_INNER_ARMOUR, () -> MannequinModel.getArmourTexturedModelData(new Dilation(0.5f)));
		EntityModelLayerRegistry.registerModelLayer(MANNEQUIN_OUTER_ARMOUR, () -> MannequinModel.getArmourTexturedModelData(new Dilation(1f)));
		EntityModelLayerRegistry.registerModelLayer(STATUE_MODEL_LAYER, () -> StatueModel.getTexturedModelData(Dilation.NONE));
		EntityModelLayerRegistry.registerModelLayer(MANNEQUIN_MODEL_LAYER, MannequinModel::getTexturedModelData);
		HandledScreens.register(SimulacraScreenHandlers.STATUE_SCREEN_HANDLER_TYPE, StatueScreen::new);
		HandledScreens.register(SimulacraScreenHandlers.MANNEQUIN_SCREEN_HANDLER_TYPE, MannequinScreen::new);
		ClientPlayNetworking.registerGlobalReceiver(FlingMannequinPacket.ID, FlingMannequinPacket::handle);
	}
}
