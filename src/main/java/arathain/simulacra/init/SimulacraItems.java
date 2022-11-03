package arathain.simulacra.init;

import arathain.simulacra.Simulacra;
import arathain.simulacra.entity.StatueItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulacraItems {
	static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	public static final Item STATUE = create("statue", new StatueItem(new QuiltItemSettings().group(ItemGroup.DECORATIONS).maxCount(32)));

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, new Identifier(Simulacra.MODID, name));
		return item;
	}

	public static void init() {
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}
}
