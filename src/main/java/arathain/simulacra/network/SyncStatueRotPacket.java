package arathain.simulacra.network;

import arathain.simulacra.Simulacra;
import arathain.simulacra.entity.MannequinEntity;
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

public class SyncStatueRotPacket {
	public static final Identifier ID = new Identifier(Simulacra.MODID, "sync_rotation");

	public static void send(@Nullable Entity entity, int i, EulerAngle angle) {
		PacketByteBuf buf = PacketByteBufs.create();

		if(entity instanceof StatueEntity || entity instanceof MannequinEntity) {
			buf.writeInt(entity.getId());
			buf.writeInt(i);
			buf.writeFloat(angle.getPitch());
			buf.writeFloat(angle.getYaw());
			buf.writeFloat(angle.getRoll());
		}

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int entityId = buf.isReadable() ? buf.readInt() : -1;
		int partId = buf.isReadable() ? buf.readInt() : -1;
		EulerAngle angle = StatueEntity.ZERO_ROT;
		if(buf.isReadable()) {
			angle = new EulerAngle(buf.readFloat(), buf.readFloat(), buf.readFloat());
		}
		EulerAngle finalAngle = angle;
		server.execute(() -> {
			if(entityId != -1) {
				Entity ent = player.getWorld().getEntityById(entityId);
				if(ent instanceof StatueEntity statue) {
					switch(partId) {
						case 0 ->
							statue.setHeadRot(finalAngle);
						case 1 ->
							statue.setBodyRot(finalAngle);
						case 3 ->
							statue.setLeftArmRot(finalAngle);
						case 2 ->
							statue.setRightArmRot(finalAngle);
						default -> {}
					}
				} else if(ent instanceof MannequinEntity man) {
					switch(partId) {
						case 0 -> man.setHeadRot(finalAngle);
						case 1 -> man.setBodyRot(finalAngle);
						case 3 -> man.setLeftArmRot(finalAngle);
						case 2 -> man.setRightArmRot(finalAngle);
						default -> {}
					}
				}
			}
		});
	}
}
