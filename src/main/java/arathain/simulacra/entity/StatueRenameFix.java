package arathain.simulacra.entity;

import com.mojang.datafixers.schemas.Schema;
import net.minecraft.datafixer.fix.EntityRenameFix;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;

import java.util.Objects;

public class StatueRenameFix extends EntityRenameFix {
	public StatueRenameFix(Schema outputSchema, boolean changesType) {
		super("StatueRenameFix", outputSchema, changesType);
	}

	protected String rename(String oldName) {
		return Objects.equals(IdentifierNormalizingSchema.normalize(oldName), "mannequins:statue") ? "simulacra:statue" : oldName;
	}
}
