package cloud.jarsey45.tankiez.networking.packet;

import cloud.jarsey45.tankiez.blocks.entity.BasicTankEntity;
import cloud.jarsey45.tankiez.networking.packet.interfaces.IBaseS2CPacket;
import cloud.jarsey45.tankiez.screen.BasicTankMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidSyncS2CPacket implements IBaseS2CPacket {
	private final FluidStack fluidStack;
	private final BlockPos pos;

	public FluidSyncS2CPacket(FluidStack fluidStack, BlockPos pos) {
		this.fluidStack = fluidStack;
		this.pos = pos;
	}

	public FluidSyncS2CPacket(FriendlyByteBuf buffer) {
		this.fluidStack = buffer.readFluidStack();
		this.pos = buffer.readBlockPos();
	}

	public void toBytes(FriendlyByteBuf buffer) {
		buffer.writeFluidStack(this.fluidStack);
		buffer.writeBlockPos(this.pos);
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context ctx = supplier.get();

		ctx.enqueueWork(() -> {
			if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof BasicTankEntity blockEntity) {
				blockEntity.setFluid(this.fluidStack);

				if(Minecraft.getInstance().player.containerMenu instanceof BasicTankMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
					menu.setFluid(this.fluidStack);
				}
			}
		});

		return true;
	}

}
