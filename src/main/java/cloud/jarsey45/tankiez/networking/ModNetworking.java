package cloud.jarsey45.tankiez.networking;

import cloud.jarsey45.tankiez.Tankiez;
import cloud.jarsey45.tankiez.networking.packet.FluidSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
	private static SimpleChannel INSTANCE;

	private static int packetID = 0;
	private static int getID() {
		return packetID++;
	}

	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder
						.named(new ResourceLocation(Tankiez.MOD_ID, "messages"))
						.networkProtocolVersion(() -> "1.0")
						.clientAcceptedVersions((s) -> true)
						.serverAcceptedVersions((s) -> true)
						.simpleChannel();

		INSTANCE = net;

		net.messageBuilder(FluidSyncS2CPacket.class, getID(), NetworkDirection.PLAY_TO_CLIENT)
						.decoder(FluidSyncS2CPacket::new)
						.encoder(FluidSyncS2CPacket::toBytes)
						.consumerMainThread(FluidSyncS2CPacket::handle)
						.add();
	}

	public static <MSG_T> void sendToServer(MSG_T message) {
		INSTANCE.sendToServer(message);
	}

	public static <MSG_T> void sendToPlayer(MSG_T message, ServerPlayer player) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}

	public static <MSG_T> void sendToClients(MSG_T message) {
		INSTANCE.send(PacketDistributor.ALL.noArg(), message);
	}
}
