package cloud.jarsey45.tankiez.networking.packet.interfaces;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IBaseS2CPacket {
	/**
	 * Write data to buffer
	 */
	void toBytes(FriendlyByteBuf buffer);

	/**
	 * Delegate function to client side
	 */
	boolean handle(Supplier<NetworkEvent.Context> supplier);
}
