package arathain.simulacra.init;

import arathain.simulacra.Simulacra;
import arathain.simulacra.client.screen.MannequinScreenHandler;
import arathain.simulacra.client.screen.StatueScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

import static arathain.simulacra.Simulacra.MODID;

public class SimulacraScreenHandlers {
	public static final ExtendedScreenHandlerType<StatueScreenHandler> STATUE_SCREEN_HANDLER_TYPE = register("statue", StatueScreenHandler::new);
	public static final ExtendedScreenHandlerType<MannequinScreenHandler> MANNEQUIN_SCREEN_HANDLER_TYPE = register("mannequin", MannequinScreenHandler::new);


	private static <T extends ScreenHandler> ExtendedScreenHandlerType<T> register(String id, ExtendedScreenHandlerType.ExtendedFactory<T> factory) {
		return Registry.register(Registry.SCREEN_HANDLER, id, new ExtendedScreenHandlerType<>(factory));
	}
	public static void init() {}
}
