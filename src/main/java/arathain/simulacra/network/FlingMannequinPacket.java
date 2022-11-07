package arathain.simulacra.network;

import arathain.simulacra.Simulacra;
import arathain.simulacra.entity.MannequinEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class FlingMannequinPacket {
	public static final Identifier ID = new Identifier(Simulacra.MODID, "fling");

	public static void send(@Nullable Entity entity, float h) {
		PacketByteBuf buf = PacketByteBufs.create();

		if(entity instanceof MannequinEntity) {
			buf.writeInt(entity.getId());
			buf.writeFloat(h);
		}
		if(entity.getWorld() instanceof ServerWorld s) {
			System.out.println("yea");
			ServerPlayNetworking.send(s.getPlayers(), ID, buf);
		}
	}


	public static void handle(MinecraftClient client, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender) {
		int entityId = buf.isReadable() ? buf.readInt() : -1;
		float angle = buf.isReadable() ? buf.readFloat() : -1;
		if(entityId != -1 && client.world.getEntityById(entityId) instanceof MannequinEntity man) {
			man.onAttack(angle);
		}
	}
}
