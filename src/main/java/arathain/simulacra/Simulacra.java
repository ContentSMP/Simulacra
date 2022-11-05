package arathain.simulacra;

import arathain.simulacra.client.screen.StatueScreenHandler;
import arathain.simulacra.entity.StatueRenameFix;
import arathain.simulacra.init.SimulacraEntities;
import arathain.simulacra.init.SimulacraItems;
import arathain.simulacra.init.SimulacraScreenHandlers;
import arathain.simulacra.network.SyncRetainPacket;
import arathain.simulacra.network.SyncStatueRotPacket;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.schemas.Schema;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.SharedConstants;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.datafixerupper.api.QuiltDataFixes;
import org.quiltmc.qsl.datafixerupper.api.SimpleFixes;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Simulacra implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "simulacra";
	public static final Logger LOGGER = LoggerFactory.getLogger("Simulacra");

	@Override
	public void onInitialize(ModContainer mod) {
		SimulacraEntities.init();
		SimulacraScreenHandlers.init();
		SimulacraItems.init();
		ServerPlayNetworking.registerGlobalReceiver(SyncStatueRotPacket.ID, SyncStatueRotPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(SyncRetainPacket.ID, SyncRetainPacket::handle);

		QuiltDataFixerBuilder builder = new QuiltDataFixerBuilder(1);
		builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA);
		Schema schemaV1 = builder.addSchema(1, IdentifierNormalizingSchema::new);
		builder.addFixer(new StatueRenameFix(schemaV1, false));
		SimpleFixes.addItemRenameFix(builder, "Retarget mannequins modid for statue item", new Identifier("mannequins", "statue"), new Identifier(MODID, "statue"), schemaV1);
		QuiltDataFixes.registerFixer(mod, 1, builder.build(Util::getBootstrapExecutor));
	}
}
