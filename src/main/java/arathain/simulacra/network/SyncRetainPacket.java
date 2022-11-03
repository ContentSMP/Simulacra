package arathain.simulacra.network;

import arathain.simulacra.Simulacra;
import arathain.simulacra.entity.StatueEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.EulerAngle;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class SyncRetainPacket {
	public static final Identifier ID = new Identifier(Simulacra.MODID, "sync_retain");

	public static void send(@Nullable Entity entity, boolean bl) {
		PacketByteBuf buf = PacketByteBufs.create();

		if(entity instanceof StatueEntity) {
			buf.writeInt(entity.getId());
			buf.writeBoolean(bl);
		}

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int entityId = buf.isReadable() ? buf.readInt() : -1;
		boolean retain = buf.readBoolean();
		server.execute(() -> {
			if(entityId != -1) {
				Entity ent = player.getWorld().getEntityById(entityId);
				if(ent instanceof StatueEntity statue) {
					statue.setRetain(retain);
				}
			}
		});
	}
}
