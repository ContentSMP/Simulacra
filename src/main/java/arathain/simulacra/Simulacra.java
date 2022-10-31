package arathain.simulacra;

import arathain.simulacra.init.SimulacraEntities;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
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
	}
}
