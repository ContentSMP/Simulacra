package arathain.simulacra.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.datafixer.fix.EntityRavagerRenameFix;
import net.minecraft.datafixer.fix.EntityRenameFix;

import java.util.Map;
import java.util.Objects;

public class StatueRenameFix extends EntityRenameFix {
	public StatueRenameFix(Schema outputSchema, boolean changesType) {
		super("StatueRenameFix", outputSchema, changesType);
	}

	protected String rename(String oldName) {
		return Objects.equals("mannequins:statue", oldName) ? "simulacra:statue" : oldName;
	}
}
