package arathain.simulacra.init;

import arathain.simulacra.entity.MannequinEntity;
import arathain.simulacra.entity.StatueEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;
import org.quiltmc.qsl.entity.mixin.DefaultAttributeRegistryMixin;

import java.util.LinkedHashMap;
import java.util.Map;

import static arathain.simulacra.Simulacra.MODID;

public class SimulacraEntities {
	private static final Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

	public static final EntityType<StatueEntity> STATUE = createEntity("statue", StatueEntity.createLivingAttributes().add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1), QuiltEntityTypeBuilder.create(SpawnGroup.MISC, StatueEntity::new).setDimensions(EntityDimensions.fixed(0.85F, 1.8F)).build());
	public static final EntityType<MannequinEntity> MANNEQUIN = createEntity("mannequin", StatueEntity.createLivingAttributes(), QuiltEntityTypeBuilder.create(SpawnGroup.MISC, MannequinEntity::new).setDimensions(EntityDimensions.fixed(0.85F, 1.8F)).build());

	public static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		ENTITY_TYPES.put(new Identifier(MODID, id), type);
		return type;
	}

	private static <T extends LivingEntity> EntityType<T> createEntity(String name, DefaultAttributeContainer.Builder attributes, EntityType<T> type) {
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(type, attributes.build());
		ENTITY_TYPES.put(new Identifier(MODID, name), type);
		return type;
	}

	public static void init() {
		ENTITY_TYPES.forEach((id, entityType) -> Registry.register(Registry.ENTITY_TYPE, id, entityType));
	}
}
